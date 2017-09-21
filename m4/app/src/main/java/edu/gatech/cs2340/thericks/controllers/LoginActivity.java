package edu.gatech.cs2340.thericks.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserTable;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.login_username_entry);
        password = (EditText) findViewById(R.id.login_password_entry);
        login    = (Button)   findViewById(R.id.login_button);

        login.setOnClickListener((View v) -> {
                Log.d("Login", "Attempt to log into account");

                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                UserTable users = UserTable.getInstance();
                User u = users.getUserByUsername(enteredUsername);
                if (u != null && u.getLogin() != null) {
                    if (u.getLogin().checkPassword(enteredPassword)) {
                        Log.d("Login", "Successfully logged into user account: " + u.getLogin().getUsername());
                        finish();
                    } else {
                        Log.d("Login", "Incorrect password");
                    }
                } else {
                    Log.d("Login", "Incorrect username");
                }
            }
        );
    }
}
