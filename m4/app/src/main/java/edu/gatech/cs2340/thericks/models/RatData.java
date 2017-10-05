package edu.gatech.cs2340.thericks.models;

/**
 * Created by Cameron on 10/5/2017.
 */

public class RatData {

    private int key;
    private String createdDateTime;
    private String locationType;
    private int incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private double latitude;
    private double longitude;

    public RatData(int key, String createdDateTime, String locationType,
                   int incidentZip, String incidentAddress, String city,
                   String borough, double latitude, double longitude) {
        this.key = key;
        this.createdDateTime = createdDateTime;
        this.locationType = locationType;
        this.incidentZip = incidentZip;
        this.incidentAddress = incidentAddress;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getKey() {
        return key;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public String getLocationType() {
        return locationType;
    }

    public int getIncidentZip() {
        return incidentZip;
    }

    public String getIncidentAddress() {
        return incidentAddress;
    }

    public String getCity() {
        return city;
    }

    public String getBorough() {
        return borough;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
