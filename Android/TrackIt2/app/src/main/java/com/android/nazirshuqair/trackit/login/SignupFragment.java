package com.android.nazirshuqair.trackit.login;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.nazirshuqair.trackit.R;

/**
 * Created by nazirshuqair on 2/3/15.
 */
public class SignupFragment extends Fragment {

    public static final String TAG = "SignupFragment.TAG";

    EditText username;
    EditText email;
    EditText pass;
    Button signupBtn;

    boolean goodEmail, goodUserName, goodPassword;

    //This is to create a new instance of the fragment
    public static SignupFragment newInstance() {
        SignupFragment frag = new SignupFragment();
        return frag;
    }

    public interface SignupListener {
        public void signUp(String _email, String _userName, String _password);
        public void signINRedirect();
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
                    mListener.signUp(email.getText().toString().toLowerCase(),
                            username.getText().toString().toLowerCase(),
                            pass.getText().toString());
                }
            }
        });
        Button signINRedirect = (Button) myFragmentView.findViewById(R.id.SignINSecondary);
        signINRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.signINRedirect();
            }
        });

        return myFragmentView;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
