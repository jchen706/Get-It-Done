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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "getMessenge";

    private Button _login;
    private EditText _password;
    private Button _signUp;
    private TaskDatabase dbHandler;
    private TextView _number;
    private TextView _name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Log.i(TAG, "oncreate");

        dbHandler = new TaskDatabase(this);

        _login = (Button) findViewById(R.id.login_main);
        _signUp = (Button) findViewById(R.id.signUp_main);
        _password = (EditText) findViewById(R.id.numberPassword_main);
        _name = (TextView) findViewById(R.id.name_main);

        if (dbHandler.getAllNames().size() > 0) {
           _signUp.setVisibility(View.GONE);
            _name.setText(dbHandler.getUserById(1).getFirstName() + "!");
        }



        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> users = dbHandler.getAllUser();


                boolean logged = false;
                for (User s: users) {

                       if( _password.getText().toString().equals(s.getPassword())){
                           Toast toast = Toast.makeText(getApplicationContext(),
                                   "Logging In",
                                   Toast.LENGTH_LONG);
                           logged = true;

                           toast.show();

                           Intent today = new Intent(MainActivity.this, RecycleViewActivity.class);
                           startActivity(today);
                           finish();
                       }




                }

                 if (logged == false) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Wrong Username or Password. Try Again!",
                                Toast.LENGTH_LONG);

                        toast.show();
                }


            }
        });


       _signUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent register = new Intent(MainActivity.this, RegisterActivity.class);
               startActivity(register);
               finish();
           }
       });


    }

}
