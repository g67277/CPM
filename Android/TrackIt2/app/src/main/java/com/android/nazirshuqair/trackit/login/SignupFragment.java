package com.android.nazirshuqair.trackit.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.nazirshuqair.trackit.R;
import com.android.nazirshuqair.trackit.mainscreens.MainActivity;

/**
 * Created by nazirshuqair on 2/3/15.
 */
public class SignupFragment extends Fragment {

    public static final String TAG = "SignupFragment.TAG";

    EditText username;
    EditText email;
    EditText pass;
    Button signupBtn;
    Button signINRedirect;
    boolean goodEmail, goodUserName, goodPassword;

    //This is to create a new instance of the fragment
    public static SignupFragment newInstance() {
        SignupFragment frag = new SignupFragment();
        return frag;
    }

    public interface SignupListener {
        public void signUp(String _email, String _userName, String _password);
        public void signINRedirect();
        public void refreshConnection();
    }

    private SignupListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof SignupListener) {
            mListener = (SignupListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_signup, container, false);

        username = (EditText) myFragmentView.findViewById(R.id.signup_username);
        Log.i("TESTING", username.getText().toString());
        email = (EditText) myFragmentView.findViewById(R.id.signup_email);
        pass = (EditText) myFragmentView.findViewById(R.id.signup_password);
        signupBtn = (Button) myFragmentView.findViewById(R.id.signup_button);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().length() < 1){
                    email.setHint("Required");
                    email.setHintTextColor(getResources().getColor(R.color.material_deep_teal_500));
                    goodEmail = false;
                }else if (!isEmailValid(email.getText().toString())){
                    email.setText("");
                    email.setHint(Html.fromHtml("<font color='#009688'>Please enter a valid email</font> "));
                    goodEmail = false;
                } else{
                    goodEmail = true;
                }
                if (username.getText().length() < 1){
                    username.setHint("Required");
                    username.setHintTextColor(getResources().getColor(R.color.material_deep_teal_500));
                    goodUserName = false;
                }else {
                    goodUserName = true;
                }
                if (pass.getText().length() < 1){
                    pass.setHint("Required");
                    pass.setHintTextColor(getResources().getColor(R.color.material_deep_teal_500));
                    goodPassword = false;
                }else {
                    if (pass.getText().length() < 4) {
                        pass.setText("");
                        pass.setHint("Password is too short");
                        goodPassword = false;
                    }else {
                        goodPassword = true;
                    }
                }

                if (goodEmail && goodUserName && goodPassword){
                    mListener.refreshConnection();
                    if (MainActivity.isOnline){
                        mListener.signUp(email.getText().toString().toLowerCase(),
                                username.getText().toString().toLowerCase(),
                                pass.getText().toString());
                    }else {
                        alerts("Offline", "Please connect to Sign Up");
                    }
                }
            }
        });
        signINRedirect = (Button) myFragmentView.findViewById(R.id.SignINSecondary);
        signINRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.refreshConnection();
                if (MainActivity.isOnline){
                    mListener.signINRedirect();
                }else {
                    alerts("Offline", "Please connect to Sign In");
                }
            }
        });

        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isConnected();
    }

    public void isConnected(){

        if (!MainActivity.isOnline){
            alerts("Offline", "Please connect to Sign Up");
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void alerts(String _title, String _message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(_title)
                .setMessage(_message)
                .setCancelable(true)
                .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.refreshConnection();
                        isConnected();
                    }
                })
                .create()
                .show();
    }
}
