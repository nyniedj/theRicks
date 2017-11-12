package edu.gatech.cs2340.thericks.controllers;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;
import edu.gatech.cs2340.thericks.models.RatDateTime;

/**
 * Created by Cameron on 10/6/2017.
 * Displays the data in a passed RatData through
 */
public class RatEntryActivity extends AppCompatActivity {

    private static final String TAG = RatEntryActivity.class.getSimpleName();

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

        Button saveButton = (Button) findViewById(R.id.rat_data_save_entry_button);
        Button cancelButton = (Button) findViewById(R.id.rat_data_cancel_entry_button);

        Bundle b = getIntent().getExtras();
        int index;
        if (b != null) {
            RatData ratData = b.getParcelable("edu.gatech.cs2340.thericks.RatData");
            index = b.getInt("INDEX");

            key.setText(ratData.getKey() + "");
            date.setText(ratData.getCreatedDateTime().toString());
            locationType.setText(ratData.getLocationType());
            address.setText(ratData.getIncidentAddress());
            zip.setText(ratData.getIncidentZip() + "");
            borough.setText(ratData.getBorough());
            city.setText(ratData.getCity());
            latitude.setText(ratData.getLatitude() + "");
            longitude.setText(ratData.getLongitude() + "");
        } else {
            index = -1;
        }

        key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                try {
                    Integer.parseInt(text);
                    key.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                } catch (NumberFormatException e) {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    key.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
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
                if (RatDateTime.isDateTime(text)) {
                    date.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    date.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
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
                try {
                    Integer.parseInt(text);
                    zip.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                } catch (NumberFormatException e) {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    zip.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
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
                try {
                    Double.parseDouble(text);
                    latitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                } catch (NumberFormatException e) {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    latitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
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
                try {
                    Double.parseDouble(text);
                    longitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                } catch (NumberFormatException e) {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    longitude.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
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
            int iKey = 0;
            int iZip = 0;
            double dLatitude = 0;
            double dLongitude = 0;

            Log.d(TAG, "Confirming rat data is valid");
            try {
                iKey = Integer.parseInt(key.getText().toString());
            } catch (NumberFormatException e) {
                Log.d(TAG, "Improperly formatted input detected in the key");
                return;
            }
            if (!RatDateTime.isDateTime(date.getText().toString())) {
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
            finish();

//            Log.d(TAG, "Valid rat data entered, passing new RatData to parent activity");
//            Intent intent = new Intent();
//            intent.putExtra("edu.gatech.cs2340.thericks.RatData", new RatData(iKey,
//                    date.getText().toString(),
//                    locationType.getText().toString(),
//                    iZip,
//                    address.getText().toString(),
//                    city.getText().toString(),
//                    borough.getText().toString(),
//                    dLatitude,
//                    dLongitude));
//            intent.putExtra("INDEX", index);
//            setResult(RESULT_OK, intent);
//            finish();
        });
    }
}
