package edu.gatech.cs2340.thericks.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cameron on 10/5/2017.
 * Holds all information for a rat entry
 */
public class RatData implements Parcelable{

    private final int key;
    private final String createdDateTime;
    private final String locationType;
    private final int incidentZip;
    private final String incidentAddress;
    private final String city;
    private final String borough;
    private final double latitude;
    private final double longitude;

    // Creates a rat entry
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

    @SuppressWarnings("TypeMayBeWeakened")
    public String getIncidentAddress() {
        return incidentAddress;
    }

    public String getCity() {
        return city;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public String getBorough() {
        return borough;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /*******************************************************
     * METHODS FOR IMPLEMENTING PARCELABLE
     * *****************************************************/
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

    @Override
    public int describeContents() {
        return 0;
    }

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
