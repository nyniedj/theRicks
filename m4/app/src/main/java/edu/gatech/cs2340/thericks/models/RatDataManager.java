package edu.gatech.cs2340.thericks.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben Lashley on 10/9/2017.
 */

public class RatDataManager {
    private static final RatDataManager instance = new RatDataManager();

    public static RatDataManager getInstance() {
        return instance;
    }

    private List<RatData> ratDataList;

    private boolean dataLoaded;
    private boolean isLoading;

    private RatDataManager() {
        ratDataList = new ArrayList<>(100000);
        dataLoaded = false;
        isLoading = false;
    }

    /* Adds provided rat data to ratDataList if not null. */
    public void addRatData(RatData data) {
        if (data != null) {
            ratDataList.add(data);
        }
    }

    /* Adds new rat data with provided details into the ratDataList. */
    public void addRatData(int key, String createdDateTime, String locationType, int incidentZip,
                           String incidentAddress, String city, String borough, double latitude,
                           double longitude) {

        addRatData(new RatData(key, createdDateTime, locationType, incidentZip, incidentAddress,
                city, borough, latitude, longitude));
    }

    public List<RatData> getRatDataList() {
        return ratDataList;
    }

    /**
     *
     * @return true if rat_data.csv has been fully loaded into ratDataList; false otherwise
     */
    public boolean isDataLoaded() {
        return dataLoaded;
    }

    /**
     *
     * @return true if data is being loaded into ratDataList; false otherwise
     */
    public boolean isDataLoading() {
        return isLoading;
    }

    /* Inform manager that data is being loaded in. */
    public void startLoading() {
        isLoading = true;
    }

    /* Inform manager that data is done being loaded. */
    public void finishLoading() {
        dataLoaded = true;
        isLoading = false;
    }
}