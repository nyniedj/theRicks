package edu.gatech.cs2340.thericks.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
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

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.Months;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.utils.DateUtility;

/**
 * Created by Cameron on 11/3/2017.
 * Holds the line graph
 */

public class LineGraphFragment extends GraphFragment<RatData> {

    private static final String TAG = LineGraphFragment.class.getSimpleName();

    private static final float X_LABEL_ROTATION = 60f;

    private LineChart lineChart;

    private TextView noDataText;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstance) {
        Log.d(TAG, "Creating Ling Graph View");

        View view = layoutInflater.inflate(R.layout.activity_line_graph, container, false);

        noDataText = view.findViewById(R.id.no_data_graph_text);
        noDataText.setVisibility(View.GONE);

        lineChart = view.findViewById(R.id.chart);
        lineChart.setVisibility(View.GONE);

        return view;
    }

    /**
     * Displays the graph, calculates the bounds for the specified date range,
     * and gets the needed data from the database
     */
    public void displayGraph(List<RatData> loadedData) {

        Log.d(TAG, "Sorting data");
        loadedData.sort((ratData1, ratData2) -> {
            Date date1 = DateUtility.parse(ratData1.getCreatedDateTime());
            Date date2 = DateUtility.parse(ratData2.getCreatedDateTime());
            if ((date1 == null) && (date2 == null)) {
                return 0;
            }
            if (date1 == null) {
                return -1;
            }
            if (date2 == null) {
                return 1;
            }
            return date1.compareTo(date2);
        });

        Log.d(TAG, "Displaying graph");

        if (!loadedData.isEmpty()) {
            noDataText.setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(DateUtility.parse(loadedData.get(0).getCreatedDateTime()));
            int beginMonth = cal.get(Calendar.MONTH);
            int beginYear = cal.get(Calendar.YEAR);
            cal.clear();

            cal.setTime(DateUtility.parse(
                    loadedData.get(loadedData.size() - 1).getCreatedDateTime()));
            int endMonth = cal.get(Calendar.MONTH);
            int endYear = cal.get(Calendar.YEAR);
            cal.clear();

            int monthDif = (endMonth + (endYear * Months.values().length))
                    - (beginMonth + (beginYear * Months.values().length));
            Date[] domainDates = new Date[monthDif];
            for (int i = 0; i < domainDates.length; i++) {
                int month = beginMonth + i;
                int year = beginYear + ((month + 1) / Months.values().length);
                month = ((month + 1) % Months.values().length) - 1;
                cal.clear();
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, year);
                domainDates[i] = cal.getTime();
            }

            List<Entry> entries = new ArrayList<>();
            for (int i = 1; i < domainDates.length; i++) {
                entries.add(new Entry(i, DateUtility.filterByDate(domainDates[i - 1],
                        domainDates[i], loadedData).size()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Rat Sightings");
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(domainDates));
            xAxis.setLabelRotationAngle(X_LABEL_ROTATION);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            lineChart.invalidate();

            lineChart.setVisibility(View.VISIBLE);
        } else {
            noDataText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Chart getChart() {
        return lineChart;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private final Date[] mValues;

        MyXAxisValueFormatter(Date[] values) {
            this.mValues = values.clone();
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            int intValue = (int) value;
            if ((intValue < mValues.length) && (intValue >= 0)) {
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