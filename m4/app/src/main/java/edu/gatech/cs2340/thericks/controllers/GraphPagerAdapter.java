package edu.gatech.cs2340.thericks.controllers;



import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by Cameron on 12/4/2017.
 * Pager class for swiping between graphs
 */
public class GraphPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    private GraphFragment<RatData> lineGraph;
    private GraphFragment<RatData> barGraph;
    private GraphFragment<RatData> pieChart;

    private boolean[] isDisplayed;

    GraphPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.isDisplayed = new boolean[NumOfTabs];
    }

    @Override
    public GraphFragment<RatData> getItem(int position) {

        switch (position) {
            case 0:
                if (lineGraph == null) {
                    lineGraph = new LineGraphFragment();
                };
                return lineGraph;
            case 1:
                if (barGraph == null) {
                    barGraph = new BarGraphFragment();
                }
                return barGraph;
            case 2:
                if (pieChart == null) {
                    pieChart = new PieChartFragment();
                }
                return pieChart;
            default:
                return null;
        }
    }

    boolean isDisplayed(int position) {
        return isDisplayed[position];
    }

    void setDisplayed(int position, boolean value) {
        isDisplayed[position] = value;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}