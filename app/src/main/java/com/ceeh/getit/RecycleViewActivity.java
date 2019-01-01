package com.ceeh.getit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ceeh.getit.Model.Task;
import com.ceeh.getit.Model.TaskDatabase;
import com.ceeh.getit.Model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecycleViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Task> tasks = new ArrayList<>(9);
    private TaskDatabase handler;
    private Button addTask;
    private Button setting;

    private TextView _today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);

        _today = (TextView) findViewById(R.id.date_recycle);
        Calendar calendar = Calendar.getInstance();
        _today.setText(calendar.get(Calendar.MONTH) + 1 + "/"
                + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                calendar.get(Calendar.YEAR));

        Intent active = new Intent();
        final int activeValue = active.getIntExtra("INACTIVE", 1);




        handler = new TaskDatabase(this);
        addTask = (Button) findViewById(R.id.add_recycle);
        setting = (Button) findViewById(R.id.settings_recycle);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(RecycleViewActivity.this,AddTaskActivity.class );
                add.putExtra("INACTIVE", activeValue);
                startActivity(add);
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(RecycleViewActivity.this,SettingsActivity.class );
                startActivity(settings);
                finish();
            }
        });

        tasks = handler.getAllTask();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);



        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(tasks);
        mRecyclerView.setAdapter(mAdapter);



    }


}
