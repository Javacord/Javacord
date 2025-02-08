package org.javacord.api.entity.channel.forum;

public enum SortOrderType {
    /**
     * Sorts the forum posts by activity.
     */
    LATEST_ACTIVITY(0),
    /**
     * Sorts the forum posts by creation date.
     */
    CREATION_DATE(1),
    /**
     * An unknown sort order type.
     */
    UNKNOWN(-1);

    private final int value;

    SortOrderType(int value) {
        this.value = value;
    }

    /**
     * Gets the value of the sort order type.
     *
     * @return The value of the sort order type.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the sort order type by its value.
     *
     * @param value The value of the sort order type.
     * @return The sort order type with the given value.
     */
    public static SortOrderType getByValue(int value) {
        for (SortOrderType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
