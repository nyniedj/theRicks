package edu.gatech.cs2340.thericks.models;

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

    private static final String DATE = "DATE";
    private static final String LOCATION_TYPE = "LOCATION_TYPE";

    private Map<String, Predicate<RatData>> predicates;

    private String beginDateStr;
    private String beginTimeStr;
    private String endDateStr;
    private String endTimeStr;

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

    private void buildPredicates() {
        buildDatePredicate();
    }

    private void buildDatePredicate() {
        predicates.put(DATE, DateUtility.createDateRangeFilter(
                DateUtility.parse(beginDateStr + " " + beginTimeStr),
                DateUtility.parse(endDateStr + " " + endTimeStr)));
    }

    public void clearDatePredicate() {
        beginDateStr = null;
        beginTimeStr = null;
        endDateStr = null;
        endTimeStr = null;
        predicates.put(DATE, null);
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

    public boolean hasDateFilter() {
        return predicates.get(DATE) != null;
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
        buildPredicates();
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
