package org.javacord.api.interaction;

import java.util.List;

public interface ApplicationCommandInteraction extends InteractionBase {
    /**
     * Gets the id of the invoked application command.
     *
     * @return The id of the invoked command.
     */
    long getCommandId();

    /**
     * Gets the id of the invoked application command as string.
     *
     * @return The id of the invoked command as string.
     */
    String getCommandIdAsString();

    /**
     * Gets the name of the invoked application command.
     *
     * @return The name of the invoked command.
     */
    String getCommandName();

    /**
     * Gets a list with the params and values from the user.
     *
     * @return A list with the params and values from the user.
     */
    List<ApplicationCommandInteractionOption> getOptions();
}
