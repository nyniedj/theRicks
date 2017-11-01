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
        db = DatabaseHandler.provideWritableDatabase();
    }

    /**
     *
     * @param a
     * @param data
     * @param progressBar
     * @param filters
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

    /**
     *  @param key
     * @param createdDateTime
     * @param locationType
     * @param incidentZip
     * @param incidentAddress
     * @param city
     * @param borough
     * @param latitude
     * @param longitude
     */
    @Override
    public void createRatData(int key, RatDateTime createdDateTime, String locationType, int incidentZip,
                              String incidentAddress, String city, String borough, double latitude,
                              double longitude) {
        RatDataDAO.createRatData(db, key, createdDateTime, locationType, incidentZip, incidentAddress,
                city, borough, latitude, longitude);
    }

    /**
     *
     * @param data
     */
    @Override
    public void deleteRatData(RatData data) {
        RatDataDAO.deleteRatData(db, data.getKey());
    }

    /**
     *
     * @return all rat data
     */
    @Override
    public List<RatData> getAllRatData() {
        return RatDataDAO.getAllRatData(db);
    }

    /**
     *
     * @param filters
     * @return filtered rat data
     */
    public List<RatData> getFilteredRatData(List<Predicate<RatData>> filters) {
        return RatDataDAO.applyFilters(RatDataDAO.getAllRatData(db), filters);
    }

    /**
     *
     * @param key
     * @return rat data by key
     */
    @Override
    public RatData findRatDataByKey(int key) {
        return RatDataDAO.findRatDataByKey(db, key);
    }
}
