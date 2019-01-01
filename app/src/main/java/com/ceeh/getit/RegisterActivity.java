package com.ceeh.getit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ceeh.getit.Model.TaskDatabase;
import com.ceeh.getit.Model.User;

import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText _firstName;
    private EditText _password;
    private TextView _today;
    private TaskDatabase dbHandler;
    private Button _register;
    private TextView _id;
    private Button _home;
    private TaskDatabase taskHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        dbHandler = new TaskDatabase(this);


        _firstName = (EditText) findViewById(R.id. firstName_reg);
        _password = (EditText) findViewById(R.id.password_reg);
        _today = (TextView) findViewById(R.id.date_reg);
        _register = (Button) findViewById(R.id.register_reg);
        _id = (TextView) findViewById(R.id.idView);
        _home = (Button) findViewById(R.id.homeButton);

        _id.setText(String.valueOf(dbHandler.getUserCount() + 1));

        Calendar calendar = Calendar.getInstance();
        _today.setText(calendar.get(Calendar.MONTH) + 1 + "/"
                + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
        calendar.get(Calendar.YEAR));




        _register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "You're registered! ";
                int check = checkCase();
                if (check != 1) {

                    if (check == 0) {
                        msg = "Invalid Name";
                    } else if (check == -1) {
                        msg = "Invalid Password";
                    }  else if (check == -2) {
                        msg = "Password length should be less than six.";
                    } else if (check == 10) {
                        msg = "Unavailable username! Try Again!";
                    }


                } else {
                    //register User
                    dbHandler.addUser(new User( dbHandler.getUserCount() + 1, _firstName.getText().toString(), _password.getText().toString()));

                    Intent register = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(register);
                    finish();
                }

                Toast toast = Toast.makeText(getApplicationContext(),
                        msg,
                        Toast.LENGTH_LONG);

                toast.show();




            }
        });


        _home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });

    }



    //check String case
    private int checkCase(){
        int a = 0;
        List<String> _listnames = dbHandler.getAllNames();
        for (String name: _listnames) {
            if (name.equals(_firstName.getText().toString())) {
                return 10;
            }
        }
        if (_firstName.getText().toString() != null && _firstName.getText().toString().length() > 0 && _firstName.getText().toString() != null) {
            a = -1;
            if (_password.getText().toString() != null && _password.getText().toString().length() > 0 && _password.getText().toString() != null) {
                a = -2;
                if (_password.getText().toString().length()<6){
                    a = 1;
                }

            }
        }
        return a;

    }


}
