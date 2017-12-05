package edu.gatech.cs2340.thericks.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.Months;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.utils.DateUtility;

/**
 * Created by Cameron on 12/5/2017.
 * Class to display a bar graph representation of graph data
 */
public class BarGraphFragment extends GraphFragment<RatData> {

    private static final String TAG = BarGraphFragment.class.getSimpleName();

    private static final float X_LABEL_ROTATION = 60f;

    private BarChart barChart;
    private TextView noDataText;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstance) {
        Log.d(TAG, "Creating Bar Graph View");

        View view = layoutInflater.inflate(R.layout.activity_bar_graph, container, false);

        noDataText = view.findViewById(R.id.no_data_bar_graph_text);
        noDataText.setVisibility(View.GONE);

        barChart = view.findViewById(R.id.bar_chart);
        barChart.setVisibility(View.GONE);

        return view;
    }

    @Override
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

            List<BarEntry> entries = new ArrayList<>();
            for (int i = 1; i < domainDates.length; i++) {
                entries.add(new BarEntry(i, DateUtility.filterByDate(domainDates[i - 1],
                        domainDates[i], loadedData).size()));
            }

            BarDataSet dataSet = new BarDataSet(entries, "Rat Sightings");
            BarData barData = new BarData(dataSet);
            barChart.setData(barData);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new DateXAxisValueFormatter(domainDates));
            xAxis.setLabelRotationAngle(X_LABEL_ROTATION);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            barChart.invalidate();

            barChart.setVisibility(View.VISIBLE);
        } else {
            noDataText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Chart getChart() {
        return barChart;
    }
}
