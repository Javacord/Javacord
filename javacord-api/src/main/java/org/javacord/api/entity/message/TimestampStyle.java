package org.javacord.api.entity.message;

import java.time.Instant;

/**
 * The enum contains all different types of timestamp styles.
 */
public enum TimestampStyle {

    /**
     * Displayed as: 16:20.
     */
    SHORT_TIME("t"),

    /**
     * Displayed as: 16:20:30.
     */
    LONG_TIME("T"),

    /**
     * Displayed as: 20/04/2021.
     */
    SHORT_DATE("d"),

    /**
     * Displayed as: 20 April 2021.
     */
    LONG_DATE("D"),

    /**
     * Displayed as: 20 April 2021 16:20.
     */
    SHORT_DATE_TIME("f"),

    /**
     * Displayed as: Tuesday, 20 April 2021 16:20.
     */
    LONG_DATE_TIME("F"),

    /**
     * Displayed as: 2 months ago.
     */
    RELATIVE_TIME("R"),

    /**
     * An unknown timestamp style.
     */
    UNKNOWN("");

    /**
     * The String representing the type.
     */
    private final String timestampStyle;

    /**
     * Creates a new timestamp style.
     *
     * @param timestampStyle The String representing the type.
     */
    TimestampStyle(final String timestampStyle) {
        this.timestampStyle = timestampStyle;
    }

    /**
     * Gets the type by its int representation.
     *
     * @param timestampStyle The String representation.
     * @return The timestamp style.
     */
    public static TimestampStyle byStyle(final String timestampStyle) {
        for (final TimestampStyle value : values()) {
            if (value.timestampStyle.equals(timestampStyle)) {
                return value;
            }
        }
        return UNKNOWN;
    }

    /**
     * Gets the timestamp style name.
     *
     * @return The name of the timestamp style.
     */
    public String getTimestampStyle() {
        return timestampStyle;
    }

    /**
     * Gets the timestamp tag used in messages.
     *
     * @param epochSeconds The epoch seconds of the timestamp.
     * @return The tag of the timestamp.
     */
    public String getTimestampTag(final long epochSeconds) {
        return "<t:"
                + epochSeconds
                + ":"
                + timestampStyle
                + ">";
    }

    /**
     * Gets the timestamp tag used in messages.
     *
     * @param instant An instant of the time that should be displayed with this timestamp.
     * @return The tag of the timestamp.
     */
    public String getTimestampTag(final Instant instant) {
        return getTimestampTag(instant.getEpochSecond());
    }

}
