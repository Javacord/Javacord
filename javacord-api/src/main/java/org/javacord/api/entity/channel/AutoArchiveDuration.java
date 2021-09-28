package org.javacord.api.entity.channel;

public enum AutoArchiveDuration {

    ONE_HOUR(60),
    ONE_DAY(1440),
    THREE_DAYS(4320),
    ONE_WEEK(10080);

    private final int minutes;

    /**
     * Creates a new AutoArchiveDuration.
     *
     * @param minutes The amount of minutes.
     */
    AutoArchiveDuration(final int minutes) {
        this.minutes = minutes;
    }

    /**
     * Gets the duration in minutes.
     *
     * @return The duration in minutes.
     */
    public int asInt() {
        return minutes;
    }
}
