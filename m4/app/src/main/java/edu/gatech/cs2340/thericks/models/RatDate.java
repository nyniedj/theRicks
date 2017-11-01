package edu.gatech.cs2340.thericks.models;

import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cameron on 10/31/2017.
 */
public class RatDate implements Comparable<RatDate>{

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

    public static RatDate forDate(int year, int month, int day) {
        return new RatDate(year, month, day);
    }

    public static RatDate forDate(String possibleDateDef) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(possibleDateDef);
        if (m.find()) {
            return new RatDate(m.group());
        }
        return null;
    }

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
