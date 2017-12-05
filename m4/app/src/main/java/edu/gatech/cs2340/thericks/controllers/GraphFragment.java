package edu.gatech.cs2340.thericks.controllers;

import android.support.v4.app.Fragment;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.gatech.cs2340.thericks.models.Months;

/**
 * Created by Cameron on 12/4/2017.
 * Extension of the Fragment class to include a method for displaying graphs and holds a chart
 */
public abstract class GraphFragment<T> extends Fragment {

    public abstract void displayGraph(List<T> list);
    public abstract Chart getChart();

    public class DateXAxisValueFormatter implements IAxisValueFormatter {

        private final Date[] mValues;

        DateXAxisValueFormatter(Date[] values) {
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
