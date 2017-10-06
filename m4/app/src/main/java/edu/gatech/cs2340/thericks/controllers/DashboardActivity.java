package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.User;

/**
 * Created by mkcac on 10/6/2017.
 */

public class DashboardActivity extends AppCompatActivity{

    private Button mapButton;
    private Button listRatDataButton;
    private Button profileButton;
    private Button settingsButton;
    private Button reportRatButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mapButton = (Button) findViewById(R.id.map_button);
        listRatDataButton = (Button) findViewById(R.id.rat_data_list_button);
        profileButton = (Button) findViewById(R.id.profile_button);
        settingsButton = (Button) findViewById(R.id.settings_button);
        reportRatButton = (Button) findViewById(R.id.report_rat_button);
        logoutButton = (Button) findViewById(R.id.logout_button);

        Bundle b = getIntent().getExtras();
        User user = b.getParcelable("edu.gatech.cs2340.thericks.User");
        user.login();
        Log.d("LoggedIn", user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());

        logoutButton.setOnClickListener(v -> {
            user.logout();
            Log.d("LoggedIn", "Logout button pressed");
            Log.d("LoggedIn", user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());
            Context context = v.getContext();
            Intent intent = new Intent(context, WelcomeActivity.class);
            context.startActivity(intent);
            finish();
        });

        listRatDataButton.setOnClickListener((View v) -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, RatDataListActivity.class);
        });
    }
}
