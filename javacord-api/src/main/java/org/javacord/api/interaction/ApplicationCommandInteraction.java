package org.javacord.api.interaction;

import org.javacord.api.entity.server.Server;
import java.util.Optional;

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
     * Gets the id of the server the command was registered on.
     *
     * @return The id of the server the command was registered on.
     */
    Optional<Long> getRegisteredCommandServerId();

    /**
     * Gets the server the command was registered on.
     *
     * @return The server the command was registered on.
     */
    default Optional<Server> getRegisteredCommandServer() {
        return getRegisteredCommandServerId().flatMap(getApi()::getServerById);
    }
}
