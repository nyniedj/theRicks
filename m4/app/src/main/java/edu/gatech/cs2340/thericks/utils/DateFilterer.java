package edu.gatech.cs2340.thericks.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    /**
     * Returns a Predicate that filters Dates in the specified range
     * @param begin the start date
     * @param end the end date
     * @return the predicate
     */
    public static Predicate<RatData> createDateRangeFilter(Date begin, Date end) {
        return ratData -> {
            Date d = parse(ratData.getCreatedDateTime());
<<<<<<< HEAD
            return d != null && (begin == null || !d.before(begin)) && (end == null || !d.after(end));
=======
            return d != null && (begin == null || d.after(begin)) && (end == null || d.before(end));
>>>>>>> 51527a947f3f1049d498852f88420dc35104f7c2
        };
    }

    /**
     * Filters the given List by the given Dats
     * @param begin the start date
     * @param end the end date
     * @param data the list
     * @return a list containing the RatData between the specified dates
     */
    public static List<RatData> filterByDate(Date begin, Date end, List<RatData> data) {
        List<RatData> filteredList = new ArrayList<RatData>();
        Predicate<RatData> predicate = createDateRangeFilter(begin, end);
        filteredList = data.stream().filter(predicate).collect(Collectors.toList());
        return filteredList;
    }
}
