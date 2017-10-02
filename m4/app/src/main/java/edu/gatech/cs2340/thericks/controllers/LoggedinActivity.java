package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.User;

/**
 * Created by sun on 9/23/17.
 */

public class LoggedinActivity extends AppCompatActivity {

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
        logout = (Button) findViewById(R.id.logout);

        Bundle b = getIntent().getExtras();
        User user = b.getParcelable("edu.gatech.cs2340.thericks.User");
        user.login();
        Log.d("LoggedIn", user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());

        logout.setOnClickListener(v -> {
            user.logout();
            Log.d("LoggedIn", "Logout button pressed");
            Log.d("LoggedIn", user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());
            Context context = v.getContext();
            Intent intent = new Intent(context, WelcomeActivity.class);
            context.startActivity(intent);
            finish();
        });
    }
}
