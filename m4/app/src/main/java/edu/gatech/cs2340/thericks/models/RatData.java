package edu.gatech.cs2340.thericks.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cameron on 10/5/2017.
 * Holds all information for a rat entry
 */
public class RatData implements Parcelable{

    private int key;
    private String createdDateTime;
    private String locationType;
    private int incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private double latitude;
    private double longitude;

    /**
     * Creates rat entry
     * @param key the key
     * @param createdDateTime the date and time
     * @param locationType the location type
     * @param incidentZip the zip
     * @param incidentAddress the address
     * @param city the city
     * @param borough the borough
     * @param latitude the latitude
     * @param longitude the longtitude
     */
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

    /**
     *
     * @return key
     */
    public int getKey() {
        return key;
    }

    /**
     *
     * @return date/time created
     */
    public String getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     *
     * @return location type
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     *
     * @return zip code of report
     */
    public int getIncidentZip() {
        return incidentZip;
    }

    /**
     *
     * @return address of report
     */
    public String getIncidentAddress() {
        return incidentAddress;
    }

    /**
     *
     * @return city of report
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @return borough of report
     */
    public String getBorough() {
        return borough;
    }

    /**
     *
     * @return latitude of report
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @return longitude of report
     */
    public double getLongitude() {
        return longitude;
    }

    /*******************************************************
     * METHODS FOR IMPLEMENTING PARCELABLE
     * *****************************************************/

    /**
     *
     * @param in
     */
    private RatData(Parcel in) {
        key = in.readInt();
        createdDateTime = in.readString();
        locationType = in.readString();
        incidentZip = in.readInt();
        incidentAddress = in.readString();
        city = in.readString();
        borough = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    /**
     *
     * @return zero
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param parcel
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(key);
        parcel.writeString(createdDateTime);
        parcel.writeString(locationType);
        parcel.writeInt(incidentZip);
        parcel.writeString(incidentAddress);
        parcel.writeString(city);
        parcel.writeString(borough);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public static final Parcelable.Creator<RatData> CREATOR
            = new Parcelable.Creator<RatData>() {
        public RatData createFromParcel(Parcel in) {
            return new RatData(in);
        }

        public RatData[] newArray(int size) {
            return new RatData[size];
        }
    };

    /* ****************************************************/
}
