package edu.gatech.cs2340.thericks.models;

import java.util.List;

/**
 * Interface for a source of rat data with basic operations for managing a collection of rat data.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public interface RatDataSource {
    void createRatData(int key, RatDateTime createdDateTime, String locationType,
                       int incidentZip, String incidentAddress, String city,
                       String borough, double latitude, double longitude);
    void deleteRatData(RatData data);
    List<RatData> getAllRatData();
    RatData findRatDataByKey(int key);
}
