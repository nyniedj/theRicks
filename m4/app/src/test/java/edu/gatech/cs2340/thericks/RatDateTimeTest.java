
package edu.gatech.cs2340.thericks;

import org.junit.Test;

import edu.gatech.cs2340.thericks.models.Period;
import edu.gatech.cs2340.thericks.models.RatDate;
import edu.gatech.cs2340.thericks.models.RatDateTime;
import edu.gatech.cs2340.thericks.models.RatTime;

import static org.junit.Assert.*;

/**
 * Created by Cameron on 10/31/2017.
 * Tests the RatDate, RatTime, and RatDateTime classes. It is necessary
 * to test both RatDate and RatTime in order to test RatDateTime due to
 * its heavy reliance on both classes. A failure in either would result
 * in a failure in RatDateTime
 */
public class RatDateTimeTest {

    @Test
    public void testRatTime() {
        RatTime time1 = RatTime.forTime("10:12:09 AM");
        assertNotNull(time1);
        assertEquals(10, time1.getHours());
        assertEquals(12, time1.getMinutes());
        assertEquals(9, time1.getSeconds());
        assertEquals(Period.AM, time1.getPeriod());

        RatTime time2 = RatTime.forTime("03:23:22 PM");
        assertNotNull(time2);
        assertEquals(15, time2.get24Hours());
        assertEquals(3, time2.getHours());
        assertEquals(Period.PM, time2.getPeriod());
        assertEquals("03:23:22 PM", time2.toString());

        RatTime time3 = RatTime.forTime(2, 23, 33, Period.AM);
        assertEquals("02:23:33 AM", time3.toString());

        assertTrue(RatTime.isTime("12:03:03 AM"));
        assertFalse(RatTime.isTime("12:22:22 FM"));

        assertTrue(time3.compareTo(time2) < 0);
        assertTrue(time1.compareTo(time3) > 0);
    }

    @Test
    public void testRatDate() {
        RatDate date1 = RatDate.forDate("10/31/2017");
        assertNotNull(date1);
        assertEquals(2017, date1.getYear());
        assertEquals(10, date1.getMonth());
        assertEquals(31, date1.getDay());
        assertEquals("10/31/2017", date1.toString());

        RatDate date2 = RatDate.forDate(1990, 1, 21);
        assertEquals(1990, date2.getYear());
        assertEquals(1, date2.getMonth());
        assertEquals(21, date2.getDay());
        assertEquals("01/21/1990", date2.toString());

        assertTrue(RatDate.isDate("01/02/2007"));

        assertTrue(date2.compareTo(date1) < 0);
    }

    @Test
    public void testRatDateTime() {
        RatDateTime dateTime1 = RatDateTime.forDateTime("03/21/2001 12:01:48 PM");
        assertNotNull(dateTime1);
        assertNotNull(dateTime1.getDate());
        assertNotNull(dateTime1.getTime());
        assertEquals(2001, dateTime1.getYear());
        assertEquals(3, dateTime1.getMonth());
        assertEquals(21, dateTime1.getDay());
        assertEquals(12, dateTime1.getHours());
        assertEquals(1, dateTime1.getMinutes());
        assertEquals(48, dateTime1.getSeconds());
        assertEquals("03/21/2001 12:01:48 PM", dateTime1.toString());

        RatDateTime dateTime2 = RatDateTime.forDateTime();
        assertEquals("08/05/2010 09:30:32 PM", dateTime2.toString());

        assertTrue(dateTime1.compareTo(dateTime2) < 0);

        assertTrue(RatDateTime.isDateTime());
    }
}
