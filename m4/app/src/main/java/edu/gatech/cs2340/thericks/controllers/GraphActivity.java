package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.database.RatTrackerApplication;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.utils.DateFilterer;

/**
 * Created by Cameron on 11/3/2017.
 * Holds multiple different graphs for displaying rat data
 */

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = GraphActivity.class.getSimpleName();

    private static final Date begin = DateFilterer.parse("08/01/2017 12:00:00 AM");
    private static final Date end = DateFilterer.parse("08/11/2017 12:00:00 AM");

    private XYPlot xyPlot;
    private BarFormatter barFormatter;

    private ProgressBar progressBar;

    private ArrayList<RatData> loadedData;

    private ArrayList<Predicate<RatData>> filters;
    private Predicate<RatData> dateInRange;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_graphs);

        progressBar = (ProgressBar) findViewById(R.id.graph_progress_bar);
        xyPlot = (XYPlot) findViewById(R.id.graph_xy_plot);

        barFormatter = new BarFormatter(Color.RED, Color.RED);

        filters = new ArrayList<>();
        //default date to filter out rat data that occurs after the specified date

        dateInRange = DateFilterer.createDateRangeFilter(begin, end);
        filters.add(dateInRange);

        loadedData = new ArrayList<>();
        ArrayAdapter<RatData> tempAdapter= new ArrayAdapter<RatData>(getApplicationContext(), ArrayAdapter.NO_SELECTION) {

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                displayGraph();
            }
        };

        RatDatabase db = new RatDatabase(RatTrackerApplication.getAppContext());
        db.loadData(tempAdapter, loadedData, progressBar, filters);
    }

    private void displayGraph() {
        Log.d(TAG, "Displaying graph");
        XYSeries series = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Test", 100, 200, loadedData.size());
        xyPlot.addSeries(series, barFormatter);
    }
}
