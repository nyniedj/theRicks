package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.gatech.cs2340.thericks.R;

/**
 * Created by Cameron on 11/3/2017.
 * Holds multiple different graphs for displaying rat data
 */

public class GraphActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_graphs);
    }
}
