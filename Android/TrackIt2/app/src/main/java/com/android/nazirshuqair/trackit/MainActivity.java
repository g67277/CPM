package com.android.nazirshuqair.trackit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.android.nazirshuqair.trackit.login.MasterFragment;
import com.android.nazirshuqair.trackit.login.ResetPasswordFragment;
import com.android.nazirshuqair.trackit.login.SigninFragment;
import com.android.nazirshuqair.trackit.login.SignupFragment;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;


public class MainActivity extends ActionBarActivity
        implements SignupFragment.SignupListener, SigninFragment.SigninListener, MasterFragment.MasterListener,
        ResetPasswordFragment.ResetListener{

    SharedPreferences defaultPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "DhqgKTMhZTqw0YcwtJR5nin2d1ekmkpYU1JLnj1k", "RJpHneFnjHgqUMIi5X0vBxHddekXUUAFR9dl9fqx");

        //quick way to display sign-in or sign-up page
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //get cached user
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            masterFrag();
        } else {
            if (defaultPrefs.getInt("accountCount", 0) == 0){
                signUpFrag();
            }else {
                signInFrag();
            }
        }

    }

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
                    }
                    Log.i("TESTING", e.toString());
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


}
