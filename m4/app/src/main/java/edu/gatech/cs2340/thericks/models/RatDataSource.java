package edu.gatech.cs2340.thericks.models;

import java.util.List;

/**
 * Created by Ben Lashley on 10/18/2017.
 */

public interface RatDataSource {
    void createRatData(int key, String createdDateTime, String locationType,
                          int incidentZip, String incidentAddress, String city,
                          String borough, double latitude, double longitude);
    void deleteRatData(RatData data);
    List<RatData> getAllRatData();
    RatData findRatDataByKey(int key);
}
