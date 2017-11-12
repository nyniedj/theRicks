package edu.gatech.cs2340.thericks.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cameron on 10/31/2017.
 * Holds a date and can read properly formatted Strings
 * as dates
 */
public class RatDate implements Comparable<RatDate>, Serializable{

    private int year;
    private int month;
    private int day;

    private RatDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private RatDate(String timeDef) {
        String[] splitDef = timeDef.split("/");
        month = Integer.parseInt(splitDef[0]);
        day = Integer.parseInt(splitDef[1]);
        year = Integer.parseInt(splitDef[2]);
    }

    /**
     * Creates a new RatDate from the passed data
     * @param year the year
     * @param month the month
     * @param day the day
     * @return the new RatDate
     */
    public static RatDate forDate(int year, int month, int day) {
        return new RatDate(year, month, day);
    }

    /**
     * Creates a new RatDate from the passed String, returns
     * null if the String doesn't contain a date
     * @param possibleDateDef the String to pull a date from
     * @return the new RatDate
     */
    public static RatDate forDate(String possibleDateDef) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(possibleDateDef);
        if (m.find()) {
            return new RatDate(m.group());
        }
        return null;
    }

    /**
     * Builds a new RatDate from the passed RatDate, effectively cloning it
     * @param d the RatDate to build a RatDate from
     * @return the new RatDate
     */
    public static RatDate forDate(RatDate d) {
        if (d != null) {
            return new RatDate(d.getYear(), d.getMonth(), d.getDay());
        }
        return null;
    }

    /**
     * Checks if the passed String contains a date
     * @param possibleDateDef the String to check
     * @return true if it contains a date, false otherwise
     */
    public static boolean isDate(String possibleDateDef) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(possibleDateDef);
        return m.find();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int compareTo(@NonNull RatDate ratDate) {
        if (year < ratDate.year) {
            return -1;
        } else if (year > ratDate.year) {
            return 1;
        } else {
            if (month < ratDate.month) {
                return -1;
            } else if (month > ratDate.month) {
                return 1;
            } else {
                if (day < ratDate.day) {
                    return -1;
                } else if (day > ratDate.day) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%02d", month) + "/" + String.format("%02d", day) + "/"
                + String.format("%04d", year);
    }
}