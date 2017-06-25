package de.btobastian.javacord.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * This class is used to parse snowflake ids.
 */
public class SnowflakeUtil {

    private SnowflakeUtil() {}

    /**
     * Parses a (discord) snowflake id to get the creation date.
     *
     * @param id The snowflake id.
     * @return The date when the snowflake was created.
     */
    public static Calendar parseDate(String id) {
        long timestamp;
        try {
            timestamp = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given string is not a number!");
        }

        // The first 42 bits (of the total 64) are the timestamp
        timestamp = timestamp >> 22;
        // Discord starts its counter at the first second of 2015
        timestamp += 1420070400000L;
        Date date = new Date(timestamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
