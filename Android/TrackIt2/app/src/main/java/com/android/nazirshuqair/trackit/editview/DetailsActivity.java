package com.android.nazirshuqair.trackit.editview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.nazirshuqair.trackit.R;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;

/**
 * Created by nazirshuqair on 2/5/15.
 */
public class DetailsActivity extends ActionBarActivity implements EditFragment.AddListener, ViewFragment.EditDeleteListener{

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //enables the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent.getBooleanExtra("add", true)){
            if(savedInstanceState == null) {
                EditFragment frag = EditFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.detail_container, frag, EditFragment.TAG).commit();
            }
        }else {
            if(savedInstanceState == null) {
                ViewFragment frag = ViewFragment.newInstance(intent.getStringExtra("title"),
                        intent.getStringExtra("description"),
                        intent.getIntExtra("priority", 1),
                        intent.getIntExtra("position", 0));
                getFragmentManager().beginTransaction().replace(R.id.detail_container, frag, ViewFragment.TAG).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addTask(String _title, String _description, int _priority) {

        ParseObject gameScore = new ParseObject("Tasks");
        gameScore.put("title", _title);
        gameScore.put("description", _description);
        gameScore.put("priority", _priority);
        gameScore.saveInBackground();

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void saveEdits(final String _title, final String _description, final int _priority) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tasks");
        Intent intent = getIntent();

        // Retrieve the object by id
        query.getInBackground(intent.getStringExtra("objectId"), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject taskObject, com.parse.ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    taskObject.put("title", _title);
                    taskObject.put("description", _description);
                    taskObject.put("priority", _priority);
                    taskObject.saveInBackground();

                    ViewFragment frag = ViewFragment.newInstance(_title,
                            _description,
                            _priority,
                            position);
                    getFragmentManager().beginTransaction().replace(R.id.detail_container, frag, ViewFragment.TAG).commit();
                }else{
                    Log.i("TESTING", e.getMessage() + " CODE: " + e.getCode());
                }
            }
        });
    }

    @Override
    public void edittask() {

        Intent intent = getIntent();

        EditFragment frag = EditFragment.newInstance(intent.getStringExtra("title"),
                intent.getStringExtra("description"),
                intent.getIntExtra("priority", 1));
        getFragmentManager().beginTransaction().replace(R.id.detail_container, frag, EditFragment.TAG).commit();
    }

    @Override
    public void deleteTask(int _index) {
        Intent intent = new Intent();
        intent.putExtra("position", _index);
        setResult(RESULT_OK, intent);
        finish();
    }


}
