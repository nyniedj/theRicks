package edu.gatech.cs2340.thericks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.controllers.RatDataListActivity;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;

/**
 * Class representing a database of rat data.  This class is used as an interface between the database and the activities.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public class RatDatabase implements RatDataSource {

    private static final String TAG = RatDatabase.class.getSimpleName();
    private DatabaseHandler handler;
    private SQLiteDatabase db;

    public RatDatabase(Context context) {
        if (context == null) {
            context = RatTrackerApplication.getAppContext();
        }
        handler = new DatabaseHandler(context);
        open();
    }

    public void open() {
        db = handler.getWritableDatabase();
    }

    public void close() {
        handler.close();
    }

    public void loadData(ArrayAdapter a, List<RatData> data, ProgressBar progressBar, List<Predicate<RatData>> filters) {
        if (!LoadRatDataTask.isReady()) {
            Log.d(TAG, "LoadRatDataTask was not ready to load data");
            return;
        }
        LoadRatDataTask loadData = new LoadRatDataTask();
        loadData.attachViews(a, data, progressBar, filters);
        loadData.execute(db);
    }

    @Override
    public void createRatData(int key, String createdDateTime, String locationType, int incidentZip,
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

    public List<RatData> getFilteredRatData(List<Predicate<RatData>> filters) {
        return RatDataDAO.applyFilters(RatDataDAO.getAllRatData(db), filters);
    }

    @Override
    public RatData findRatDataByKey(int key) {
        return RatDataDAO.findRatDataByKey(db, key);
    }
}
