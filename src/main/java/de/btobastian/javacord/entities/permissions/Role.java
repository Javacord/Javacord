package de.btobastian.javacord.entities.permissions;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.role.*;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a Discord role, e.g. "moderator".
 */
public interface Role extends DiscordEntity, Mentionable {

    /**
     * Gets the server of the role.
     *
     * @return The server of the role.
     */
    Server getServer();

    /**
     * Gets the name of the role.
     *
     * @return The name of the role.
     */
    String getName();

    /**
     * Gets the position of the role.
     *
     * @return The position of the role.
     */
    int getPosition();

    /**
     * Gets the color of the role.
     *
     * @return The color of the role.
     */
    Optional<Color> getColor();

    /**
     * Check if this role is mentionable.
     *
     * @return Whether this role is mentionable or not.
     */
    boolean isMentionable();

    /**
     * Check if this role is pinned in the user listing (sometimes called "hoist").
     *
     * @return Whether this role is pinned in the user listing or not.
     */
    boolean isDisplayedSeparately();

    /**
     * Gets a collection with all users who have this role.
     *
     * @return A collection with all users who have this role.
     */
    Collection<User> getUsers();

    /**
     * Gets the permissions of the role.
     *
     * @return The permissions of the role.
     */
    Permissions getPermissions();

    /**
     * Checks if this role is managed by an integration.
     *
     * @return Whether this role is managed by an integration or not.
     */
    boolean isManaged();

    /**
     * Checks if the role is the @everyone role.
     *
     * @return Whether the role is the @everyone role or not.
     */
    default boolean isEveryoneRole() {
        return getId() == getServer().getId();
    }

    /**
     * Gets the updater for this role.
     *
     * @return The updater for this role.
     */
    default RoleUpdater getUpdater() {
        return new RoleUpdater(this);
    }

    /**
     * Updates the name of the role.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param name The new name of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return getUpdater().setName(name).update();
    }

    /**
     * Updates the permissions of the role.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param permissions The new permissions of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updatePermissions(Permissions permissions) {
        return getUpdater().setPermissions(permissions).update();
    }

    /**
     * Updates the color of the role.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param color The new color of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateColor(Color color) {
        return getUpdater().setColor(color).update();
    }

    /**
     * Updates the display separately flag of the role.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param displaySeparately The new display separately flag of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateDisplaySeparatelyFlag(boolean displaySeparately) {
        return getUpdater().setDisplaySeparatelyFlag(displaySeparately).update();
    }

    /**
     * Updates the mentionable flag of the role.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param mentionable The new mentionable flag of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateMentionableFlag(boolean mentionable) {
        return getUpdater().setMentionableFlag(mentionable).update();
    }

    /**
     * Deletes the role.
     *
     * @return A future to check if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.ROLE)
                .setUrlParameters(String.valueOf(getServer().getId()), String.valueOf(getId()))
                .execute(result -> null);
    }

    /**
     * Gets the allowed permissions of the role.
     *
     * @return The allowed permissions of the role.
     */
    default Collection<PermissionType> getAllowedPermissions() {
        return Arrays.stream(PermissionType.values())
                .filter(type -> getPermissions().getState(type) == PermissionState.ALLOWED)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the unset permissions of the role.
     *
     * @return The unset permissions of the role.
     */
    default Collection<PermissionType> getUnsetPermissions() {
        return Arrays.stream(PermissionType.values())
                .filter(type -> getPermissions().getState(type) == PermissionState.NONE)
                .collect(Collectors.toSet());
    }

    @Override
    default String getMentionTag() {
        return "<@&" + getId() + ">";
    }

    /**
     * Checks if the this role is higher than the given role.
     * Always returns <code>true</code> if the roles are on different servers.
     *
     * @param role The role to check.
     * @return Whether the this role is higher than the given role or not.
     */
    default boolean isHigherThan(Role role) {
        return role.getServer() != getServer() || role.getPosition() <= getPosition();
    }

    /**
     * Adds a listener, which listens to permission changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangePermissionsListener.class, listener);
    }

    /**
     * Gets a list with all registered role change permissions listeners.
     *
     * @return A list with all registered role change permissions listeners.
     */
    default java.util.List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangePermissionsListener.class);
    }

    /**
     * Adds a listener, which listens to position changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(
            RoleChangePositionListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Role.class, getId(), RoleChangePositionListener.class, listener);
    }

    /**
     * Gets a list with all registered role change position listeners.
     *
     * @return A list with all registered role change position listeners.
     */
    default java.util.List<RoleChangePositionListener> getRoleChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangePositionListener.class);
    }

    /**
     * Adds a listener, which listens to overwritten permission changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    default java.util.List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Role.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    /**
     * Adds a listener, which listens to this role being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Role.class, getId(), RoleDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered role delete listeners.
     *
     * @return A list with all registered role delete listeners.
     */
    default java.util.List<RoleDeleteListener> getRoleDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleDeleteListener.class);
    }

    /**
     * Adds a listener, which listens to this user being added to this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(Role.class, getId(), UserRoleAddListener.class, listener);
    }

    /**
     * Gets a list with all registered user role add listeners.
     *
     * @return A list with all registered user role add listeners.
     */
    default java.util.List<UserRoleAddListener> getUserRoleAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), UserRoleAddListener.class);
    }

    /**
     * Adds a listener, which listens to this user being removed from this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Role.class, getId(), UserRoleRemoveListener.class, listener);
    }

    /**
     * Gets a list with all registered user role remove listeners.
     *
     * @return A list with all registered user role remove listeners.
     */
    default java.util.List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), UserRoleRemoveListener.class);
    }

}
