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
    private EditText key;
    private EditText date;
    private EditText locationType;
    private EditText address;
    private EditText zip;
    private EditText borough;
    private EditText city;
    private EditText latitude;
    private EditText longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_entry);

        key = (EditText) findViewById(R.id.rat_data_key_entry);
        date = (EditText) findViewById(R.id.rat_data_date_entry);
        locationType = (EditText) findViewById(R.id.rat_data_location_type_entry);
        address = (EditText) findViewById(R.id.rat_data_incident_address_entry);
        zip = (EditText) findViewById(R.id.rat_data_incident_zip_entry);
        borough = (EditText) findViewById(R.id.rat_data_borough_entry);
        city = (EditText) findViewById(R.id.rat_data_city_entry);
        latitude = (EditText) findViewById(R.id.rat_data_latitude_entry);
        longitude = (EditText) findViewById(R.id.rat_data_longitude_entry);

        Bundle b = getIntent().getExtras();
        ratData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");

        key.setText(ratData.getKey());
        date.setText(ratData.getCreatedDateTime());
        locationType.setText(ratData.getLocationType());
        address.setText(ratData.getIncidentAddress());
        zip.setText(ratData.getIncidentZip());
        borough.setText(ratData.getBorough());
        city.setText(ratData.getCity());
        latitude.setText(ratData.getLatitude() + "");
        longitude.setText(ratData.getLongitude() + "");
    }
}
