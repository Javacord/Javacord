package org.javacord.api.entity.user;

public enum NitroType {
    NONE(0),
    NITRO_CLASSIC(1),
    NITRO(2),
    NITRO_BASIC(3),
    UNKNOWN(-1);

    private final int value;

    /**
     * Creates a new nitro type.
     *
     * @param value The value of the nitro type.
     */
    NitroType(int value) {
        this.value = value;
    }

    /**
     * Gets the value of the nitro type.
     *
     * @return The value of the nitro type.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the nitro type by its value.
     *
     * @param value The value of the nitro type.
     * @return The nitro type with the given value.
     */
    public static NitroType fromValue(int value) {
        for (NitroType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
