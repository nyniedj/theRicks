package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.UserTable;

/**
 * Created by Ben Lashley on 9/20/2017.
 */

public class WelcomeActivity extends AppCompatActivity {

    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        UserTable.getInstance().loadDummyData();

        login =    (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.register_button);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
