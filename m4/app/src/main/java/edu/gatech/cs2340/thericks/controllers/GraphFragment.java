package edu.gatech.cs2340.thericks.controllers;

import android.support.v4.app.Fragment;

import com.github.mikephil.charting.charts.Chart;

import java.util.List;

/**
 * Created by Cameron on 12/4/2017.
 * Extension of the Fragment class to include a method for displaying graphs and holds a chart
 */
public abstract class GraphFragment<T> extends Fragment {

    public abstract void displayGraph(List<T> list);
    public abstract Chart getChart();
}
