package edu.gatech.cs2340.thericks.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Cameron on 10/31/2017.
 */
public class RatDateTime implements Comparable<RatDateTime>, Serializable{

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

    public static RatDateTime forDateTime(String possibleDateTimeDef) {
        if (RatDate.isDate(possibleDateTimeDef) && RatTime.isTime(possibleDateTimeDef)) {
            return new RatDateTime(possibleDateTimeDef);
        }
        return null;
    }

    public static RatDateTime forDateTime(int year, int month, int day, int hour, int minute, int second, Period period) {
        return new RatDateTime(year, month, day, hour, minute, second, period);
    }

    public static boolean isDateTime(String possibleDateTimeDef) {
        return RatDate.isDate(possibleDateTimeDef) && RatTime.isTime(possibleDateTimeDef);
    }

    public RatDate getDate() {
        return date;
    }

    public RatTime getTime() {
        return time;
    }

    public int getYear() {
        return date.getYear();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public int getDay() {
        return date.getDay();
    }

    public int getHours() {
        return time.getHours();
    }

    public int get24Hours() {
        return time.get24Hours();
    }

    public int getMinutes() {
        return time.getMinutes();
    }

    public int getSeconds() {
        return time.getSeconds();
    }

    public Period getPeriod() {
        return time.getPeriod();
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
