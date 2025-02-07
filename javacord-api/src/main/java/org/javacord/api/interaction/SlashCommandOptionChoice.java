package org.javacord.api.interaction;

import java.util.Map;
import java.util.Optional;

/**
 * A choice for a slash command option.
 */
public interface SlashCommandOptionChoice {

    /**
     * Gets the name of this choice.
     *
     * @return The name of the choice.
     */
    String getName();

    /**
     * Gets the name localizations for this choice.
     *
     * @return The name localizations for this choice.
     */
    Map<DiscordLocale, String> getNameLocalizations();

    /**
     * Gets the string value of this choice.
     *
     * <p>If this option is an integer or long choice, the optional will be empty.
     *
     * @return The string value of this choice.
     */
    Optional<String> getStringValue();

    /**
     * Gets the long value of this choice.
     *
     * <p>If this option is a string or double choice, the optional will be empty.
     *
     * @return The long value of this choice.
     */
    Optional<Long> getLongValue();

    /**
     * Gets the double value of this choice.
     *
     * <p>If this option is a string or long choice, the optional will be empty.
     *
     * @return The double value of this choice.
     */
    Optional<Double> getDoubleValue();

    /**
     * Gets the value of this choice as a string.
     *
     * <p>If the value is an integer, its string representation will be returned.
     *
     * @return The value of the choice as a string.
     */
    default String getValueAsString() {
        return getStringValue().orElseGet(() -> getLongValue()
            .map(String::valueOf)
            .orElseThrow(() ->
                new AssertionError("slash command option choice value that's neither a string nor int")));
    }

    /**
     * Create a new option choice builder to be used with a command option builder.
     *
     * @param name The name of the choice.
     * @param value The value of the choice.
     * @return The new choice builder.
     */
    static SlashCommandOptionChoice create(String name, String value) {
        return new SlashCommandOptionChoiceBuilder()
            .setName(name)
            .setValue(value)
            .build();
    }

    /**
     * Create a new option choice builder to be used with a command option builder.
     *
     * @param name The name of the choice.
     * @param value The value of the choice. Can be any long between -2^53 and 2^53.
     * @return The new choice builder.
     */
    static SlashCommandOptionChoice create(String name, long value) {
        return new SlashCommandOptionChoiceBuilder()
            .setName(name)
            .setValue(value)
            .build();
    }

    /**
     * Create a new option choice builder to be used with a command option builder.
     *
     * @param name The name of the choice.
     * @param value The value of the choice. Can be any double between -2^53 and 2^53.
     * @return The new choice builder.
     */
    static SlashCommandOptionChoice create(String name, double value) {
        return new SlashCommandOptionChoiceBuilder()
            .setName(name)
            .setValue(value)
            .build();
    }
}
