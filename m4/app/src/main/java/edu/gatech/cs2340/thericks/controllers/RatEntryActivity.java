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
    private TextView key;
    private TextView date;
    private TextView locationType;
    private TextView address;
    private TextView zip;
    private TextView borough;
    private TextView city;
    private TextView latitude;
    private TextView longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_entry);

        key = (TextView) findViewById(R.id.rat_data_key_text);
        date = (TextView) findViewById(R.id.rat_data_date_text);
        locationType = (TextView) findViewById(R.id.rat_data_location_type_text);
        address = (TextView) findViewById(R.id.rat_data_incident_address_text);
        zip = (TextView) findViewById(R.id.rat_data_incident_zip_text);
        borough = (TextView) findViewById(R.id.rat_data_borough_text);
        city = (TextView) findViewById(R.id.rat_data_city_text);
        latitude = (TextView) findViewById(R.id.rat_data_latitude_text);
        longitude = (TextView) findViewById(R.id.rat_data_longitude_text);

        Bundle b = getIntent().getExtras();
        ratData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");

        key.setText("Key: " + ratData.getKey());
        date.setText("Created Date and Time: " + ratData.getCreatedDateTime());
        locationType.setText("Location type: " + ratData.getLocationType());
        address.setText("Incident Address: " + ratData.getIncidentAddress());
        zip.setText("Incident ZIP: " + ratData.getIncidentZip());
        borough.setText("Borough: " + ratData.getBorough());
        city.setText("City: " + ratData.getCity());
        latitude.setText("Latitude: " + ratData.getLatitude());
        longitude.setText("Longitude: " +ratData.getLongitude());
    }
}
