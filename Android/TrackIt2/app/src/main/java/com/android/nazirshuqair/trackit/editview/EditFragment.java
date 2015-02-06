package com.android.nazirshuqair.trackit.editview;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.nazirshuqair.trackit.R;

/**
 * Created by nazirshuqair on 2/5/15.
 */
public class EditFragment extends Fragment {

    //Tag to identify the fragment
    public static final String TAG = "EditFragment.TAG";
    private static final String ARG_TITLE = "ViewFragment.ARG_TITLE";
    private static final String ARG_DESCRIPTION = "ViewFragment.ARG_DESCRIPTION";
    private static final String ARG_PRIORITY = "ViewFragment.ARG_PRIORITY";
    private static final String ARG_POSITION = "ViewFragment.ARG_POSITION";

    int priorityNum;

    EditText title;
    EditText description;
    EditText timeSpent;
    RadioGroup priorityGroup;
    RadioButton priority1, priority2, priority3, priority4;
    Button trackBtn;

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
                mListener.addTask(title.getText().toString(), description.getText().toString(),
                        priorityNum);
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
                    mListener.saveEdits(title.getText().toString(), description.getText().toString(),
                            priorityNum);
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
