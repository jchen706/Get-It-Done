package com.ceeh.getit;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ceeh.getit.Model.Task;
import com.ceeh.getit.Model.TaskDatabase;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

     private AlarmManager _alarmManager;
     private PendingIntent _pendingIntent;
    private static final String TAG = "getMessenge";


    @Override
    public void onReceive(Context context, Intent intent) {


        long taskId = intent.getLongExtra("TASK_ID", 1);

        TaskDatabase dbHandler = new TaskDatabase(context);

        Task _task = dbHandler.getTaskById(taskId); //get Task from the sqllite database



        Intent _taskOpen = new Intent(context, TaskActivity.class); //create intent for the notification click

        _taskOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //flag
        _taskOpen.putExtra("TASK_ID", taskId);

        PendingIntent _click = PendingIntent.getActivity(context, (int)taskId , _taskOpen, PendingIntent.FLAG_UPDATE_CURRENT);
        //cancel the previous , update a new one

        String CHANNEL_ID = "Reminder"; //id for new Oreo version

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder";
            String description = "Task Reminder";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }





        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(_task.get_name())
                .setContentText(_task.get_description())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Set the intent that will fire when the user taps the notification
                .setContentIntent(_click)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOnlyAlertOnce(true);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);





        nManager.notify((int)taskId, mBuilder.build());





    }


    public void setTime(Context context, Calendar calendar , long _taskId ) {
             _alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

             Calendar now = Calendar.getInstance();

             long timedifference = calendar.getTimeInMillis() - now.getTimeInMillis();


             Intent set = new Intent(context, NotificationReceiver.class);
             set.putExtra("TASK_ID", _taskId);
              _pendingIntent = PendingIntent.getBroadcast(context, (int)_taskId, set, PendingIntent.FLAG_CANCEL_CURRENT);


        _alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + timedifference,
                _pendingIntent);

        Toast toast1 = Toast.makeText(context,
                timedifference + " " + "fire",
                Toast.LENGTH_LONG);

        toast1.show();
        //device reboot


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


    }


    public void repeatTime(Context context, Calendar calendar, long _taskId, long _time) {
        _alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar now = Calendar.getInstance();

        long timedifference = calendar.getTimeInMillis() - now.getTimeInMillis();

        Intent set = new Intent(context, NotificationReceiver.class);
        set.putExtra("TASK_ID", _taskId);
        _pendingIntent = PendingIntent.getBroadcast(context, (int)_taskId, set, PendingIntent.FLAG_CANCEL_CURRENT);


        _alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + timedifference, _time,
                _pendingIntent);

        //reboot



        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);




    }


    public void cancelTime(Context context, long _taskId) {
        _alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // If the alarm has been set, cancel it.
        _pendingIntent = PendingIntent.getBroadcast(context,(int)_taskId, new Intent(context, NotificationReceiver.class), 0);
        _alarmManager.cancel(_pendingIntent);


        //reboot when device restart


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);


    }







}