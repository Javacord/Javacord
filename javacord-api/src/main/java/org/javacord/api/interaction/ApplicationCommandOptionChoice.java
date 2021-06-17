package org.javacord.api.interaction;

import java.util.Optional;

/**
 * A choice for an application command option.
 */
public interface ApplicationCommandOptionChoice {

    /**
     * Gets the name of this choice.
     *
     * @return The name of the choice.
     */
    String getName();

    /**
     * Gets the string value of this choice.
     *
     * <p>If this option is an integer choice, the optional will be empty.
     *
     * @return The string value of this choice.
     */
    Optional<String> getStringValue();

    /**
     * Gets the integer value of this choice.
     *
     * <p>If this option is an string choice, the optional will be empty.
     *
     * @return The integer value of this choice.
     */
    Optional<Integer> getIntValue();

    /**
     * Gets the value of this choice as a string.
     *
     * <p>If the value is an integer, its string representation will be returned.
     *
     * @return The value of the choice as a string.
     */
    default String getValueAsString() {
        return getStringValue().orElseGet(() -> getIntValue()
            .map(String::valueOf)
            .orElseThrow(() ->
                new AssertionError("Application command option choice value that's neither a string nor int")));
    }

    /**
     * Create a new option choice builder to be used with a command option builder.
     *
     * @param name The name of the choice.
     * @param value The value of the choice.
     * @return The new choice builder.
     */
    static ApplicationCommandOptionChoice create(String name, String value) {
        return new ApplicationCommandOptionChoiceBuilder()
            .setName(name)
            .setValue(value)
            .build();
    }

    /**
     * Create a new option choice builder to be used with a command option builder.
     *
     * @param name The name of the choice.
     * @param value The value of the choice.
     * @return The new choice builder.
     */
    static ApplicationCommandOptionChoice create(String name, int value) {
        return new ApplicationCommandOptionChoiceBuilder()
            .setName(name)
            .setValue(value)
            .build();
    }
}
