package edu.gatech.cs2340.thericks.controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
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

    private Button saveButton;

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

        saveButton = (Button) findViewById(R.id.rat_data_save_entry_button);

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

        key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                try {
                    Integer.parseInt(text);
                    key.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                } catch (NumberFormatException e) {
                    key.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });

        zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                try {
                    Integer.parseInt(text);
                    zip.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                } catch (NumberFormatException e) {
                    zip.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });

        latitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                try {
                    Double.parseDouble(text);
                    latitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                } catch (NumberFormatException e) {
                    latitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });

        longitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                try {
                    Double.parseDouble(text);
                    longitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                } catch (NumberFormatException e) {
                    longitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });

        saveButton.setOnClickListener((View v) -> {
            int iKey = 0;
            int iZip = 0;
            double dLatitude = 0;
            double dLongitude = 0;

            try {
                iKey = Integer.parseInt(key.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            try {
                iZip = Integer.parseInt(zip.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            try {
                dLatitude = Double.parseDouble(latitude.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            try {
                dLongitude = Double.parseDouble(longitude.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }

            RatDatabase database = new RatDatabase(v.getContext());

            database.createRatData(iKey,
                    date.getText().toString(),
                    locationType.getText().toString(),
                    iZip,
                    address.getText().toString(),
                    borough.getText().toString(),
                    city.getText().toString(),
                    dLatitude,
                    dLongitude);
        });
    }
}
