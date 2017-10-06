package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserTable;
import edu.gatech.cs2340.thericks.utils.Security;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.login_username_entry);
        password = (EditText) findViewById(R.id.login_password_entry);
        login    = (Button)   findViewById(R.id.login_button);
        error    = (TextView) findViewById(R.id.incorrect_login_label);

        error.setVisibility(View.GONE);

        login.setOnClickListener((View v) -> {
                Log.d("Login", "Attempt to log into account");

                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                UserTable users = UserTable.getInstance();
                User u = users.getUserByUsername(enteredUsername);
                if (u != null && u.getLogin() != null) {
                    if (Security.checkPassword(enteredPassword, u.getLogin())) {
                        Log.d("Login", "Successfully logged into user account: " + u.getLogin().getUsername());
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DashboardActivity.class);
                        intent.putExtra("edu.gatech.cs2340.thericks.User", u);
                        context.startActivity(intent);
                    } else {
                        Log.d("Login", "Incorrect password");
                        error.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("Login", "Incorrect username");
                    error.setVisibility(View.VISIBLE);
                }
            }
        );
    }
}
