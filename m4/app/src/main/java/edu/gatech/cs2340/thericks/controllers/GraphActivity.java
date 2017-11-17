package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.Months;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.utils.DateFilterer;
import edu.gatech.cs2340.thericks.utils.SerializablePredicate;

/**
 * Created by Cameron on 11/3/2017.
 * Holds multiple different graphs for displaying rat data
 */

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = GraphActivity.class.getSimpleName();

    //default dates to filter out rat data that occurs between the dates
    private static final String beginDateString = "01/01/2015 12:00:00 AM";
    private static final String endDateString = "10/01/2015 12:00:00 AM";
    private static final Date begin = DateFilterer.parse(beginDateString);
    private static final Date end = DateFilterer.parse(endDateString);

    private LineChart chart;

    private ProgressBar progressBar;

    private List<RatData> loadedData;

    private List<Predicate<RatData>> filters;

    private Button applyFiltersButton;

    private TextView noDataText;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_graphs);

        Log.d(TAG, "Entered Graph Activity");

        progressBar = findViewById(R.id.graph_progress_bar);

        noDataText = findViewById(R.id.no_data_graph_text);
        noDataText.setVisibility(View.GONE);

        chart = findViewById(R.id.chart);
        chart.setVisibility(View.GONE);

        applyFiltersButton = findViewById(R.id.apply_filters_button_graph);
        applyFiltersButton.setVisibility(View.GONE);
        applyFiltersButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, FilterActivity.class);
            startActivityForResult(intent, FilterActivity.GET_FILTERS);
        });

        filters = new ArrayList<>();
        filters.add(DateFilterer.createDateRangeFilter(begin, end));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FilterActivity.GET_FILTERS) {
            if (resultCode == RESULT_OK) {
                filters.clear();
                filters.addAll((List<Predicate<RatData>>) data.getSerializableExtra("PREDICATES"));
                applyFiltersButton.setEnabled(false);
                ArrayAdapter<RatData> tempAdapter = new ArrayAdapter<RatData>(getApplicationContext(), ArrayAdapter.NO_SELECTION) {

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
            }
        }
    }

    /**
     * Displays the graph, calculates the bounds for the specified date range,
     * and gets the needed data from the database
     */
    private void displayGraph() {
        progressBar.setVisibility(View.VISIBLE);
        chart.setVisibility(View.GONE);
        noDataText.setVisibility(View.GONE);
        applyFiltersButton.setEnabled(false);

        Log.d(TAG, "Sorting data");
        loadedData.sort(new Comparator<RatData>() {

            @Override
            public int compare(RatData ratData1, RatData ratData2) {
                Date date1 = DateFilterer.parse(ratData1.getCreatedDateTime());
                Date date2 = DateFilterer.parse(ratData2.getCreatedDateTime());
                if (date1 == null && date2 == null) {
                    return 0;
                }
                if (date1 == null) {
                    return -1;
                }
                if (date2 == null) {
                    return 1;
                }
                return date1.compareTo(date2);
            }

        });

        Log.d(TAG, "Displaying graph");

        if (!loadedData.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(DateFilterer.parse(loadedData.get(0).getCreatedDateTime()));
            int beginMonth = cal.get(Calendar.MONTH);
            int beginYear = cal.get(Calendar.YEAR);
            cal.clear();

            cal.setTime(DateFilterer.parse(loadedData.get(loadedData.size() - 1).getCreatedDateTime()));
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

            LineDataSet dataSet = new LineDataSet(entries, "Rat Sightings");
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);

            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(domainDates));
            xAxis.setLabelRotationAngle(60);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            chart.invalidate();

            chart.setVisibility(View.VISIBLE);
        } else {
            noDataText.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);
        applyFiltersButton.setVisibility(View.VISIBLE);
        applyFiltersButton.setEnabled(true);
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
                return Months.values()[c.get(Calendar.MONTH)].toString()
                        + " " + c.get(Calendar.YEAR);
            } else {
                return intValue + "";
            }
        }
    }
}