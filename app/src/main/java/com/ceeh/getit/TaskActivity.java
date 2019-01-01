package com.ceeh.getit;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ceeh.getit.Model.Task;
import com.ceeh.getit.Model.TaskDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;




public class TaskActivity extends AppCompatActivity {
    private Button home;
    private TaskDatabase taskHandler;
    private TextView taskName;
    private TextView dueDate;
    private TextView completedDate;
    private Button completed;
    private TextView description;
    private TextView task_id;

    private Switch _activeSwitch;

    private NotificationReceiver notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        description = (TextView) findViewById(R.id.description_task);
        dueDate = (TextView) findViewById(R.id.dueDate_task);
        completedDate = (TextView) findViewById(R.id.date_task_num);
        completed = (Button) findViewById(R.id.completed_task);
        _activeSwitch = (Switch)findViewById(R.id.switch2);


        task_id = (TextView) findViewById(R.id.task_id_task);




        final NotificationReceiver notify = new NotificationReceiver();
        taskHandler = new TaskDatabase(this);


        Intent intent = getIntent();
        Long id = intent.getLongExtra("TASK_ID", 1);

        final Task _task = taskHandler.getTaskById(id);

        taskName = (TextView) findViewById(R.id.mytask_task);
        taskName.setText(_task.get_name());
        dueDate .setText(_task.get_dueDate());

        task_id.setText(_task.get_id()+"");




        description.setText(_task.get_description());

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _activeSwitch.setChecked(false);
                _activeSwitch.setText("COMPLETED!");
                Calendar calendar = Calendar.getInstance();
                String am = "PM";
                if (calendar.get(Calendar.AM_PM) == 0) {
                    am = "AM";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                completedDate.setText(   calendar.get(Calendar.MONTH) + 1 + "/"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        calendar.get(Calendar.YEAR) + " "
                        + sdf.format(calendar.getTime()) + " "
                + am);

                notify.cancelTime(getApplicationContext(), _task.get_id());


                completed.setVisibility(View.INVISIBLE);
            }
        });












        if(_task.get_status().equals("Active")) {
            completedDate.setText("INCOMPLETE");
            _activeSwitch.setChecked(true);
        } else {
            _activeSwitch.setChecked(false);
            _activeSwitch.setText("Inactive");
        }


        _activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    notify.cancelTime(getApplicationContext(),_task.get_id());
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Task Reminder is Canceled.",
                            Toast.LENGTH_LONG);
                    _activeSwitch.setText("Inactive");

                } else {
                    Calendar calendar = Calendar.getInstance();


                    String date = _task.get_dueDate();
                    String time = _task.get_time();
                    int repeatTime = _task.get_repeatingMinutes();

                    String[] date1 = date.split("/");
                    String[]  time1 = time.split(":");


                    int month = Integer.parseInt(date1[0]);
                    int day = Integer.parseInt(date1[1]);
                    int year = Integer.parseInt(date1[2]);

                    int hour = Integer.parseInt(time1[0]);
                    int minute = Integer.parseInt(time1[1]);
                    String am = time1[2];

                    calendar.set(year,month,day,hour,minute);
                    if (am.equals("AM")) {
                        calendar.set( Calendar.AM_PM, Calendar.AM );

                    } else  {
                        calendar.set( Calendar.AM_PM, Calendar.PM );

                    }

                    notify.setTime(getApplicationContext(),calendar, _task.get_id());
                }
            }
        });















        home = (Button) findViewById(R.id.home_task);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(TaskActivity.this, RecycleViewActivity.class);
                startActivity(home);
                finish();
            }
        });


    }







}
