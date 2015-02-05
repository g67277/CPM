package com.android.nazirshuqair.trackit.login;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.nazirshuqair.trackit.R;

/**
 * Created by nazirshuqair on 2/4/15.
 */
public class SigninFragment extends Fragment {

    public static final String TAG = "SigninFragment.TAG";

    EditText username;
    EditText pass;
    Button signinBtn;

    //This is to create a new instance of the fragment
    public static SigninFragment newInstance() {
        SigninFragment frag = new SigninFragment();
        return frag;
    }

    public interface SigninListener {
        public void signIn(String _userName, String _password);
        public void signUPredirect();
        public void resetPassword();
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
                mListener.signIn(username.getText().toString().toLowerCase(), pass.getText().toString());
            }
        });
        Button signUPRedirect = (Button) myFragmentView.findViewById(R.id.signUPSecondary);
        signUPRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.signUPredirect();
            }
        });
        Button resetPass = (Button) myFragmentView.findViewById(R.id.signin_resetPass);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.resetPassword();
            }
        });

        return myFragmentView;
    }
}
