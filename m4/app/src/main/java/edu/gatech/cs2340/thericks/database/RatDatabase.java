package edu.gatech.cs2340.thericks.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;

/**
 * Class representing a database of rat data.  This class is used as an interface between the
 * database and the activities.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public class RatDatabase implements RatDataSource {

    private static final String TAG = RatDatabase.class.getSimpleName();
    private SQLiteDatabase db;

    /**
     * initializes the SQLite Database to a RatDatabaseHandler 
     */
    public RatDatabase() {
        open();
    }

    private void open() {
        db = RatDatabaseHandler.provideWritableDatabase();
    }

    /**
     * Loads in a list of RatData Objects
     * @param a the ArrayAdapter that returns the views for each RatData Object
     * @param data the list of RatData Objects whose views will be added
     * @param filters the filters used to select certain RatData Objects
     */
    public void loadData(ArrayAdapter a, List<RatData> data, List<Predicate<RatData>> filters) {
        if (!LoadRatDataTask.isReady()) {
            Log.d(TAG, "LoadRatDataTask was not ready to load data or data was already loaded");
            if ((data != null) && (a != null)) {
                data.clear();
                data.addAll(getFilteredRatData(filters));
                a.notifyDataSetChanged();
            }
            return;
        }
        Log.d(TAG, "Creating new LoadRatDataTask");
        LoadRatDataTask loadData = new LoadRatDataTask();
        loadData.attachViews(a, data, filters);
        loadData.execute(db);
    }

    @Override
    public void createRatData(int key, String createdDateTime, String locationType, int incidentZip,
                              String incidentAddress, String city, String borough, double latitude,
                              double longitude) {
        RatDataDAO.createRatData(db, key, createdDateTime, locationType, incidentZip,
                incidentAddress, city, borough, latitude, longitude);
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
    public List<RatData> getFilteredRatData(Collection<Predicate<RatData>> filters) {
        // return RatDataDAO.applyFilters(RatDataDAO.getAllRatData(db), filters);
        return RatDataDAO.getFilteredRatData(db, filters);
    }

    @Override
    public RatData findRatDataByKey(int key) {
        return RatDataDAO.findRatDataByKey(db, key);
    }
}
