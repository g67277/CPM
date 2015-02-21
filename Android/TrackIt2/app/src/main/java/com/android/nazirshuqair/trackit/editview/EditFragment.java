package com.android.nazirshuqair.trackit.editview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.nazirshuqair.trackit.R;
import com.android.nazirshuqair.trackit.mainscreens.MainActivity;

/**
 * Created by nazirshuqair on 2/5/15.
 */
public class EditFragment extends Fragment {

    //Tag to identify the fragment
    public static final String TAG = "EditFragment.TAG";
    private static final String ARG_TITLE = "ViewFragment.ARG_TITLE";
    private static final String ARG_DESCRIPTION = "ViewFragment.ARG_DESCRIPTION";
    private static final String ARG_PRIORITY = "ViewFragment.ARG_PRIORITY";

    int priorityNum = 0;

    EditText title;
    EditText description;
    RadioGroup priorityGroup;
    RadioButton priority1, priority2, priority3, priority4;
    Button trackBtn;
    boolean goodTitle;
    boolean goodPriority;

    //This is to create a new instance of the fragment
    public static EditFragment newInstance() {
        EditFragment frag = new EditFragment();
        return frag;
    }

    public static EditFragment newInstance(String _title, String _description, int _priority){

        EditFragment frag = new EditFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, _title);
        args.putString(ARG_DESCRIPTION, _description);
        args.putInt(ARG_PRIORITY, _priority);
        frag.setArguments(args);

        return frag;
    }

    public interface AddListener {
        public void addTask(String _title, String _description, int _priority);
        public void saveEdits(String _title, String _description, int _priority);
        public void refreshConnection();
    }

    private AddListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof AddListener) {
            mListener = (AddListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_edit, container, false);
        //Connecting the edittext
        title = (EditText) myFragmentView.findViewById(R.id.edit_title);
        description = (EditText) myFragmentView.findViewById(R.id.edit_disc);
        //timeSpent = (EditText) myFragmentView.findViewById(R.id.edit_timeSpent);
        priorityGroup = (RadioGroup) myFragmentView.findViewById(R.id.edit_radioGroup);
        priority1 = (RadioButton) myFragmentView.findViewById(R.id.edit_priority1);
        priority2 = (RadioButton) myFragmentView.findViewById(R.id.edit_priority2);
        priority3 = (RadioButton) myFragmentView.findViewById(R.id.edit_priority3);
        priority4 = (RadioButton) myFragmentView.findViewById(R.id.edit_priority4);

        priorityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.edit_priority1: priorityNum = 1;
                        break;
                    case R.id.edit_priority2: priorityNum = 2;
                        break;
                    case R.id.edit_priority3: priorityNum = 3;
                        break;
                    case R.id.edit_priority4: priorityNum = 4;
                        break;
                    default: priorityNum = 1;
                        break;
                }
            }
        });

        trackBtn = (Button) myFragmentView.findViewById(R.id.edit_trackBtn);
        trackBtn.setText("Track It");
        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.getText().length() < 1){
                    title.setText("");
                    title.setHint(Html.fromHtml("<font color='#ff0000'>Required</font> "));
                    goodTitle = false;
                }else {
                    goodTitle = true;
                }
                if (priorityNum == 0){
                    priorityGroup.setBackgroundColor(Color.parseColor("#ff0000"));
                    goodPriority = false;
                }else {
                    goodPriority = true;
                }
                if (goodPriority && goodTitle){
                    mListener.refreshConnection();
                    if (DetailsActivity.isOnline){
                        mListener.addTask(title.getText().toString(), description.getText().toString(),
                                priorityNum);
                    }else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Offline")
                                .setMessage("Please connect to add Tasks")
                                .setCancelable(true)
                                .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mListener.refreshConnection();
                                    }
                                })
                                .create()
                                .show();
                    }
                }
                //Integer.parseInt(timeSpent.getText().toString())
            }
        });
        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_TITLE)){
            setDisplay(args.getString(ARG_TITLE),
                    args.getString(ARG_DESCRIPTION),
                    args.getInt(ARG_PRIORITY));

            trackBtn.setText("Done");
            trackBtn.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if (title.getText().length() < 1){
                        title.setText("");
                        title.setHint(Html.fromHtml("<font color='#ff0000'>Required</font> "));
                        goodTitle = false;
                    }else{
                        goodTitle = true;
                    }
                    if (goodTitle){
                            mListener.refreshConnection();
                            if (DetailsActivity.isOnline){
                                mListener.saveEdits(title.getText().toString(), description.getText().toString(),
                                        priorityNum);
                            }else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setTitle("Offline")
                                        .setMessage("Please connect to update Tasks")
                                        .setCancelable(true)
                                        .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mListener.refreshConnection();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }

                }
            });
        }
    }

    public void setDisplay(String _title, String _description, int _priority){

        title.setText(_title);
        description.setText(_description);
        switch (_priority){
            case 1: priority1.setChecked(true);
                break;
            case 2: priority2.setChecked(true);
                break;
            case 3: priority3.setChecked(true);
                break;
            case 4: priority4.setChecked(true);
                break;
        }

    }
}
