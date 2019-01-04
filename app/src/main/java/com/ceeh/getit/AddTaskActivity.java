package com.ceeh.getit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
    private TextView _due_date;


    private TextView _due_time;


    private int _month;
    private int _day;
    private int _year;
    private int repeatTime = 0;

    private DatePickerDialog picker;
    private TimePickerDialog timePiker;


    private int _hour;
    private int _minute;

    private FloatingActionButton close;
    private Button _setDate;
    private Button _setTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


         _setTime = (Button) findViewById(R.id.set_Time_add);
        _setDate = (Button) findViewById(R.id.set_date_add);
        _due_date = (TextView) findViewById(R.id.due_date_add);
        _due_time = (TextView) findViewById(R.id.alert_time_add);


        _setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();

                final int day = now.get(Calendar.DAY_OF_MONTH);
                final int month = now.get(Calendar.MONTH);
                int year = now.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    _year = year;
                                    _month = monthOfYear + 1;
                                    _day = dayOfMonth;

                                _due_date.setText(_month + "/" + _day + "/" + _year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });



        _setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                timePiker = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        _hour = i;
                        _minute = i1;
                        if (_hour / 12 == 1) {
                            am = "PM";
                        } else {
                            am = "AM";
                        }

                        if (hour == 0) {
                            _hour = 12;
                        }

                        _due_time.setText(_hour + ":" +_minute + " " + am);

                    }
                }, hour, minute, false );
                timePiker.setTitle("Set Time");
                timePiker.show();

            }
        });


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

                final int check = check();

                if (check == -1) {





                    taskName = _taskName.getText().toString();
                    detail = _detail.getText().toString();

                    Calendar cal = Calendar.getInstance();
                    Calendar now1 = Calendar.getInstance();

                    if (am == "AM") {
                        if (_hour == 12) {
                            _hour = 0;
                        }
                    }




                    cal.set(_year,_month - 1, _day, _hour % 24, _minute, 0);
                    long now2 = now1.getTimeInMillis();
                    long cal2 = cal.getTimeInMillis();
                    if (now2 <= cal2) {


                        Log.v(TAG, cal.getTime() + " " + am);

                        date = monthChecker() + "/" + dayChecker() + "/" + _year;
                        time = hourChecker() + ":" + minutesChecker() + ":" + am;

                        Log.v(TAG, date);
                        Log.v(TAG, time);


//                        if (_repeatTime.getText().toString().length() < 1) {
//                            repeatTime = 0;
//                        } else {
//                            repeatTime = Integer.parseInt(_repeatTime.getText().toString());
//                        }

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
        Integer _hour1 = _hour % 12;
        String num = String.valueOf(_hour1);
        if (_hour < 10 && num.length() < 2) {
            num = "0" + num;
        }
        return num;
    }
    //codes can be simplified


    private String dayChecker() {
        String num = String.valueOf(_day);
        if (_day < 10) {
            num = "0" + num;
        }
        return num;
    }
    //codes can be simplified

    private String minutesChecker() {
        String num = String.valueOf(_minute);

        if (_minute < 10  && num.length() < 2) {
            num = "0" + num;
        }
        return num;

    }

    //codes can be simplified
    private String monthChecker() {
       String num = String.valueOf(_month);
        if (_month < 10  && num.length() < 2) {
            num = "0" + num;
        }
        return num;
    }

    private int check() {
          int i = 0;
          if (  _taskName != null && _taskName.getText().length() > 0 ) {
              i = -1;

          }
          return i;

    }












}
