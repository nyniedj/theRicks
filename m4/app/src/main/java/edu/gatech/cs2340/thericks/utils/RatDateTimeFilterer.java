package edu.gatech.cs2340.thericks.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDateTime;

/**
 * Utility class for filtering by date and time.
 *
 * Created by Ben Lashley on 11/1/2017.
 */

public class RatDateTimeFilterer {

    /**
     * Returns a Predicate that filters Dates in the specified range
     * @param begin the start date
     * @param end the end date
     * @return the predicate
     */
    public static Predicate<RatData> createRatDateTimeRangeFilter(RatDateTime begin, RatDateTime end) {
        return ratData -> {
            RatDateTime d = RatDateTime.forDateTime(ratData.getCreatedDateTime());
            return (d != null) && ((begin == null) || (d.compareTo(begin) >= 0)) && ((end == null) || (d.compareTo(end) <= 0));
        };
    }

    /**
     * Filters the given List by the given date
     * @param begin the start date
     * @param end the end date
     * @param data the list
     * @return a list containing the RatData between the specified dates
     */
    public static Collection<RatData> filterByDate(RatDateTime begin, RatDateTime end, Collection<RatData> data) {
        List<RatData> filteredList;
        Predicate<RatData> predicate = createRatDateTimeRangeFilter(begin, end);
        filteredList = data.stream().filter(predicate).collect(Collectors.toList());
        return filteredList;
    }
}
