package edu.gatech.cs2340.thericks.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.utils.DateUtility;

/**
 * Created by Cameron on 11/15/2017.
 * Activity to be started for result to obtain a single predicate to
 * filter with
 */
public class FilterActivity extends AppCompatActivity {

    private static final String TAG = FilterActivity.class.getSimpleName();

    public static final int GET_FILTER = 700;
    public static final String FILTER = "FILTER";

    private CheckedTextView dateAndTimeCheck;
    private CheckedTextView locationTypeCheck;
    private CheckedTextView zipCheck;
    private CheckedTextView addressCheck;
    private CheckedTextView cityCheck;
    private CheckedTextView boroughCheck;
    private CheckedTextView latitudeCheck;
    private CheckedTextView longitudeCheck;

    private Button date1Button;
    private Button date2Button;
    private Button time1Button;
    private Button time2Button;

    private EditText locationTypeEdit;
    private EditText zipEdit;
    private EditText addressEdit;
    private EditText cityEdit;
    private EditText boroughEdit;
    private EditText minLatitudeEdit;
    private EditText maxLatitudeEdit;
    private EditText minLongitudeEdit;
    private EditText maxLongitudeEdit;

    private TextView latSeparator;
    private TextView longSeparator;

    private RatFilter filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        date1Button = findViewById(R.id.date_button_filter_1);
        date2Button = findViewById(R.id.date_button_filter_2);
        time1Button = findViewById(R.id.time_button_filter_1);
        time2Button = findViewById(R.id.time_button_filter_2);

        locationTypeEdit = findViewById(R.id.location_type_filter_edit);
        zipEdit = findViewById(R.id.zip_filter_edit);
        addressEdit = findViewById(R.id.address_filter_edit);
        cityEdit = findViewById(R.id.city_filter_edit);
        boroughEdit = findViewById(R.id.borough_filter_edit);
        minLatitudeEdit = findViewById(R.id.min_latitude_filter_edit);
        maxLatitudeEdit = findViewById(R.id.max_latitude_filter_edit);
        minLongitudeEdit = findViewById(R.id.min_longitude_filter_edit);
        maxLongitudeEdit = findViewById(R.id.max_longitude_filter_edit);

        dateAndTimeCheck = findViewById(R.id.date_filter_check_text);
        locationTypeCheck = findViewById(R.id.location_type_filter_check_text);
        zipCheck = findViewById(R.id.zip_filter_check_text);
        addressCheck = findViewById(R.id.address_filter_check_text);
        cityCheck = findViewById(R.id.city_filter_check_text);
        boroughCheck = findViewById(R.id.borough_filter_check_text);
        latitudeCheck = findViewById(R.id.latitude_filter_check_text);
        longitudeCheck = findViewById(R.id.longitude_filter_check_text);

        latSeparator = findViewById(R.id.lat_separator_filter_text);
        longSeparator = findViewById(R.id.long_separator_filter_text);

        filter = getIntent().getParcelableExtra(FILTER);
        if (filter == null) {
            filter = RatFilter.getDefaultInstance();
        }

        if (filter.hasPredicate(RatFilter.DATE)) {
            date1Button.setText(filter.getBeginDateStr());
            date2Button.setText(filter.getEndDateStr());
            time1Button.setText(filter.getBeginTimeStr());
            time2Button.setText(filter.getEndTimeStr());
            if (filter.isPredicateEnabled(RatFilter.DATE)) {
                onDateAndTimeCheckClicked(dateAndTimeCheck);
            }
        }

        if (filter.hasPredicate(RatFilter.LOCATION_TYPE)) {
            locationTypeEdit.setText(filter.getLocationType());
            if (filter.isPredicateEnabled(RatFilter.LOCATION_TYPE)) {
                onLocationTypeCheckClicked(locationTypeCheck);
            }
        }

        if (filter.hasPredicate(RatFilter.ZIP)) {
            zipEdit.setText(String.format(Locale.ENGLISH, "%d", filter.getZip()));
            if (filter.isPredicateEnabled(RatFilter.ZIP)) {
                onZipCheckClicked(zipCheck);
            }
        }

        if (filter.hasPredicate(RatFilter.ADDRESS)) {
            addressEdit.setText(filter.getAddress());
            if (filter.isPredicateEnabled(RatFilter.ADDRESS)) {
                onAddressCheckClicked(addressCheck);
            }
        }

        if (filter.hasPredicate(RatFilter.CITY)) {
            cityEdit.setText(filter.getCity());
            if (filter.isPredicateEnabled(RatFilter.CITY)) {
                onCityCheckClicked(cityCheck);
            }
        }

        if (filter.hasPredicate(RatFilter.BOROUGH)) {
            boroughEdit.setText(filter.getBorough());
            if (filter.isPredicateEnabled(RatFilter.BOROUGH)) {
                onBoroughCheckClicked(boroughCheck);
            }
        }

        if (filter.hasPredicate(RatFilter.LATITUDE)) {
            minLatitudeEdit.setText(String.format(Locale.ENGLISH, "%f", filter.getMinLatitude()));
            maxLatitudeEdit.setText(String.format(Locale.ENGLISH, "%f", filter.getMaxLatitude()));
            if (filter.isPredicateEnabled(RatFilter.LATITUDE)) {
                onLatitudeCheckClicked(latitudeCheck);
            }
        }

        if (filter.hasPredicate(RatFilter.LONGITUDE)) {
            minLongitudeEdit.setText(String.format(Locale.ENGLISH, "%f", filter.getMinLongitude()));
            maxLongitudeEdit.setText(String.format(Locale.ENGLISH, "%f", filter.getMaxLongitude()));
            if (filter.isPredicateEnabled(RatFilter.LONGITUDE)) {
                onLongitudeCheckClicked(longitudeCheck);
            }
        }
    }

    public void onApplyButtonClicked(View v) {
        Calendar date1 = Calendar.getInstance();
        try {
            date1.setTime(DateUtility.DATE_FORMAT.parse(date1Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar time1 = Calendar.getInstance();
        try {
            time1.setTime(DateUtility.TIME_FORMAT.parse(time1Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date1.set(Calendar.HOUR, time1.get(Calendar.HOUR));
        date1.set(Calendar.MINUTE, time1.get(Calendar.MINUTE));
        Calendar date2 = Calendar.getInstance();
        try {
            date2.setTime(DateUtility.DATE_FORMAT.parse(date2Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar time2 = Calendar.getInstance();
        try {
            time2.setTime(DateUtility.TIME_FORMAT.parse(time2Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date2.set(Calendar.HOUR, time2.get(Calendar.HOUR));
        date2.set(Calendar.MINUTE, time2.get(Calendar.MINUTE));
        filter.setBeginDate(date1.getTime());
        filter.setEndDate(date2.getTime());
        filter.setPredicateEnabled(RatFilter.DATE, dateAndTimeCheck.isChecked());

        filter.setLocationType(locationTypeEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.LOCATION_TYPE, locationTypeCheck.isChecked());

        try {
            filter.setZip(Integer.parseInt(zipEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setZip(null);
        }
        filter.setPredicateEnabled(RatFilter.ZIP, zipCheck.isChecked());

        filter.setAddress(addressEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.ADDRESS, addressCheck.isChecked());

        filter.setCity(cityEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.CITY, cityCheck.isChecked());

        filter.setBorough(boroughEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.BOROUGH, boroughCheck.isChecked());

        try {
            filter.setMinLatitude(Double.parseDouble(minLatitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMinLatitude(null);
        }
        try {
            filter.setMaxLatitude(Double.parseDouble(maxLatitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMaxLatitude(null);
        }
        filter.setPredicateEnabled(RatFilter.LATITUDE, latitudeCheck.isChecked());

        try {
            filter.setMinLongitude(Double.parseDouble(minLongitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMinLongitude(null);
        }
        try {
            filter.setMaxLongitude(Double.parseDouble(maxLongitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMaxLongitude(null);
        }
        filter.setPredicateEnabled(RatFilter.LONGITUDE, longitudeCheck.isChecked());

        Intent intent = new Intent();
        intent.putExtra(FILTER, filter);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onCancelButtonClicked(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onClearButtonClicked(View v) {
        dateAndTimeCheck.setChecked(false);
        locationTypeCheck.setChecked(false);
        zipCheck.setChecked(false);
        addressCheck.setChecked(false);
        cityCheck.setChecked(false);
        boroughCheck.setChecked(false);
        latitudeCheck.setChecked(false);
        longitudeCheck.setChecked(false);
    }

    public void onDateAndTimeCheckClicked(View v) {
        if (dateAndTimeCheck.isChecked()) {
            dateAndTimeCheck.setChecked(false);
            date1Button.setVisibility(View.GONE);
            date2Button.setVisibility(View.GONE);
            time1Button.setVisibility(View.GONE);
            time2Button.setVisibility(View.GONE);
        } else {
            dateAndTimeCheck.setChecked(true);
            date1Button.setVisibility(View.VISIBLE);
            date2Button.setVisibility(View.VISIBLE);
            time1Button.setVisibility(View.VISIBLE);
            time2Button.setVisibility(View.VISIBLE);
        }
    }

    public void onLocationTypeCheckClicked(View v) {
        if (locationTypeCheck.isChecked()) {
            locationTypeCheck.setChecked(false);
            locationTypeEdit.setVisibility(View.GONE);
        } else {
            locationTypeCheck.setChecked(true);
            locationTypeEdit.setVisibility(View.VISIBLE);
        }
    }

    public void onZipCheckClicked(View v) {
        if (zipCheck.isChecked()) {
            zipCheck.setChecked(false);
            zipEdit.setVisibility(View.GONE);
        } else {
            zipCheck.setChecked(true);
            zipEdit.setVisibility(View.VISIBLE);
        }
    }

    public void onAddressCheckClicked(View v) {
        if (addressCheck.isChecked()) {
            addressCheck.setChecked(false);
            addressEdit.setVisibility(View.GONE);
        } else {
            addressCheck.setChecked(true);
            addressEdit.setVisibility(View.VISIBLE);
        }
    }

    public void onCityCheckClicked(View v) {
        if (cityCheck.isChecked()) {
            cityCheck.setChecked(false);
            cityEdit.setVisibility(View.GONE);
        } else {
            cityCheck.setChecked(true);
            cityEdit.setVisibility(View.VISIBLE);
        }
    }

    public void onBoroughCheckClicked(View v) {
        if (boroughCheck.isChecked()) {
            boroughCheck.setChecked(false);
            boroughEdit.setVisibility(View.GONE);
        } else {
            boroughCheck.setChecked(true);
            boroughEdit.setVisibility(View.VISIBLE);
        }
    }

    public void onLatitudeCheckClicked(View v) {
        if (latitudeCheck.isChecked()) {
            latitudeCheck.setChecked(false);
            minLatitudeEdit.setVisibility(View.GONE);
            maxLatitudeEdit.setVisibility(View.GONE);
            latSeparator.setVisibility(View.GONE);
        } else {
            latitudeCheck.setChecked(true);
            minLatitudeEdit.setVisibility(View.VISIBLE);
            maxLatitudeEdit.setVisibility(View.VISIBLE);
            latSeparator.setVisibility(View.VISIBLE);
        }
    }

    public void onLongitudeCheckClicked(View v) {
        if (longitudeCheck.isChecked()) {
            longitudeCheck.setChecked(false);
            minLongitudeEdit.setVisibility(View.GONE);
            maxLongitudeEdit.setVisibility(View.GONE);
            longSeparator.setVisibility(View.GONE);
        } else {
            longitudeCheck.setChecked(true);
            minLongitudeEdit.setVisibility(View.VISIBLE);
            maxLongitudeEdit.setVisibility(View.VISIBLE);
            longSeparator.setVisibility(View.VISIBLE);
        }
    }

    public void showDate1PickerDialog(View v) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(DateUtility.DATE_FORMAT.parse(date1Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FilterDatePickerFragment newFragment = new FilterDatePickerFragment();
        newFragment.setCallback(bundle -> {
            int year = bundle.getInt("YEAR");
            int month = bundle.getInt("MONTH");
            int day = bundle.getInt("DAY");
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.YEAR, year);
            cal1.set(Calendar.MONTH, month);
            cal1.set(Calendar.DAY_OF_MONTH, day);
            date1Button.setText(DateUtility.DATE_FORMAT.format(cal1.getTime()));
        });
        Bundle args = new Bundle();
        args.putInt("YEAR", cal.get(Calendar.YEAR));
        args.putInt("MONTH", cal.get(Calendar.MONTH));
        args.putInt("DAY", cal.get(Calendar.DAY_OF_MONTH));
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDate2PickerDialog(View v) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(DateUtility.DATE_FORMAT.parse(date2Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FilterDatePickerFragment newFragment = new FilterDatePickerFragment();
        newFragment.setCallback(bundle -> {
            int year = bundle.getInt("YEAR");
            int month = bundle.getInt("MONTH");
            int day = bundle.getInt("DAY");
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.YEAR, year);
            cal1.set(Calendar.MONTH, month);
            cal1.set(Calendar.DAY_OF_MONTH, day);
            date2Button.setText(DateUtility.DATE_FORMAT.format(cal1.getTime()));
        });
        Bundle args = new Bundle();
        args.putInt("YEAR", cal.get(Calendar.YEAR));
        args.putInt("MONTH", cal.get(Calendar.MONTH));
        args.putInt("DAY", cal.get(Calendar.DAY_OF_MONTH));
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTime1PickerDialog(View v) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(DateUtility.TIME_FORMAT.parse(time1Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FilterTimePickerFragment newFragment = new FilterTimePickerFragment();
        newFragment.setCallback(bundle -> {
            int hour = bundle.getInt("HOUR");
            int minute = bundle.getInt("MINUTE");
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.HOUR_OF_DAY, hour);
            cal1.set(Calendar.MINUTE, minute);
            time1Button.setText(DateUtility.TIME_FORMAT.format(cal1.getTime()));
        });
        Bundle args = new Bundle();
        args.putInt("HOUR", cal.get(Calendar.HOUR_OF_DAY));
        args.putInt("MINUTE", cal.get(Calendar.MINUTE));
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTime2PickerDialog(View v) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(DateUtility.TIME_FORMAT.parse(time2Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FilterTimePickerFragment newFragment = new FilterTimePickerFragment();
        newFragment.setCallback(bundle -> {
            int hour = bundle.getInt("HOUR");
            int minute = bundle.getInt("MINUTE");
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.HOUR_OF_DAY, hour);
            cal1.set(Calendar.MINUTE, minute);
            time2Button.setText(DateUtility.TIME_FORMAT.format(cal1.getTime()));
        });
        Bundle args = new Bundle();
        args.putInt("HOUR", cal.get(Calendar.HOUR_OF_DAY));
        args.putInt("MINUTE", cal.get(Calendar.MINUTE));
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public interface DialogFragmentCallback {
        void onResultCallback(Bundle bundle);
    }

    public static class FilterDatePickerFragment
            extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private DialogFragmentCallback callback;

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Gets the date data from the arguments
            Bundle args = getArguments();
            int year = args.getInt("YEAR");
            int month = args.getInt("MONTH");
            int day = args.getInt("DAY");

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setCallback(DialogFragmentCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Bundle bundle = new Bundle();
            bundle.putInt("YEAR", year);
            bundle.putInt("MONTH", month);
            bundle.putInt("DAY", day);
            callback.onResultCallback(bundle);
        }
    }

    public static class FilterTimePickerFragment
            extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        DialogFragmentCallback callback;

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Bundle args = getArguments();
            int hour = args.getInt("HOUR");
            int minute = args.getInt("MINUTE");

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this,
                    hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void setCallback(DialogFragmentCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Bundle bundle = new Bundle();
            bundle.putInt("HOUR", hourOfDay);
            bundle.putInt("MINUTE", minute);
            callback.onResultCallback(bundle);
        }
    }
}
