package com.android.nazirshuqair.trackit.mainscreens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.nazirshuqair.trackit.R;
import com.android.nazirshuqair.trackit.editview.DetailsActivity;
import com.android.nazirshuqair.trackit.login.ResetPasswordFragment;
import com.android.nazirshuqair.trackit.login.SigninFragment;
import com.android.nazirshuqair.trackit.login.SignupFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements SignupFragment.SignupListener, SigninFragment.SigninListener, MasterFragment.MasterListener,
        ResetPasswordFragment.ResetListener{

    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_CODE2 = 2;

    SharedPreferences defaultPrefs;
    static ArrayList<ParseObject> tasksList = new ArrayList<>();
    ArrayList<ParseObject> dirtyCheck = new ArrayList<>();
    private ConnectionChangeReceiver receiver;
    static boolean isOnline;

    int taskViewed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new ConnectionChangeReceiver();
        registerReceiver(receiver, filter);

        Parse.initialize(this, "DhqgKTMhZTqw0YcwtJR5nin2d1ekmkpYU1JLnj1k", "RJpHneFnjHgqUMIi5X0vBxHddekXUUAFR9dl9fqx");
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

        //quick way to display sign-in or sign-up page
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //get cached user
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            populateTaskList();
            timerHandler.postDelayed(timerRunnable, 0);
            masterFrag();
        } else {
            if (defaultPrefs.getInt("accountCount", 0) == 0){
                signUpFrag();
            }else {
                signInFrag();
            }
        }
    }

    public static Handler timerHandler = new Handler();
    public Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if (isOnline() && ConnectionChangeReceiver.isOnline){
                isOnline = true;
                populateTaskList();
                timerHandler.postDelayed(this, 20000);
            }else{
                isOnline = false;
            }
        }
    };

//------------------------------------------Retrive tasks-------------------------------------------------------
    public void populateTaskList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> incomingTasksList, ParseException e) {
                if (e == null) {
                    tasksList = (ArrayList) incomingTasksList;
                    if (dirtyCheck.size() < 1) {
                        populateDirtyList(tasksList);
                        updateUI(tasksList);
                    }else if (dirtyCheck.size() > tasksList.size() || dirtyCheck.size() < tasksList.size() ){
                        updateUI(tasksList);
                    }else{
                        for (int i = 0; i < tasksList.size(); i++){
                            if (dirtyCheck.get(i).getUpdatedAt().getTime() != tasksList.get(i).getUpdatedAt().getTime()){
                                updateUI(tasksList);
                                populateDirtyList(tasksList);
                                break;
                            }
                        }
                    }
                    Log.i("TESTING", "Retrieved " + incomingTasksList.size() + " Tasks");
                } else {
                    Log.i("TESTING", "Error: " + e.getMessage() + " CODE: " + e.getCode());
                }
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------

    public void populateDirtyList(ArrayList<ParseObject> incomingLing){
        dirtyCheck.clear();
        for (ParseObject task: incomingLing){
            dirtyCheck.add(task);
        }
    }

    //Checking Connectivity
    protected boolean isOnline(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }

//------------------------------------------Add/View/Delete tasks-------------------------------------------------------
    @Override
    public void addTask() {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("add", true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void pushItemSelected(int _index) {

        ParseObject toBeViewed = tasksList.get(_index);
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("add", false);
        intent.putExtra("title", toBeViewed.get("title").toString());
        intent.putExtra("description", toBeViewed.get("description").toString());
        intent.putExtra("priority", toBeViewed.getInt("priority"));
        intent.putExtra("position", _index);
        intent.putExtra("objectId",toBeViewed.getObjectId());
        startActivityForResult(intent, REQUEST_CODE2);
    }

    @Override
    public void deleteTask(int _index) {
        ParseObject toBeDeleted = tasksList.get(_index);
        toBeDeleted.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    populateTaskList();
                }else {
                    Log.i("TESTING", e.getMessage() + " CODE: " + e.getCode());
                }
            }
        });
    }

    @Override
    public void refreshList() {
        populateTaskList();
    }
//--------------------------------------------------------------------------------------------------------------

//------------------------------------------Refresh List-------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            tasksList.clear();
            timerHandler.postDelayed(timerRunnable, 0);
        }else if (requestCode == REQUEST_CODE2 && resultCode == RESULT_OK){
            deleteTask(data.getIntExtra("position", 0));
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    public void updateUI(ArrayList<ParseObject> _list){

        MasterFragment frag = (MasterFragment) getFragmentManager().findFragmentByTag(MasterFragment.TAG);

        if (frag == null){
            frag = MasterFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.main_container, frag, MasterFragment.TAG).commit();
        }

        frag.updateDisplay(_list);
    }
//--------------------------------------------------------------------------------------------------------------

//------------------------------------------Sign UP-------------------------------------------------------------
    @Override
    public void signUp(String _email, String _userName, String _password) {

        ParseUser user = new ParseUser();
        user.setEmail(_email);
        user.setUsername(_userName);
        user.setPassword(_password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    masterFrag();

                    SharedPreferences.Editor edit = defaultPrefs.edit();
                    edit.putInt("accountCount", 1);
                    edit.apply();

                }else {
                    ParseException test = e;
                    if (e.getCode() == 202){
                        alerts(e.getMessage(), "Please choose another one");
                    }else if (e.getCode() == 203){
                        alerts(e.getMessage(), "Please enter another or reset your password");
                    }
                    Log.i("TESTING", e.getMessage() + " Code:" + e.getCode());
                }
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------

//------------------------------------------Sign IN-------------------------------------------------------------
    @Override
    public void signIn(String _userName, String _password) {

        ParseUser.logInInBackground(_userName, _password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null){
                    masterFrag();
                    populateTaskList();

                    if (defaultPrefs.getInt("accountCount", 0) == 0) {
                        SharedPreferences.Editor edit = defaultPrefs.edit();
                        edit.putInt("accountCount", 1);
                        edit.apply();
                    }
                }else{
                    ParseException test = e;
                    if (e.getCode() == 101) {
                        alerts("Invalid Username or Password", "Make sure you're entering valid logins");
                    }
                    Log.i("TESTING", e.toString());
                }
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------

//------------------------------------------Login Redirects------------------------------------------------------
    @Override
    public void signINRedirect() {
        signInFrag();
    }
    @Override
    public void signUPredirect() {
        signUpFrag();
    }
//-----------------------------------------------------------------------------------------------------------------

//------------------------------------------Reset Password------------------------------------------------------

    @Override
    public void resetPassword() {
        ResetPasswordFragment frag = ResetPasswordFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_container, frag, ResetPasswordFragment.TAG).commit();
    }
//-----------------------------------------------------------------------------------------------------------------

//------------------------------------------Log Out------------------------------------------------------

    @Override
    public void logOut() {
        ParseUser.logOut();
        signInFrag();
    }
//-----------------------------------------------------------------------------------------------------------------

//------------------------------------------Frags Methods------------------------------------------------------

    public void masterFrag(){
        MasterFragment frag = MasterFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_container, frag, MasterFragment.TAG).commit();
    }
    public void signInFrag(){
        SigninFragment frag  = SigninFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_container, frag, SigninFragment.TAG).commit();
    }
    public void signUpFrag(){
        SignupFragment frag  = SignupFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_container, frag, SignupFragment.TAG).commit();
    }
//-----------------------------------------------------------------------------------------------------------------

//------------------------------------------Alert Methods------------------------------------------------------

    public void alerts(String _title, String _message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(_title)
                .setMessage(_message)
                .setCancelable(true)
                .create()
                .show();
    }
//-----------------------------------------------------------------------------------------------------------------

//------------------------------------------Reset password------------------------------------------------------

    @Override
    public void resetEmail(String _email) {
        ParseUser.requestPasswordResetInBackground(_email,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            alerts("Email Sent", "Please follow the email to reset your password");
                        } else {

                        }
                    }
                });
        signInFrag();
    }

    @Override
    public void resetCancel() {
        signInFrag();
    }
//-----------------------------------------------------------------------------------------------------------------

    public static class ConnectionChangeReceiver extends BroadcastReceiver
    {
        boolean firstConnect = true;
        public static boolean isOnline = true;

        @Override
        public void onReceive( Context context, Intent intent )
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if ( activeNetInfo != null )
            {
                if (firstConnect){
                    Toast.makeText(context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    firstConnect = false;
                    isOnline = true;
                }
            }if (activeNetInfo == null){

                if (!firstConnect){
                    Toast.makeText(context, "You're offline", Toast.LENGTH_SHORT).show();
                    firstConnect = true;
                    isOnline = false;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
