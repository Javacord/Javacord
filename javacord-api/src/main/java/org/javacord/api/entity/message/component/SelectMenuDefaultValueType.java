package org.javacord.api.entity.message.component;

import java.util.Arrays;

public enum SelectMenuDefaultValueType {

    USER("user"),
    ROLE("role"),
    CHANNEL("channel");

    private final String jsonName;

    SelectMenuDefaultValueType(final String jsonName) {
        this.jsonName = jsonName;
    }

    /**
     * Get the string that is being put into JSON in order to communicate with Discord API.
     *
     * @return The type name that is being put into JSON.
     */
    public String getJsonName() {
        return this.jsonName;
    }

    /**
     * Convert a string to the SelectMenuDefaultValueType enum value.
     *
     * @param type The string to be converted. Should be either "user", "role" or "channel".
     * @return The SelectMenuDefaultValueType enum value after conversion.
     * @throws IllegalArgumentException If type is not "user", "role" or "channel".
     */
    public static SelectMenuDefaultValueType fromString(String type) {
        return Arrays
                .stream(SelectMenuDefaultValueType.values())
                .filter(defaultValueType -> defaultValueType.getJsonName().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Select menu default value type should be either \"user\", \"role\", or \"channel\".")
                );
    }
}
