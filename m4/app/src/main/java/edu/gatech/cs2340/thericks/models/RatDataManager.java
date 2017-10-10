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

    private RatDataManager() {
        ratDataList = new ArrayList<>(100000);
        dataLoaded = false;
    }

    public void addRatData(int key, String createdDateTime, String locationType, int incidentZip,
                           String incidentAddress, String city, String borough, double latitude,
                           double longitude) {

        ratDataList.add(new RatData(key, createdDateTime, locationType, incidentZip, incidentAddress,
                city, borough, latitude, longitude));
    }

    public List<RatData> getRatDataList() {
        return ratDataList;
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void finishLoading() {
        dataLoaded = true;
    }
}