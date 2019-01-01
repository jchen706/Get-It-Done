package com.ceeh.getit;

import android.content.Intent;
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

import java.util.Calendar;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Button home;
    private Switch notifySwitch;
    private TextView on;
    private TaskDatabase taskHandler;

    private List<Task> _taskList;
    private NotificationReceiver notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        on = (TextView) findViewById(R.id.alert_on_settings);

        taskHandler = new TaskDatabase(this);
        notify = new NotificationReceiver();

        notifySwitch = (Switch) findViewById(R.id.alert_switch_settings) ;

        _taskList = taskHandler.getAllTask();

        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    on.setText("Off");

                    for (Task c :_taskList) {

                        notify.cancelTime(getApplicationContext(), c.get_id());
                        c.set_status("InactiveBySwitch");
                    }



                    Toast toast = Toast.makeText(getApplicationContext(),
                            "All reminders are canceled.",
                            Toast.LENGTH_LONG);
                    toast.show();

                }  else {
                    on.setText("On");

                    for (Task _task :_taskList) {

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
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "All reminders are active..",
                            Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });



        home = (Button) findViewById(R.id.home_setting);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(SettingsActivity.this, RecycleViewActivity.class);
                if (notifySwitch.isChecked() == false) {
                    home.putExtra("INACTIVE", 0);

                }
                startActivity(home);
                finish();
            }
        });


    }
}
