package org.javacord.api.interaction;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.Specializable;

import java.util.EnumSet;
import java.util.Map;
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
     * Gets the name localizations of this command.
     *
     * @return The name localizations of this command.
     */
    Map<DiscordLocale, String> getNameLocalizations();

    /**
     * Gets the description of this command.
     *
     * @return The description of this command.
     */
    String getDescription();

    /**
     * Gets the description localizations of this command.
     *
     * @return The description localizations of this command.
     */
    Map<DiscordLocale, String> getDescriptionLocalizations();

    /**
     * Gets the default required permissions for this command.
     * This may differ from the actual permissions of the command if they have been changed by a server administrator.
     *
     * @return The default required permissions for this command.
     */
    Optional<EnumSet<PermissionType>> getDefaultRequiredPermissions();

    /**
     * Gets whether this command is disabled and only usable by server administrators by default.
     * Note that this is different to {@link #getDefaultRequiredPermissions} returning
     * {@link PermissionType#ADMINISTRATOR}.
     *
     * @return Whether this command is disabled by default.
     */
    boolean isDisabledByDefault();

    /**
     * Gets whether this command can be used in DMs.
     * Will always return {@code false} for server commands.
     *
     * @return Whether the command is enabled in DMs
     */
    boolean isEnabledInDms();

    /**
     * Gets the server id of this command if it is not global.
     *
     * @return The server of this command.
     */
    Optional<Long> getServerId();

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
     * Whether the command is nsfw.
     *
     * @return Whether the command is nsfw.
     */
    boolean isNsfw();

    /**
     * Deletes this application command.
     *
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> delete();

    /**
     * Deletes this application command globally.
     *
     * @return A future to check if the deletion was successful.
     * @deprecated Use {@link #delete()} instead.
     */
    @Deprecated
    CompletableFuture<Void> deleteGlobal();

    /**
     * Deletes this application command for a server.
     *
     * @param server The server where the command should be deleted from.
     * @return A future to check if the deletion was successful.
     * @deprecated Use {@link #delete()} instead.
     */
    @Deprecated
    default CompletableFuture<Void> deleteForServer(Server server) {
        return deleteForServer(server.getId());
    }

    /**
     * Deletes this application command for a server.
     *
     * @param server The server where the command should be deleted from.
     * @return A future to check if the deletion was successful.
     * @deprecated Use {@link #delete()} instead.
     */
    @Deprecated
    CompletableFuture<Void> deleteForServer(long server);
}
