package com.android.nazirshuqair.trackit.login;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.nazirshuqair.trackit.R;

/**
 * Created by nazirshuqair on 2/4/15.
 */
public class ResetPasswordFragment extends Fragment {

    public static final String TAG = "ResetPasswordFragment.TAG";

    //This is to create a new instance of the fragment
    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment frag = new ResetPasswordFragment();
        return frag;
    }

    public interface ResetListener{
        public void resetEmail(String _email);
        public void resetCancel();
    }

    private ResetListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof ResetListener) {
            mListener = (ResetListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_reset_password, container, false);

        final EditText emailReset = (EditText) myFragmentView.findViewById(R.id.reset_email);
        Button resetBtn = (Button) myFragmentView.findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailReset.getText().length() < 0){
                    emailReset.setHint("Required");
                    emailReset.setHintTextColor(getResources().getColor(R.color.material_deep_teal_500));
                }else if(!isEmailValid(emailReset.getText().toString())){
                        emailReset.setText("");
                        emailReset.setHint(Html.fromHtml("<font color='#009688'>Please enter a valid email</font> "));
                }else {
                    mListener.resetEmail(emailReset.getText().toString().toLowerCase());
                }
            }
        });
        Button resetCancel = (Button) myFragmentView.findViewById(R.id.reset_cancel);
        resetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.resetCancel();
            }
        });

        return myFragmentView;
    }

        boolean isEmailValid(CharSequence email) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }


}
