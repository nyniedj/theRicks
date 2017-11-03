package edu.gatech.cs2340.thericks.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Utility class for filtering by date and time.
 *
 * Created by Ben Lashley on 11/1/2017.
 */

public class DateFilterer {
    private static DateFormat FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);

    public static Date parse(String input) {
        Date result = null;
        try {
            result = FORMAT.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Predicate<RatData> createDateRangeFilter(Date begin, Date end) {
        return ratData -> {
            Date d = parse(ratData.getCreatedDateTime());
            return d != null && (begin == null || !d.before(begin)) && (end == null || !d.after(end));
        };
    }
}
