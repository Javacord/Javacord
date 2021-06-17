package org.javacord.api.interaction;

public interface ApplicationCommandInteraction
        extends InteractionBase, ApplicationCommandInteractionOptionsProvider {
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
}
