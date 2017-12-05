package edu.gatech.cs2340.thericks.controllers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;
import edu.gatech.cs2340.thericks.utils.DateUtility;

/**
 * Created by Cameron on 10/6/2017.
 * Displays the data in a passed RatData through
 */
public class RatEntryActivity extends AppCompatActivity {

    private static final String TAG = RatEntryActivity.class.getSimpleName();

    private static final int LOCATION_SERVICES_REQUEST = 999;

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

        key = findViewById(R.id.rat_data_key_entry);
        date = findViewById(R.id.rat_data_date_entry);
        locationType = findViewById(R.id.rat_data_location_type_entry);
        address = findViewById(R.id.rat_data_incident_address_entry);
        zip = findViewById(R.id.rat_data_incident_zip_entry);
        borough = findViewById(R.id.rat_data_borough_entry);
        city = findViewById(R.id.rat_data_city_entry);
        latitude = findViewById(R.id.rat_data_latitude_entry);
        longitude = findViewById(R.id.rat_data_longitude_entry);

        Button saveButton = findViewById(R.id.rat_data_save_entry_button);
        Button cancelButton = findViewById(R.id.rat_data_cancel_entry_button);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            RatData ratData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");
            if (ratData != null) {
                key.setText(String.valueOf(ratData.getKey()));
                date.setText(ratData.getCreatedDateTime());
                locationType.setText(ratData.getLocationType());
                address.setText(ratData.getIncidentAddress());
                zip.setText(String.valueOf(ratData.getIncidentZip()));
                borough.setText(ratData.getBorough());
                city.setText(ratData.getCity());
                latitude.setText(String.format(Locale.ENGLISH, "%8f", ratData.getLatitude()));
                longitude.setText(String.format(Locale.ENGLISH, "%8f", ratData.getLongitude()));
            }
        } else {
            Log.d(TAG, "No rat data passed in, passing in current defaults");
            date.setText(DateUtility.DATE_TIME_FORMAT.format(Calendar.getInstance().getTime()));
            populateCurrentLocation();
        }

        key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                Scanner intTest = new Scanner(text);
                if (intTest.hasNextInt()) {
                    key.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack,
                            null));
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    key.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary,
                            null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (DateUtility.parse(text) != null) {
                    date.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack,
                            null));
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    date.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary,
                            null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                Scanner intTest = new Scanner(text);
                if (intTest.hasNextInt()) {
                    zip.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack,
                            null));
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    zip.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary,
                            null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        latitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                Scanner doubleTest = new Scanner(text);
                if (doubleTest.hasNextDouble()) {
                    latitude.setTextColor(ResourcesCompat.getColor(getResources(),
                            R.color.colorBlack, null));
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    latitude.setTextColor(ResourcesCompat.getColor(getResources(),
                            R.color.errorPrimary, null));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        longitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                Scanner doubleTest = new Scanner(text);
                if (doubleTest.hasNextDouble()){
                    longitude.setTextColor(ResourcesCompat.getColor(getResources(),
                            R.color.colorBlack, null));
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    longitude.setTextColor(ResourcesCompat.getColor(getResources(),
                            R.color.errorPrimary, null));
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        cancelButton.setOnClickListener((View v) -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        saveButton.setOnClickListener((View v) -> {
            int iKey;
            int iZip;
            double dLatitude;
            double dLongitude;

            Log.d(TAG, "Confirming rat data is valid");
            try {
                iKey = Integer.parseInt(key.getText().toString());
            } catch (NumberFormatException e) {
                Log.d(TAG, "Improperly formatted input detected in the key");
                return;
            }
            if (DateUtility.parse(date.getText().toString()) == null) {
                Log.d(TAG, "Improperly formatted input detected in the date time");
                return;
            }
            try {
                iZip = Integer.parseInt(zip.getText().toString());
            } catch (NumberFormatException e) {
                Log.d(TAG, "Improperly formatted input detected in the zip");
                return;
            }
            try {
                dLatitude = Double.parseDouble(latitude.getText().toString());
            } catch (NumberFormatException e) {
                Log.d(TAG, "Improperly formatted input detected in the latitude");
                return;
            }
            try {
                dLongitude = Double.parseDouble(longitude.getText().toString());
            } catch (NumberFormatException e) {
                Log.d(TAG, "Improperly formatted input detected in the longitude");
                e.printStackTrace();
                return;
            }

            Log.d(TAG, "Valid rat data entered, passing rat meta data to the database");
            RatDataSource database = new RatDatabase();

            database.createRatData(iKey,
                    date.getText().toString(),
                    locationType.getText().toString(),
                    iZip,
                    address.getText().toString(),
                    city.getText().toString(),
                    borough.getText().toString(),
                    dLatitude,
                    dLongitude);
            setResult(RESULT_OK);
            finish();
        });
    }

    private void populateCurrentLocation() {
        Log.d(TAG, "Attempting to fetch a location");
        LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_SERVICES_REQUEST);
            } else {
                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(TAG, "Populating location");
                        latitude.setText(String.format(Locale.ENGLISH, "%8f",
                                location.getLatitude()));
                        longitude.setText(String.format(Locale.ENGLISH, "%8f",
                                location.getLongitude()));
                        //locMan.removeUpdates(this);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                        Log.d(TAG, "Status changed: " + s);
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        Log.d(TAG, "Provider enabled: " + s);
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        Log.d(TAG, "Provider disabled: " + s);
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "Permissions request result recieved");
        if (requestCode == LOCATION_SERVICES_REQUEST) {
            for (int p: grantResults) {
                if (p == PackageManager.PERMISSION_DENIED) {
                    Log.d(TAG, "A location permission request was denied");
                    return;
                }
            }
            populateCurrentLocation();
        }
    }
}
