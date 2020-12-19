package org.javacord.api.command;

import java.util.List;

/**
 * An application command's option (i.e., a parameter for the command).
 */
public interface ApplicationCommandOption {

    /**
     * Gets the type of this option.
     *
     * @return The type.
     */
    ApplicationCommandOptionType getType();

    /**
     * Gets the name of this option.
     *
     * @return The name of this option.
     */
    String getName();

    /**
     * Gets the description of this option.
     *
     * @return The description of this option.
     */
    String getDescription();

    /**
     * Checks whether or not this option is required.
     *
     * @return Whether or not this option is required.
     */
    boolean isRequired();

    /**
     * Gets a list with all choices for this option.
     *
     * <p>If this option has any choices, they are the only valid values for a user to pick.
     *
     * @return A list with all choices for this option.
     */
    List<ApplicationCommandOptionChoice> getChoices();

    /**
     * If this option is a subcommand or subcommand group type, this nested options will be the parameters.
     *
     * @return A list with the nested options.
     */
    List<ApplicationCommandOption> getOptions();

}
