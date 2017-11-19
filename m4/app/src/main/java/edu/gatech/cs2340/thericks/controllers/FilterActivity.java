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
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;

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

    private static final String DATE_1 = "DATE1";
    private static final String DATE_2 = "DATE2";
    private static final String TIME_1 = "TIME1";
    private static final String TIME_2 = "TIME2";

    private CheckedTextView dateAndTimeCheck;

    private Button date1Button;
    private Button date2Button;
    private Button time1Button;
    private Button time2Button;

    private RatFilter filter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        date1Button = findViewById(R.id.date_button_filter_1);
        date2Button = findViewById(R.id.date_button_filter_2);
        time1Button = findViewById(R.id.time_button_filter_1);
        time2Button = findViewById(R.id.time_button_filter_2);

        dateAndTimeCheck = findViewById(R.id.date_filter_check_text);

        filter = getIntent().getParcelableExtra(FILTER);
        if (filter == null) {
            filter = RatFilter.getDefaultInstance();
        }

        if (filter.hasDateFilter()) {
            date1Button.setText(filter.getBeginDateStr());
            date2Button.setText(filter.getEndDateStr());
            time1Button.setText(filter.getBeginTimeStr());
            time2Button.setText(filter.getEndTimeStr());
            onDateAndTimeCheckClicked(dateAndTimeCheck);
        }
    }

    public void onApplyButtonClicked(View v) {
        //Date predicate
        if (dateAndTimeCheck.isChecked()) {
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
        } else {
            filter.clearDatePredicate();
        }

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

    public void showDate1PickerDialog(View v) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(DateUtility.DATE_FORMAT.parse(date1Button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        FilterDatePickerFragment newFragment = new FilterDatePickerFragment();
        newFragment.setCallback(new DialogFragmentCallback() {

            @Override
            public void onResultCallback(Bundle bundle) {
                int year = bundle.getInt("YEAR");
                int month = bundle.getInt("MONTH");
                int day = bundle.getInt("DAY");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                date1Button.setText(DateUtility.DATE_FORMAT.format(cal.getTime()));
            }

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
        newFragment.setCallback(new DialogFragmentCallback() {

            @Override
            public void onResultCallback(Bundle bundle) {
                int year = bundle.getInt("YEAR");
                int month = bundle.getInt("MONTH");
                int day = bundle.getInt("DAY");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                date2Button.setText(DateUtility.DATE_FORMAT.format(cal.getTime()));
            }

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
        newFragment.setCallback(new DialogFragmentCallback() {

            @Override
            public void onResultCallback(Bundle bundle) {
                int hour = bundle.getInt("HOUR");
                int minute = bundle.getInt("MINUTE");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                time1Button.setText(DateUtility.TIME_FORMAT.format(cal.getTime()));
            }

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
        newFragment.setCallback(new DialogFragmentCallback() {

            @Override
            public void onResultCallback(Bundle bundle) {
                int hour = bundle.getInt("HOUR");
                int minute = bundle.getInt("MINUTE");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                time2Button.setText(DateUtility.TIME_FORMAT.format(cal.getTime()));
            }

        });
        Bundle args = new Bundle();
        args.putInt("HOUR", cal.get(Calendar.HOUR_OF_DAY));
        args.putInt("MINUTE", cal.get(Calendar.MINUTE));
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public interface DialogFragmentCallback {
        public void onResultCallback(Bundle bundle);
    }

    public static class FilterDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Bundle bundle = new Bundle();
            bundle.putInt("YEAR", year);
            bundle.putInt("MONTH", month);
            bundle.putInt("DAY", day);
            callback.onResultCallback(bundle);
        }
    }

    public static class FilterTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

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

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Bundle bundle = new Bundle();
            bundle.putInt("HOUR", hourOfDay);
            bundle.putInt("MINUTE", minute);
            callback.onResultCallback(bundle);
        }
    }
}
