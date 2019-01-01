package com.ceeh.getit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ceeh.getit.Model.Task;
import com.ceeh.getit.Model.TaskDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BootReceiver  extends BroadcastReceiver{

    private TaskDatabase taskHandler;




    private String time;
    private String date;
    private String am;
    private String status;
    private long id;

    private int month;
    private int day;
    private int year;
    private long repeatTime;
    private int hour;
    private int minute;






    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.

            taskHandler = new TaskDatabase(context);
            NotificationReceiver notify = new NotificationReceiver();

            Calendar calendar = Calendar.getInstance();
            NotificationReceiver receiver = new NotificationReceiver();


            List<Task> _taskList = taskHandler.getAllTask();

            for (Task _task : _taskList) {
                //assign all the variables,
                id =  _task.get_id();

                date = _task.get_dueDate();
                time = _task.get_time();
                status = _task.get_status();
                repeatTime = _task.get_repeatingMinutes();

                String[] date1 = date.split("/");
                String[]  time1 = time.split(":");


                month = Integer.parseInt(date1[0]);
                day = Integer.parseInt(date1[1]);
                year = Integer.parseInt(date1[2]);

                hour = Integer.parseInt(time1[0]);
                minute = Integer.parseInt(time1[1]);
                am = time1[2];



                calendar.set(year,month,day,hour,minute);
                if (am.equals("AM")) {
                    calendar.set( Calendar.AM_PM, Calendar.AM );

                } else  {
                    calendar.set( Calendar.AM_PM, Calendar.PM );

                }

                if(status.equals("Active")) {
                    notify.setTime(context,calendar, id);
                }


            }



        }
    }


}
