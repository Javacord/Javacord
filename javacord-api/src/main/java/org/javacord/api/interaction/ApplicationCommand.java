package org.javacord.api.interaction;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.Specializable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ApplicationCommand extends DiscordEntity, Specializable<ApplicationCommand> {

    /**
     * Gets the unique id of this command.
     *
     * @return The unique id of this command.
     */
    long getId();

    /**
     * Gets the unique id of the application that this command belongs to.
     *
     * @return The unique application id.
     */
    long getApplicationId();

    /**
     * Gets the type of this application command.
     *
     * @return The type of this command.
     */
    ApplicationCommandType getType();

    /**
     * Gets the name of this command.
     *
     * @return The name of this command.
     */
    String getName();

    /**
     * Gets the description of this command.
     *
     * @return The description of this command.
     */
    String getDescription();

    /**
     * Gets the default permission of this command.
     *
     * @return The default permission of this command.
     */
    boolean getDefaultPermission();

    /**
     * Gets the server of this command if it is not global.
     *
     * @return The server of this command.
     */
    Optional<Server> getServer();

    /**
     * Gets whether this application command is global.
     * @return If this application command is global or not.
     */
    boolean isGlobalApplicationCommand();

    /**
     * Gets whether this application command is a server application command.
     * @return If this application command is a server application command or not.
     */
    boolean isServerApplicationCommand();

    /**
     * Deletes this application command globally.
     *
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> deleteGlobal();

    /**
     * Deletes this application command globally.
     *
     * @param server The server where the command should be deleted from.
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> deleteForServer(Server server);
}
