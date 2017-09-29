package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;

import edu.gatech.cs2340.thericks.R;

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

        logout.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, WelcomeActivity.class);
            context.startActivity(intent);
            finish();
        });
    }
}
