package edu.gatech.cs2340.thericks.controllers;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.Months;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.utils.DateFilterer;

/**
 * Created by Cameron on 11/3/2017.
 * Holds multiple different graphs for displaying rat data
 */

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = GraphActivity.class.getSimpleName();

    //default dates to filter out rat data that occurs between the dates
    private static final String beginDateString = "01/01/2015 12:00:00 AM";
    private static final String endDateString = "10/01/2015 12:00:00 AM";
    private Date begin = DateFilterer.parse(beginDateString);
    private Date end = DateFilterer.parse(endDateString);

    private LineChart chart;

    private ProgressBar progressBar;

    private ArrayList<RatData> loadedData;

    private ArrayList<Predicate<RatData>> filters;
    private Predicate<RatData> dateInRange;

    private EditText date1Edit;
    private EditText date2Edit;
    private Button applyFiltersButton;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_graphs);

        Log.d(TAG, "Entered Graph Activity");

        progressBar = findViewById(R.id.graph_progress_bar);

        chart = findViewById(R.id.chart);
        chart.setVisibility(View.GONE);

        applyFiltersButton = findViewById(R.id.apply_filters_button_graph);
        applyFiltersButton.setVisibility(View.GONE);
        applyFiltersButton.setOnClickListener(v -> {
            begin = DateFilterer.parse(date1Edit.getText().toString());
            end = DateFilterer.parse(date2Edit.getText().toString());
            filters.remove(dateInRange);
            dateInRange = DateFilterer.createDateRangeFilter(begin, end);
            filters.add(dateInRange);
            applyFiltersButton.setEnabled(false);
            ArrayAdapter<RatData> tempAdapter= new ArrayAdapter<RatData>(getApplicationContext(), ArrayAdapter.NO_SELECTION) {

                @Override
                public void notifyDataSetChanged() {
                    super.notifyDataSetChanged();
                    Log.d(TAG, "Notified that the data finished loading");
                    displayGraph();
                }
            };
            progressBar.setVisibility(View.VISIBLE);
            RatDatabase db = new RatDatabase();
            db.loadData(tempAdapter, loadedData, filters);
        });

        TextView dateSeparator = findViewById(R.id.date_separator_graph_text);
        dateSeparator.setVisibility(View.GONE);

        date1Edit = findViewById(R.id.date1_graph);
        date1Edit.setVisibility(View.GONE);
        date1Edit.setText(beginDateString);
        date1Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (DateFilterer.parse(text) != null) {
                    date1Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                    applyFiltersButton.setEnabled(true);
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    date1Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                    applyFiltersButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        date2Edit = findViewById(R.id.date2_graph);
        date2Edit.setVisibility(View.GONE);
        date2Edit.setText(endDateString);
        date2Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (DateFilterer.parse(text) != null) {
                    date2Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                    applyFiltersButton.setEnabled(true);
                } else {
                    Log.d(TAG, getString(R.string.improper_date_input) + text);
                    date2Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                    applyFiltersButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        filters = new ArrayList<>();

        dateInRange = DateFilterer.createDateRangeFilter(begin, end);
        filters.add(dateInRange);

        loadedData = new ArrayList<>();
        ArrayAdapter<RatData> tempAdapter= new ArrayAdapter<RatData>(getApplicationContext(), ArrayAdapter.NO_SELECTION) {

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                Log.d(TAG, "Notified that the data finished loading");
                displayGraph();
            }
        };

        RatDatabase db = new RatDatabase();
        db.loadData(tempAdapter, loadedData, filters);
    }

    /**
     * Displays the graph, calculates the bounds for the specified date range,
     * and gets the needed data from the database
     */
    private void displayGraph() {
        Log.d(TAG, "Displaying graph");

        progressBar.setVisibility(View.VISIBLE);
        applyFiltersButton.setEnabled(false);

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(begin);
        int beginMonth = cal.get(Calendar.MONTH);
        int beginYear = cal.get(Calendar.YEAR);
        cal.clear();

        cal.setTime(end);
        int endMonth = cal.get(Calendar.MONTH);
        int endYear = cal.get(Calendar.YEAR);
        cal.clear();

        int monthDif = (endMonth + (endYear * 12)) - (beginMonth + (beginYear * 12));
        Date[] domainDates = new Date[monthDif];
        for (int i = 0; i < domainDates.length; i++) {
            int month = beginMonth + i;
            int year = beginYear + ((month + 1) / 12);
            month = ((month + 1) % 12) - 1;
            cal.clear();
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            domainDates[i] = cal.getTime();
        }

        List<Entry> entries = new ArrayList<>();
        for (int i = 1; i < domainDates.length; i++) {
            entries.add(new Entry(i, DateFilterer.filterByDate(domainDates[i - 1], domainDates[i], loadedData).size()));
        }

        Log.e(TAG, entries.toString());

        LineDataSet dataSet = new LineDataSet(entries, "Rat Sightings");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(domainDates));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.invalidate();

        progressBar.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);
        date1Edit.setVisibility(View.VISIBLE);
        date2Edit.setVisibility(View.VISIBLE);
        applyFiltersButton.setVisibility(View.VISIBLE);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private final Date[] mValues;

        MyXAxisValueFormatter(Date[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            int intValue = (int) value;
            if (intValue < mValues.length && intValue >= 0) {
                Calendar c = Calendar.getInstance();
                c.clear();
                c.setTime(mValues[intValue]);
                return Months.values()[c.get(Calendar.MONTH)].toString();
            } else {
                return intValue + "";
            }
        }
    }
}