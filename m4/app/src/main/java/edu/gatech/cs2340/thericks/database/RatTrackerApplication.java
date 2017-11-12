package edu.gatech.cs2340.thericks.database;

import android.app.Application;
import android.content.Context;

/**
 * Class for accessing application level context.  Used in database package to get context.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public class RatTrackerApplication extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
