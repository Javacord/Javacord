package org.javacord.api.command;

import java.util.List;
import java.util.Optional;

public interface ApplicationCommandInteractionDataOption {

    /**
     * Gets the name of the option.
     *
     * @return The name.
     */
    String getName();

    /**
     * Checks if the option is a subcommand or group.
     *
     * <p>If the option is a subcommand or group, it does have options but no value.
     * If the option is not a subcommand or group, it does have a value but no options.
     *
     * @return If the option is a subcommand or group.
     */
    default boolean isSubcommandOrGroup() {
        return !getValueAsString().isPresent();
    }

    /**
     * Gets the string value of this choice.
     *
     * <p>If this option is an integer or the option itself is a subcommand or group, the optional will be empty.
     *
     * @return The string value of this choice.
     */
    Optional<String> getStringValue();

    /**
     * Gets the integer value of this choice.
     *
     * <p>If this option is a string or the option itself is a subcommand or group, the optional will be empty.
     *
     * @return The integer value of this choice.
     */
    Optional<Integer> getIntValue();

    /**
     * Gets the value of this choice as a string.
     *
     * <p>If this option itself is a subcommand or group, the optional will be empty.
     * In this case, the {@link #getOptions()} list will contain elements.
     *
     * <p>If the value is an integer, its string representation will be returned.
     *
     * @return The value of the choice as a string.
     */
    default Optional<String> getValueAsString() {
        if (getStringValue().isPresent())  {
            return getStringValue();
        } else {
            return getIntValue().map(String::valueOf);
        }
    }

    /**
     * Gets a list with all options of this option, if this option denotes a subcommand or group.
     *
     * <p>If this option does not denote a subcommand or group, the list will be empty.
     *
     * @return A list with all options.
     */
    List<ApplicationCommandInteractionDataOption> getOptions();

}
