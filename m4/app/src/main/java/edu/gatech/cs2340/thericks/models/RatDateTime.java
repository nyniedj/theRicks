package edu.gatech.cs2340.thericks.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Cameron on 10/31/2017.
 * Holds a date and time and can read dates and times in from
 * properly formatted Strings and raw data
 */
public final class RatDateTime implements Comparable<RatDateTime>, Serializable{

    private RatDate date;
    private RatTime time;

    private RatDateTime(RatDate date, RatTime time) {
        this.date = date;
        this.time = time;
    }

    private RatDateTime(String dateTimeDef) {
        date = RatDate.forDate(dateTimeDef);
        time = RatTime.forTime(dateTimeDef);
    }

    private RatDateTime(int year, int month, int day, int hour, int minute, int second, Period period) {
        date = RatDate.forDate(year, month, day);
        time = RatTime.forTime(hour, minute, second, period);
    }

    /**
     * Creates a new RatDateTime from the passed String, returns
     * null if no date and time were found in the String
     * @param possibleDateTimeDef the String to pull the date and
     *                            time from
     * @return the new RatDateTime
     */
    public static RatDateTime forDateTime(String possibleDateTimeDef) {
        if (RatDate.isDate(possibleDateTimeDef) && RatTime.isTime(possibleDateTimeDef)) {
            return new RatDateTime(possibleDateTimeDef);
        }
        return null;
    }

    /**
     * Creates a new RatDateTime from the passed data
     * @param year the year
     * @param month the month
     * @param day the day
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @param period the period
     * @return the new RatDateTime
     */
    public static RatDateTime forDateTime(int year, int month, int day, int hour, int minute, int second, Period period) {
        return new RatDateTime(year, month, day, hour, minute, second, period);
    }

    /**
     * Builds a new RatDateTime from the passed RatDateTime, effectively cloning it
     * @param r the RatDateTime to build a new RatDateTime from
     * @return the new RatDateTime
     */
    public static RatDateTime forDateTime(RatDateTime r) {
        if (r != null) {
            return new RatDateTime(RatDate.forDate(r.getDate()), RatTime.forTime(r.getTime()));
        }
        return null;
    }

    /**
     * Checks if the passed String contains a date and time
     * @param possibleDateTimeDef the String to check
     * @return true if it contains a date and time, false otherwise
     */
    public static boolean isDateTime(String possibleDateTimeDef) {
        return RatDate.isDate(possibleDateTimeDef) && RatTime.isTime(possibleDateTimeDef);
    }

    /**
     * Getter for date
     * @return the date
     */
    public RatDate getDate() {
        return date;
    }

    /**
     * Getter for time
     * @return the time
     */
    public RatTime getTime() {
        return time;
    }

    /**
     * Getter for year
     * @return the year
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Getter for month
     * @return the month
     */
    public int getMonth() {
        return date.getMonth();
    }

    /**
     * Getter for day
     * @return the day
     */
    public int getDay() {
        return date.getDay();
    }

    /**
     * Getter for hours
     * @return the hours
     */
    public int getHours() {
        return time.getHours();
    }

    /**
     * Getter for military time
     * @return the 24-hour
     */
    public int get24Hours() {
        return time.get24Hours();
    }

    /**
     * Getter for minutes
     * @return the minutes
     */
    public int getMinutes() {
        return time.getMinutes();
    }

    /**
     * Getter for seconds
     * @return the seconds
     */
    public int getSeconds() {
        return time.getSeconds();
    }

    /**
     * Getter for period
     * @return the period
     */
    public Period getPeriod() {
        return time.getPeriod();
    }

    /**
     * Setter for date
     * @param date new date
     */
    public void setDate(RatDate date) {
        this.date = date;
    }

    /**
     * Setter for time
     * @param time new time
     */
    public void setTime(RatTime time) {
        this.time = time;
    }

    /**
     * Setter for year
     * @param year new year
     */
    public void setYear(int year) {
        date.setYear(year);
    }

    /**
     * Setter for month
     * @param month new month
     */
    public void setMonth(int month) {
        date.setMonth(month);
    }

    /**
     * Setter for day
     * @param day new day
     */
    public void setDay(int day) {
        date.setDay(day);
    }

    /**
     * Setter for hours
     * @param hours new hours
     */
    public void setHours(int hours) {
        time.setHours(hours);
    }

    /**
     * Setter for minutes
     * @param minutes new minutes
     */
    public void setMinutes(int minutes) {
        time.setMinutes(minutes);
    }

    /**
     * Setter for seconds
     * @param seconds new seconds
     */
    public void setSeconds(int seconds) {
        time.setSeconds(seconds);
    }

    /**
     * Setter for period
     * @param period new period
     */
    public void setPeriod(Period period) {
        time.setPeriod(period);
    }

    @Override
    public int compareTo(@NonNull RatDateTime ratDateTime) {
        int dateComp = date.compareTo(ratDateTime.getDate());
        if (dateComp == 0) {
            return time.compareTo(ratDateTime.getTime());
        }
        return dateComp;
    }

    @Override
    public String toString() {
        return date.toString() + " " + time.toString();
    }
}