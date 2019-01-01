package com.ceeh.getit;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ceeh.getit.Model.Task;
import com.ceeh.getit.Model.TaskDatabase;
import com.ceeh.getit.Model.User;

import java.util.Calendar;



public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TaskDatabase taskHandler;
    private static final String TAG = "getMessenge";


    private EditText  _taskName;
    private EditText _detail;
    private TextView _id;
    private TextView _date;
    private Spinner am_pm;

    private Button _add;


    private String taskName;
    private String detail;
    private String time;
    private String date;
    private String am;




    private static int next_Id  = 1;

    //Time components
    private EditText _month;
    private EditText _day;
    private EditText _year;

    private EditText _hour;
    private EditText _minute;
    private EditText _repeatTime;

    private int month;
    private int day;
    private int year;
    private int repeatTime;


    private int hour;
    private int minute;

    private FloatingActionButton close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        _month = (EditText) findViewById(R.id.month_add);
        _day = (EditText) findViewById(R.id.day_add);
        _year = (EditText) findViewById(R.id.year_add);

        _hour = (EditText) findViewById(R.id.hour_add);
        _minute = (EditText) findViewById(R.id.minute_add);
        _repeatTime = (EditText) findViewById(R.id.repeat_add);

        am_pm = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.am_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        am_pm.setAdapter(adapter);

        final Intent active = new Intent();
        final int activeValue = active.getIntExtra("INACTIVE", 1);



        _taskName = (EditText) findViewById(R.id.name_add);
        _detail =   (EditText) findViewById(R.id.detail_add);
        _id = (TextView) findViewById(R.id.id_add);
        _add = (Button) findViewById(R.id.add_button);



        close = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  home = new Intent(AddTaskActivity.this, RecycleViewActivity.class);
                startActivity(home);
                finish();
            }
        });




       taskHandler = new TaskDatabase(this);








        _add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am = am_pm.getSelectedItem().toString();

                final int check = check();
                final int checkDay =  checkDayValues();
                final int checkHour = checkHours();

                if (check == -1 && checkDay == 5 && checkHour == 1) {





                    taskName = _taskName.getText().toString();
                    detail = _detail.getText().toString();

                    Calendar cal = Calendar.getInstance();
                    Calendar now1 = Calendar.getInstance();


                    if (am.equals("PM")) {
                        hour = hour + 12;
                    }
                    cal.set(year,month - 1, day, hour % 24, minute, 0);
                    long now2 = now1.getTimeInMillis();
                    long cal2 = cal.getTimeInMillis();
                    if (now2 <= cal2) {


                        Log.v(TAG, cal.getTime() + " " + am);

                        date = monthChecker() + "/" + dayChecker() + "/" + year;
                        time = hourChecker() + ":" + minutesChecker() + ":" + am;

                        Log.v(TAG, date);
                        Log.v(TAG, time);


                        if (_repeatTime.getText().toString().length() < 1) {
                            repeatTime = 0;
                        } else {
                            repeatTime = Integer.parseInt(_repeatTime.getText().toString());
                        }

                        String active_msg = "Active";
                        if (activeValue == 0) {
                            active_msg = "InactiveBySwitch";
                        }


                        Task new_task = new Task(
                                _taskName.getText().toString(),
                                active_msg,
                                detail,
                                date,
                                time,
                                repeatTime

                        );


                        long tskId = taskHandler.addTask(new_task);

                        if (active_msg.equals("Active")) {
                            new NotificationReceiver().setTime(getApplicationContext(), cal,
                                    tskId);
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    cal.getTime() + "",
                                    Toast.LENGTH_LONG);

                            toast.show();
                            Log.v(TAG, cal.getTime() + " " + "Here");
                        }


                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Task Added!",
                                Toast.LENGTH_SHORT);

                        toast.show();


                        Intent home = new Intent(AddTaskActivity.this, RecycleViewActivity.class);
                        startActivity(home);
                        finish();

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Invalid Date or Time",
                                Toast.LENGTH_LONG);

                        toast.show();
                    }
                } else  {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Invalid Date or Time",
                            Toast.LENGTH_LONG);

                    toast.show();
                }










            }
        });

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
       am = arg0.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        am = arg0.getItemAtPosition(0).toString();
    }



    private String hourChecker() {
        String num = _hour.getText().toString();
        if (Integer.parseInt(num) < 10 && num.length() < 2) {
            num = "0" + num;
        }

        return num;

    }
    //codes can be simplified


    private String dayChecker() {
        String num = "";
        if (Integer.parseInt(_day.getText().toString()) < 10 && _day.getText().toString().length() < 2) {
            num = "0" + Integer.parseInt(_day.getText().toString());
        } else {
            num = _day.getText().toString();
        }

        return num;

    }
    //codes can be simplified

    private String minutesChecker() {
        String num = "";
        Log.v(TAG,_minute.getText().toString());
        if (Integer.parseInt(_minute.getText().toString()) < 10  && _minute.getText().toString().length() < 2) {
            num = "0" + Integer.parseInt(_minute.getText().toString());
        }  else {
            num = _minute.getText().toString();
        }

        return num;

    }

    //codes can be simplified
    private String monthChecker() {
        String num = "";
        if (Integer.parseInt(_month.getText().toString()) < 10  && _month.getText().toString().length() < 2) {
            num = "0" + Integer.parseInt(_month.getText().toString());
        }  else {
            num = _month.getText().toString();
        }

        return num;

    }




    private int checkDayValues() {
        Calendar now = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        int a = 0;

        try {

            year = Integer.parseInt(_year.getText().toString());
            month = Integer.parseInt(_month.getText().toString());
            day = Integer.parseInt(_day.getText().toString());

            if (month == 0 || day == 0) {
                return 0;
            }

            if (year > now.get(Calendar.YEAR)) {
                a = 1;

                    if (month <= now.getActualMaximum(Calendar.MONTH) + 1) {
                        a = 3;

                        if (month == 2) {
                            if ((year - 2000) % 4 != 0 ) {
                                if (day < 29) {
                                    return 5;
                                } else {
                                    return 0;
                                }
                            } else {
                                if (day <= 29) {
                                    return 5;
                                }
                            }
                        }

                        later.set(year,month + 1,1);



                        if (day <= later.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                                a = 5;
                        }


                    }

            } else if (year == now.get(Calendar.YEAR)) {
                a = 1;
                if (month == now.get(Calendar.MONTH) + 1) {
                    a = 2;

                        if (day >= now.get(Calendar.DAY_OF_MONTH)) {
                            a = 5;


                        }

                } else if (month > now.get(Calendar.MONTH) + 1) {
                    a = 2;
                    later.set(year,month + 1,1);

                    if (day >= later.getActualMaximum(Calendar.DAY_OF_MONTH)){
                        a = 5;


                    }
                }
            }

        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Invalid Date Format.",
                    Toast.LENGTH_LONG);

            toast.show();
        }

        return a;
    }


    private int checkHours() {

        Calendar calendar = Calendar.getInstance();
        int a = 2;
        try {

             hour = Integer.parseInt(_hour.getText().toString());
             minute = Integer.parseInt(_minute.getText().toString());

            if (hour < 0 && hour > 12) {
                return 0;

            }

            if (hour == 12) {
                hour = 0;
            }


            if (minute < 0 && minute > 59) {
                return 0;

            }

            if (Integer.parseInt(_minute.getText().toString()) < 0 &&
                    Integer.parseInt(_minute.getText().toString()) > calendar.getActualMaximum((Calendar.MINUTE))) {
                return 0;
            }


            if (Integer.parseInt(_year.getText().toString()) == calendar.get(Calendar.YEAR)) {
                if (Integer.parseInt(_month.getText().toString()) == calendar.get(Calendar.MONTH) + 1) {
                    if (Integer.parseInt(_day.getText().toString()) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        if (am.toString().equals("AM") && calendar.get(Calendar.AM_PM) == 1 ||
                                am.toString().equals("PM") && calendar.get(Calendar.AM_PM) == 0
                                ) {


                            if (hour < calendar.get(Calendar.HOUR)) {
                                 a = 0;

                            }

                            if (hour == calendar.get(Calendar.HOUR)) {
                                if (Integer.parseInt(_minute.getText().toString()) < calendar.get((Calendar.MINUTE))) {
                                    a = 0;
                                }

                            }

                        }
                    }
                }
            }

            a = 1;
        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Invalid Time Format.",
                    Toast.LENGTH_LONG);

            toast.show();
        }

        return a;
    }


    private int check() {
          int i = 0;
          if (  _taskName != null && _taskName.getText().length() > 0 ) {
              i = -1;

          }
          return i;

    }












}
