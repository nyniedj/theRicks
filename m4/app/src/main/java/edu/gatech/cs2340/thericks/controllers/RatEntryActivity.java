package edu.gatech.cs2340.thericks.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by mkcac on 10/6/2017.
 */

public class RatEntryActivity extends AppCompatActivity {

    private RatData ratData;
    private EditText dateCreated;
    private EditText borough;
    private EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_entry);

        dateCreated = (EditText) findViewById(R.id.dateCreatedTextField);
        borough = (EditText) findViewById(R.id.boroughTextField);
        city = (EditText) findViewById(R.id.cityTextField);

        Bundle b = getIntent().getExtras();
        ratData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");

        dateCreated.setText(ratData.getCreatedDateTime());
        borough.setText(ratData.getBorough());
        city.setText(ratData.getCity());
    }
}
