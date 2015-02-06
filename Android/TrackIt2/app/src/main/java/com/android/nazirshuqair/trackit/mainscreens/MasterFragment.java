package com.android.nazirshuqair.trackit.mainscreens;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.nazirshuqair.trackit.R;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nazirshuqair on 2/4/15.
 */
public class MasterFragment extends Fragment {

    public static final String TAG = "MasterFragment.TAG";
    public static final String TITLE = "TITLE";
    public static final String PRIORITY = "PRIORITY";

    ListView tasksListView;
    SimpleAdapter adapter;
    private ActionMode mActionMode;
    private int taskSelected = -1;
    String priorityIcons;

    //This is a test, probably need to change
    private boolean longClick = false;

    public ArrayList<HashMap<String, Object>> tasks = new ArrayList<HashMap<String, Object>>();

    //This is to create a new instance of the fragment
    public static MasterFragment newInstance() {
        MasterFragment frag = new MasterFragment();
        return frag;
    }

    public void updateDisplay (ArrayList<ParseObject> _object){
        tasks.clear();
        for (ParseObject task: _object){
            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put(TITLE, task.get("title"));

            switch (task.getInt("priority")){
                case 1: priorityIcons = "!";
                    break;
                case 2: priorityIcons = "!!";
                    break;
                case 3: priorityIcons = "!!!";
                    break;
                case 4: priorityIcons = "!!!!";
            }
            map.put(PRIORITY, priorityIcons);

            tasks.add(map);
        }
        tasksListView.invalidateViews();
    }

    public interface MasterListener{
        public void logOut();
        public void addTask();
        public void pushItemSelected(int _index);
        public void deleteTask(int _index);
    }

    private MasterListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof MasterListener) {
            mListener = (MasterListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_master, container, false);

        tasksListView = (ListView) myFragmentView.findViewById(R.id.master_listView);

        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // Creating an array of our keys
        String[] keys = new String[] {
                TITLE, PRIORITY
        };

        // Creating an array of our list item components.
        // Indices must match the keys array.
        int[] views = new int[] {
                R.id.list_title,
                R.id.list_priority
        };

        //creating an adapter to populate the listview
        adapter = new SimpleAdapter(getActivity(), tasks,  R.layout.list_item, keys, views);
        tasksListView.setAdapter(adapter);

        tasksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (mActionMode != null){
                    return false;
                }
                taskSelected = i;
                mActionMode = getActivity().startActionMode(mActionModeCallBack);
                longClick = true;

                return false;
            }
        });

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Call the displayDetails method and pass the adapter view and position
                //to populate details elements
                if (!longClick) {
                    mListener.pushItemSelected(position);
                }
                longClick = false;
            }
        });
    }

    //Contextual Actionbar Listener

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback(){

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.delete_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {

            switch (item.getItemId()){
                case R.id.taskDelete:
                    mListener.deleteTask(taskSelected);
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }
        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem saveItem = menu.add("Add");
        saveItem.setShowAsAction(1);

        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mListener.addTask();
                return false;
            }
        });
        MenuItem resetForm = menu.add("Log out");
        resetForm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mListener.logOut();
                return false;
            }
        });
    }


}
