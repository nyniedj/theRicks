package edu.gatech.cs2340.thericks.models;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.utils.DateUtility;

/**
 * Created by Cameron on 11/18/2017.
 * Holds the raw data and Predicates needed for filtering RatData. Its
 * primary reason for existence is to be passed between FilterActivity
 * and whatever activity starts FilterActivity for a result, so that
 * filters can be obtained from the FilterActivity and so that the
 * FilterActivity can rebuild itself from the raw data inside this
 * class. Any custom Predicates added to this class (custom meaning
 * any Predicate that will be put into the map with a procedurally
 * generated key)
 */
public class RatFilter implements Parcelable {

    public static final String DATE = "DATE";
    public static final String LOCATION_TYPE = "LOCATION_TYPE";
    public static final String ZIP = "ZIP";
    public static final String ADDRESS = "ADDRESS";
    public static final String CITY = "CITY";
    public static final String BOROUGH = "BOROUGH";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    private Map<String, Predicate<RatData>> predicates;

    // raw data
    private String beginDateStr;
    private String beginTimeStr;
    private String endDateStr;
    private String endTimeStr;
    private String locationType;
    private Integer zip;
    private String address;
    private String city;
    private String borough;
    private Double minLatitude;
    private Double maxLatitude;
    private Double minLongitude;
    private Double maxLongitude;

    private RatFilter() {
        predicates = new HashMap<>();
    }

    public RatFilter(Iterable<Predicate<RatData>> set) {
        predicates = new HashMap<>();
        if (set != null) {
            int i = 0;
            for (Predicate<RatData> p : set) {
                if (p != null) {
                    predicates.put(i + p.toString(), p);
                    i++;
                }
            }
        }
    }

    private void buildAllPredicates() {
        if (beginDateStr != null
                && beginTimeStr != null
                && endDateStr != null
                && endTimeStr != null) {
            buildDatePredicate();
        }
        if (locationType != null) {
            buildLocationTypePredicate();
        }
        if (zip != null) {
            buildZipPredicate();
        }
        if (address != null) {
            buildAddressPredicate();
        }
        if (city != null) {
            buildCityPredicate();
        }
        if (borough != null ) {
            buildBoroughPredicate();
        }
        if (minLatitude != null && maxLatitude != null) {
            buildLatitudePredicate();
        }
        if (minLongitude != null && maxLongitude != null) {
            buildLongitudePredicate();
        }
    }

    private void buildDatePredicate() {
        predicates.put(DATE, DateUtility.createDateRangeFilter(
                DateUtility.parse(beginDateStr + " " + beginTimeStr),
                DateUtility.parse(endDateStr + " " + endTimeStr)));
    }

    private void buildLocationTypePredicate() {
        predicates.put(LOCATION_TYPE, new Predicate<RatData>() {

            @Override
            public boolean test(RatData ratData) {
                return ratData.getLocationType().equalsIgnoreCase(locationType);
            }

        });
    }

    private void buildZipPredicate() {
        predicates.put(ZIP, new Predicate<RatData>() {

            @Override
            public boolean test(RatData ratData) {
                return ratData.getIncidentZip() == zip;
            }

        });
    }

    private void buildAddressPredicate() {
        predicates.put(ADDRESS, new Predicate<RatData>() {

            @Override
            public boolean test(RatData ratData) {
                return ratData.getIncidentAddress().contains(address);
            }

        });
    }

    private void buildCityPredicate() {
        predicates.put(CITY, new Predicate<RatData>() {

            @Override
            public boolean test(RatData ratData) {
                return ratData.getCity().equalsIgnoreCase(city);
            }

        });
    }

    private void buildBoroughPredicate() {
        predicates.put(BOROUGH, new Predicate<RatData>() {

            @Override
            public boolean test(RatData ratData) {
                return ratData.getBorough().equalsIgnoreCase(borough);
            }

        });
    }

    private void buildLatitudePredicate() {
        predicates.put(LATITUDE, new Predicate<RatData>() {

            @Override
            public boolean test(RatData ratData) {
                return ratData.getLatitude() >= minLatitude
                        && ratData.getLatitude() <= maxLatitude;
            }

        });
    }

    private void buildLongitudePredicate() {
        predicates.put(LONGITUDE, new Predicate<RatData>() {

            @Override
            public boolean test(RatData ratData) {
                return ratData.getLongitude() >= minLongitude
                        && ratData.getLongitude() <= maxLongitude;
            }

        });
    }

    public void clearDatePredicate() {
        beginDateStr = null;
        beginTimeStr = null;
        endDateStr = null;
        endTimeStr = null;
        predicates.put(DATE, null);
    }

    public void clearLocationTypePredicate() {
        locationType = null;
        predicates.put(LOCATION_TYPE, null);
    }

    public void clearZipPredicate() {
        zip = null;
        predicates.put(ZIP, null);
    }

    public void clearAddressPredicate() {
        address = null;
        predicates.put(ADDRESS, null);
    }

    public void clearCityPredicate() {
        city = null;
        predicates.put(CITY, null);
    }

    public void clearBoroughPredicate() {
        borough = null;
        predicates.put(BOROUGH, null);
    }

    public void clearLatitudePredicate() {
        minLatitude = null;
        maxLatitude = null;
        predicates.put(LATITUDE, null);
    }

    public void clearLongitudePredicate() {
        minLongitude = null;
        maxLongitude = null;
        predicates.put(LONGITUDE, null);
    }

    public void setBeginDate(Date beginDate) {
        beginDateStr = DateUtility.DATE_FORMAT.format(beginDate);
        beginTimeStr = DateUtility.TIME_FORMAT.format(beginDate);
        if (endDateStr != null && endTimeStr != null) {
            buildDatePredicate();
        }
    }

    public void setEndDate(Date endDate) {
        endDateStr = DateUtility.DATE_FORMAT.format(endDate);
        endTimeStr = DateUtility.TIME_FORMAT.format(endDate);
        if (beginDateStr != null && beginTimeStr != null) {
            buildDatePredicate();
        }
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
        buildLocationTypePredicate();
    }

    public void setZip(int zip) {
        this.zip = zip;
        buildZipPredicate();
    }

    public void setAddress(String address) {
        this.address = address;
        buildAddressPredicate();
    }

    public void setCity(String city) {
        this.city = city;
        buildCityPredicate();
    }

    public void setBorough(String borough) {
        this.borough = borough;
        buildBoroughPredicate();
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
        if (maxLatitude != null) {
            buildLatitudePredicate();
        }
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
        if (minLatitude != null) {
            buildLatitudePredicate();
        }
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
        if (maxLongitude != null) {
            buildLongitudePredicate();
        }
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
        if (minLongitude != null) {
            buildLongitudePredicate();
        }
    }

    public boolean hasPredicate(String key) {
        return predicates.get(key) != null;
    }

    public String getBeginDateStr() {
        return beginDateStr;
    }

    public String getBeginTimeStr() {
        return beginTimeStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public String getLocationType() {
        return locationType;
    }

    public int getZip() {
        return zip;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getBorough() {
        return borough;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public List<Predicate<RatData>> getPredicates() {
        return new ArrayList<>(predicates.values());
    }

    public static RatFilter getDefaultInstance() {
        RatFilter filter = new RatFilter();
        filter.setBeginDate(DateUtility.getLastMonth());
        filter.setEndDate(Calendar.getInstance().getTime());
        return filter;
    }

    //////////////////////////////////////
    // CODE FOR PARCELABLE IMPLEMENTATION
    //////////////////////////////////////
    private RatFilter(Parcel in) {
        predicates = new HashMap<>();
        beginDateStr = in.readString();
        beginTimeStr = in.readString();
        endDateStr = in.readString();
        endTimeStr = in.readString();
        locationType = in.readString();
        zip = (Integer) in.readValue(Integer.class.getClassLoader());
        address = in.readString();
        city = in.readString();
        borough = in.readString();
        minLatitude = (Double) in.readValue(Double.class.getClassLoader());
        maxLatitude = (Double) in.readValue(Double.class.getClassLoader());
        minLongitude = (Double) in.readValue(Double.class.getClassLoader());
        maxLongitude = (Double) in.readValue(Double.class.getClassLoader());
        buildAllPredicates();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(beginDateStr);
        parcel.writeString(beginTimeStr);
        parcel.writeString(endDateStr);
        parcel.writeString(endTimeStr);
        parcel.writeString(locationType);
        parcel.writeValue(zip);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(borough);
        parcel.writeValue(minLatitude);
        parcel.writeValue(maxLatitude);
        parcel.writeValue(minLongitude);
        parcel.writeValue(maxLongitude);
    }

    public static final Parcelable.Creator<RatFilter> CREATOR
            = new Parcelable.Creator<RatFilter>() {
        @Override
        public RatFilter createFromParcel(Parcel in) {
            return new RatFilter(in);
        }

        @Override
        public RatFilter[] newArray(int size) {
            return new RatFilter[size];
        }
    };
}
