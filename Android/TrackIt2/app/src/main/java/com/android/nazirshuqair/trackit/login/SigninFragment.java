package com.android.nazirshuqair.trackit.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.nazirshuqair.trackit.R;
import com.android.nazirshuqair.trackit.mainscreens.MainActivity;

/**
 * Created by nazirshuqair on 2/4/15.
 */
public class SigninFragment extends Fragment {

    public static final String TAG = "SigninFragment.TAG";

    EditText username;
    EditText pass;
    Button signinBtn;
    Button signUPRedirect;
    Button resetPass;

    //This is to create a new instance of the fragment
    public static SigninFragment newInstance() {
        SigninFragment frag = new SigninFragment();
        return frag;
    }

    public interface SigninListener {
        public void signIn(String _userName, String _password);
        public void signUPredirect();
        public void resetPassword();
        public void refreshConnection();
    }

    private SigninListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof SigninListener) {
            mListener = (SigninListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_signin, container, false);

        username = (EditText) myFragmentView.findViewById(R.id.signin_username);
        pass = (EditText) myFragmentView.findViewById(R.id.signin_pass);
        signinBtn = (Button) myFragmentView.findViewById(R.id.signin_btn);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.refreshConnection();
                if (MainActivity.isOnline){
                    mListener.signIn(username.getText().toString().toLowerCase(), pass.getText().toString());
                }else {
                    alerts("Offline", "Please connect to Sign In");
                }
            }
        });
        signUPRedirect = (Button) myFragmentView.findViewById(R.id.signUPSecondary);
        signUPRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.refreshConnection();
                if (MainActivity.isOnline){
                    mListener.signUPredirect();
                }else {
                    alerts("Offline", "Please connect to Sign Up");
                }
            }
        });
        resetPass = (Button) myFragmentView.findViewById(R.id.signin_resetPass);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.refreshConnection();
                if (MainActivity.isOnline){
                    mListener.resetPassword();
                }else {
                    alerts("Offline", "Please connect to Reset Password");
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
            alerts("Offline", "Please connect to Sign In");
        }
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
