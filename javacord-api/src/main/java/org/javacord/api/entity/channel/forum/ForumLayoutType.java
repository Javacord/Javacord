package org.javacord.api.entity.channel.forum;

public enum ForumLayoutType {
    /**
     * No default layout has been set.
     */
    NOT_SET(0),
    /**
     * Forum posts will be displayed as a list.
     */
    LIST_VIEW(1),
    /**
     * Forum pots will be displayed as a collection of tiles.
     */
    GALLERY_VIEW(2),
    /**
     * An unknown layout type.
     */
    UNKNOWN(-1);

    private final int value;

    ForumLayoutType(int value) {
        this.value = value;
    }

    /**
     * Gets the value of the enum.
     *
     * @return The value of the enum.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the enum by its value.
     *
     * @param value The value of the enum.
     * @return The enum with the given value.
     */
    public static ForumLayoutType getByValue(int value) {
        for (ForumLayoutType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
