package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
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

    //default dates to filter out rat data that occurs between the dates
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

        Log.d(TAG, "Entered Graph Activity");

        progressBar = (ProgressBar) findViewById(R.id.graph_progress_bar);
        xyPlot = (XYPlot) findViewById(R.id.graph_xy_plot);
        xyPlot.setVisibility(View.GONE);
        xyPlot.setLinesPerRangeLabel(3);
        xyPlot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
        xyPlot.setLinesPerDomainLabel(2);

        barFormatter = new BarFormatter(Color.RED, Color.RED);
        barFormatter.setMarginRight(PixelUtils.dpToPix(5));
        barFormatter.setMarginLeft(PixelUtils.dpToPix(5));

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

    private void displayGraph() {
        Log.d(TAG, "Displaying graph");
        final Number[] domainLabels = {1, 2, 3, 4, 5, 6, 7};
        XYSeries series = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Test", 1, 2, 3, 4, 5, 6, 7);

        xyPlot.addSeries(series, barFormatter);

        BarRenderer renderer = xyPlot.getRenderer(BarRenderer.class);
        renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_GAP, 5);
        xyPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }

        });

        xyPlot.redraw();

        xyPlot.setVisibility(View.VISIBLE);
    }
}
