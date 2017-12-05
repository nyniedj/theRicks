package edu.gatech.cs2340.thericks.controllers;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Created by Cameron on 12/4/2017.
 * Pager class for swiping between graphs
 */
public class GraphPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    private GraphFragment<RatData> lineGraph;
    private Fragment barGraph;
    private Fragment pieChart;

    GraphPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public GraphFragment<RatData> getItem(int position) {

        switch (position) {
            case 0:
                if (lineGraph == null) {
                    lineGraph = new LineGraphFragment();
                };
                return lineGraph;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}