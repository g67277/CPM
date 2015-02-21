package com.android.nazirshuqair.trackit.editview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.nazirshuqair.trackit.R;

/**
 * Created by nazirshuqair on 2/5/15.
 */
public class ViewFragment extends Fragment {

    //Tag to identify the fragment
    public static final String TAG = "ViewFragment.TAG";

    private static final String ARG_TITLE = "ViewFragment.ARG_TITLE";
    private static final String ARG_DESCRIPTION = "ViewFragment.ARG_DESCRIPTION";
    private static final String ARG_PRIORITY = "ViewFragment.ARG_PRIORITY";
    private static final String ARG_POSITION = "ViewFragment.ARG_POSITION";
    private static final String ARG_CREATEDAT = "ViewFragment.ARG_UPDATEDAT";
    private static final String ARG_UPDATEDAT = "ViewFragment.ARG_UPDATEDAT";
    static String priorityLevel;

    TextView titleView;
    TextView descriptionView;
    TextView priorityView;

    //This is to create a new instance of the fragment
    public static ViewFragment newInstance(String _title, String _description, int _priority, int _position) {
        ViewFragment frag = new ViewFragment();

        priorityIcons(_priority);

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, _title);
        args.putString(ARG_DESCRIPTION, _description);
        args.putString(ARG_PRIORITY, priorityLevel);
        args.putInt(ARG_POSITION, _position);
        frag.setArguments(args);

        return frag;
    }

    public interface EditDeleteListener {
        public void deleteTask(int _index);
        public void edittask();
        public void refreshConnection();
    }

    private EditDeleteListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof EditDeleteListener) {
            mListener = (EditDeleteListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_view, container, false);
        //Connecting the ListView
        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_TITLE)){
            setDisplay(args.getString(ARG_TITLE),
                    args.getString(ARG_DESCRIPTION),
                    args.getString(ARG_PRIORITY));

            Button deleteBtn = (Button) getView().findViewById(R.id.view_deleteBtn);
            deleteBtn.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mListener.refreshConnection();
                    if (DetailsActivity.isOnline){
                        mListener.deleteTask(args.getInt(ARG_POSITION));
                    }else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Offline")
                                .setMessage("Please connect to delete Tasks")
                                .setCancelable(true)
                                .create()
                                .show();
                    }
                }
            });
        }

    }

    public void setDisplay(String _title, String _description, String _priority){

        //Connecting the textView
        titleView = (TextView) getView().findViewById(R.id.view_title);
        descriptionView = (TextView) getView().findViewById(R.id.view_description);
        priorityView = (TextView) getView().findViewById(R.id.view_priority);

        titleView.setText(_title);
        descriptionView.setText(_description);
        priorityView.setText(_priority);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem saveItem = menu.add("Edit");
        saveItem.setShowAsAction(1);
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mListener.refreshConnection();
                if (DetailsActivity.isOnline){
                    mListener.edittask();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Offline")
                            .setMessage("Please connect to edit Tasks")
                            .setCancelable(true)
                            .create()
                            .show();
                }
                return false;
            }
        });
    }

    public static void priorityIcons(int _priority){

        switch (_priority){
            case 1: priorityLevel = "!";
                break;
            case 2: priorityLevel = "!!";
                break;
            case 3: priorityLevel = "!!!";
                break;
            case 4: priorityLevel = "!!!!";
        }
    }

}
