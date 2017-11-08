package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.graphics.Color;
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

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.w3c.dom.Text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.database.RatTrackerApplication;
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
    private Date begin = DateFilterer.parse("01/01/2017 12:00:00 AM");
    private Date end = DateFilterer.parse("09/01/2017 12:00:00 AM");

    private LineChart chart;

    private ProgressBar progressBar;

    private ArrayList<RatData> loadedData;

    private ArrayList<Predicate<RatData>> filters;
    private Predicate<RatData> dateInRange;

    private EditText date1Edit;
    private EditText date2Edit;
    private TextView dateSeperator;
    private Button applyFiltersButton;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_graphs);

        Log.d(TAG, "Entered Graph Activity");

        progressBar = (ProgressBar) findViewById(R.id.graph_progress_bar);

        chart = (LineChart) findViewById(R.id.chart);
        chart.setVisibility(View.GONE);

        applyFiltersButton = (Button) findViewById(R.id.apply_filters_button_graph);
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
            RatDatabase db = new RatDatabase(RatTrackerApplication.getAppContext());
            db.loadData(tempAdapter, loadedData, progressBar, filters);
        });

        dateSeperator = (TextView) findViewById(R.id.date_separator_graph_text);
        dateSeperator.setVisibility(View.GONE);

        date1Edit = (EditText) findViewById(R.id.date1_graph);
        date1Edit.setVisibility(View.GONE);
        date1Edit.setText("01/01/2017 12:00:00 AM");
        date1Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
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
                return;
            }
        });
        date2Edit = (EditText) findViewById(R.id.date2_graph);
        date2Edit.setVisibility(View.GONE);
        date2Edit.setText("09/01/2017 12:00:00 AM");
        date2Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (DateFilterer.parse(text) != null) {
                    date2Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                    applyFiltersButton.setEnabled(true);
                } else {
                    Log.d(TAG, "Improperly formatted input detected: " + text);
                    date2Edit.setTextColor(ResourcesCompat.getColor(getResources(), R.color.errorPrimary, null));
                    applyFiltersButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
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

        RatDatabase db = new RatDatabase(RatTrackerApplication.getAppContext());
        db.loadData(tempAdapter, loadedData, progressBar, filters);
    }

    /**
     * Displays the graph, calculates the bounds for the specified date range,
     * and gets the needed data from the database
     */
    private void displayGraph() {
        Log.d(TAG, "Displaying graph");

        progressBar.setVisibility(View.VISIBLE);
        applyFiltersButton.setEnabled(false);

        int monthDif = ((end.getMonth() + 1) + (end.getYear() * 12)) - ((begin.getMonth() + 1) + (begin.getYear() * 12));
        Date[] domainDates = new Date[monthDif];
        for (int i = 0; i < domainDates.length; i++) {
            int month = begin.getMonth() + i;
            int year = begin.getYear() + ((month + 1) / 12);
            month = ((month + 1) % 12) - 1;
            Date d = (Date) begin.clone();
            d.setMonth(month);
            d.setYear(year);
            domainDates[i] = d;
        }

        List<Entry> entries = new ArrayList<Entry>();
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

        private Date[] mValues;

        public MyXAxisValueFormatter(Date[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            int intValue = (int) value;
            if (intValue < mValues.length && intValue >= 0) {
                return Months.values()[mValues[intValue].getMonth()].toString();
            } else {
                return intValue + "";
            }
        }
    }
}
