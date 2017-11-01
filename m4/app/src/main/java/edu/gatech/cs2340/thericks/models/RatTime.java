package edu.gatech.cs2340.thericks.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cameron on 10/31/2017.
 * Holds a time and can read in a properly formatted time
 * from a String
 */
public class RatTime implements Comparable<RatTime>, Serializable{

    private int seconds;
    private int minutes;
    private int hours;

    private Period period;

    private RatTime(int hours, int minutes, int seconds, Period period) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.period = period;
    }

    private RatTime(String timeDef) {
        String[] splitDef = timeDef.split(":");
        hours = Integer.parseInt(splitDef[0]);
        minutes = Integer.parseInt(splitDef[1]);
        String[] periodSplit = splitDef[2].split("\\s");
        seconds = Integer.parseInt(periodSplit[0]);
        period = Period.valueOf(periodSplit[1]);
    }

    /**
     * Creates a new RatTime from the passed data
     * @param hours the hours
     * @param minutes the minutes
     * @param seconds the seconds
     * @param period the period (AM or PM)
     * @return the new RatTime
     */
    public static RatTime forTime(int hours, int minutes, int seconds, Period period) {
        return new RatTime(hours, minutes, seconds, period);
    }

    /**
     * Creates a new RatTime from the passed String, returns null
     * if the String doesn't contain a time
     * @param possibleTimeDef the String to pull a time from
     * @return the new RatTime
     */
    public static RatTime forTime(String possibleTimeDef) {
        Pattern p = Pattern.compile("\\d\\d:\\d\\d:\\d\\d\\s[AP]M");
        Matcher m = p.matcher(possibleTimeDef);
        if (m.find()) {
            return new RatTime(m.group());
        }
        return null;
    }

    /**
     * Checks if the passed String contains a time
     * @param possibleTimeDef the String to check
     * @return true if it contains a tiem, false otherwise
     */
    public static boolean isTime(String possibleTimeDef) {
        Pattern p = Pattern.compile("\\d\\d:\\d\\d:\\d\\d\\s[AP]M");
        Matcher m = p.matcher(possibleTimeDef);
        return m.find();
    }

    public int getHours() {
        return hours;
    }

    /**
     * Returns the hours as they would be represented
     * on a 24 hour clock (i.e. 3:00pm becomes 15:00)
     * @return the 24 hours
     */
    public int get24Hours() {
        if (period.equals(Period.PM)) {
            return hours + 12;
        }
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public Period getPeriod() {
        return period;
    }

    @Override
    public int compareTo(@NonNull RatTime ratTime) {
        if (get24Hours() < ratTime.get24Hours()) {
            return -1;
        } else if (get24Hours() > ratTime.get24Hours()) {
            return 1;
        } else {
            if (minutes < ratTime.minutes) {
                return -1;
            } else if (minutes > ratTime.minutes) {
                return 1;
            } else {
                if (seconds < ratTime.seconds) {
                    return -1;
                } else if (seconds > ratTime.seconds) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds) + " " + period.toString();
    }
}
