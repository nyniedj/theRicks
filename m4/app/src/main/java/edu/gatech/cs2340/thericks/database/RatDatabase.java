package edu.gatech.cs2340.thericks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;
import edu.gatech.cs2340.thericks.models.RatDateTime;

/**
 * Class representing a database of rat data.  This class is used as an interface between the database and the activities.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public class RatDatabase implements RatDataSource {

    private static final String TAG = RatDatabase.class.getSimpleName();
    private SQLiteDatabase db;

    public RatDatabase(Context context) {
        open();
    }

    public void open() {
        db = RatDatabaseHandler.provideWritableDatabase();
    }

    /**
     * Loads in a list of RatData Objects
     * @param a the ArrayAdapter that returns the views for each RatData Object
     * @param data the list of RatData Objects whose views will be added
     * @param progressBar indicates the progress of loading in data
     * @param filters the filters used to select certain RatData Objects
     */
    public void loadData(ArrayAdapter a, List<RatData> data, ProgressBar progressBar, List<Predicate<RatData>> filters) {
        if (!LoadRatDataTask.isReady()) {
            Log.d(TAG, "LoadRatDataTask was not ready to load data or data was already loaded");
            if (data != null && a != null) {
                data.clear();
                data.addAll(getFilteredRatData(filters));
                a.notifyDataSetChanged();
            }
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            return;
        }
        Log.d(TAG, "Creating new LoadRatDataTask");
        LoadRatDataTask loadData = new LoadRatDataTask();
        loadData.attachViews(a, data, progressBar, filters);
        loadData.execute(db);
    }

    @Override
    public void createRatData(int key, RatDateTime createdDateTime, String locationType, int incidentZip,
                              String incidentAddress, String city, String borough, double latitude,
                              double longitude) {
        RatDataDAO.createRatData(db, key, createdDateTime, locationType, incidentZip, incidentAddress,
                city, borough, latitude, longitude);
    }

    @Override
    public void deleteRatData(RatData data) {
        RatDataDAO.deleteRatData(db, data.getKey());
    }


    @Override
    public List<RatData> getAllRatData() {
        return RatDataDAO.getAllRatData(db);
    }

    /**
     * Filters initial list of RatData Objects
     * @param filters the filters used to select certain RatData Objects
     * @return a list of filtered RatData Objects
     */
    public List<RatData> getFilteredRatData(List<Predicate<RatData>> filters) {
        // return RatDataDAO.applyFilters(RatDataDAO.getAllRatData(db), filters);
        return RatDataDAO.getFilteredRatData(db, filters);
    }

    @Override
    public RatData findRatDataByKey(int key) {
        return RatDataDAO.findRatDataByKey(db, key);
    }
}
