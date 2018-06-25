package org.javacord.api.entity.permission;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.api.listener.server.role.RoleAttachableListener;
import org.javacord.api.listener.server.role.RoleChangeColorListener;
import org.javacord.api.listener.server.role.RoleChangeHoistListener;
import org.javacord.api.listener.server.role.RoleChangeMentionableListener;
import org.javacord.api.listener.server.role.RoleChangeNameListener;
import org.javacord.api.listener.server.role.RoleChangePermissionsListener;
import org.javacord.api.listener.server.role.RoleChangePositionListener;
import org.javacord.api.listener.server.role.RoleDeleteListener;
import org.javacord.api.listener.server.role.UserRoleAddListener;
import org.javacord.api.listener.server.role.UserRoleRemoveListener;
import org.javacord.api.util.event.ListenerManager;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a Discord role, e.g. "moderator".
 */
public interface Role extends DiscordEntity, Mentionable, Nameable, UpdatableFromCache<Role> {

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
    default RoleUpdater createUpdater() {
        return new RoleUpdater(this);
    }

    /**
     * Updates the name of the role.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param name The new name of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return createUpdater().setName(name).update();
    }

    /**
     * Updates the permissions of the role.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param permissions The new permissions of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updatePermissions(Permissions permissions) {
        return createUpdater().setPermissions(permissions).update();
    }

    /**
     * Updates the color of the role.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param color The new color of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateColor(Color color) {
        return createUpdater().setColor(color).update();
    }

    /**
     * Updates the display separately flag of the role.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param displaySeparately The new display separately flag of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateDisplaySeparatelyFlag(boolean displaySeparately) {
        return createUpdater().setDisplaySeparatelyFlag(displaySeparately).update();
    }

    /**
     * Updates the mentionable flag of the role.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link RoleUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param mentionable The new mentionable flag of the role.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateMentionableFlag(boolean mentionable) {
        return createUpdater().setMentionableFlag(mentionable).update();
    }

    /**
     * Adds the role to the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param user The user the role should be added to.
     * @return A future to check if the update was successful.
     * @see Server#addRoleToUser(User, Role)
     */
    default CompletableFuture<Void> addUser(User user) {
        return addUser(user, null);
    }

    /**
     * Adds the role to the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param user The user the role should be added to.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     * @see Server#addRoleToUser(User, Role, String)
     */
    default CompletableFuture<Void> addUser(User user, String reason) {
        return getServer().addRoleToUser(user, this, reason);
    }

    /**
     * Removes the role from the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param user The user the role should be removed from.
     * @return A future to check if the update was successful.
     * @see Server#removeRoleFromUser(User, Role)
     */
    default CompletableFuture<Void> removeUser(User user) {
        return removeUser(user, null);
    }

    /**
     * Removes the role from the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param user The user the role should be removed from.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     * @see Server#removeRoleFromUser(User, Role, String)
     */
    default CompletableFuture<Void> removeUser(User user, String reason) {
        return getServer().removeRoleFromUser(user, this, reason);
    }

    /**
     * Deletes the role.
     *
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> delete();

    /**
     * Gets the allowed permissions of the role.
     *
     * @return The allowed permissions of the role.
     */
    default Collection<PermissionType> getAllowedPermissions() {
        return getPermissions().getAllowedPermission();
    }

    /**
     * Gets the unset permissions of the role.
     *
     * @return The unset permissions of the role.
     */
    default Collection<PermissionType> getUnsetPermissions() {
        return getPermissions().getUnsetPermissions();
    }

    @Override
    default String getMentionTag() {
        return "<@&" + getIdAsString() + ">";
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
     * Adds a listener, which listens to color changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener);

    /**
     * Gets a list with all registered role change color listeners.
     *
     * @return A list with all registered role change color listeners.
     */
    List<RoleChangeColorListener> getRoleChangeColorListeners();

    /**
     * Adds a listener, which listens to hoist changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener);

    /**
     * Gets a list with all registered role change hoist listeners.
     *
     * @return A list with all registered role change hoist listeners.
     */
    List<RoleChangeHoistListener> getRoleChangeHoistListeners();

    /**
     * Adds a listener, which listens to mentionable changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener);

    /**
     * Gets a list with all registered role change mentionable listeners.
     *
     * @return A list with all registered role change mentionable listeners.
     */
    List<RoleChangeMentionableListener> getRoleChangeMentionableListeners();

    /**
     * Adds a listener, which listens to name changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener);

    /**
     * Gets a list with all registered role change name listeners.
     *
     * @return A list with all registered role change name listeners.
     */
    List<RoleChangeNameListener> getRoleChangeNameListeners();

    /**
     * Adds a listener, which listens to permission changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener);

    /**
     * Gets a list with all registered role change permissions listeners.
     *
     * @return A list with all registered role change permissions listeners.
     */
    List<RoleChangePermissionsListener> getRoleChangePermissionsListeners();

    /**
     * Adds a listener, which listens to position changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(RoleChangePositionListener listener);

    /**
     * Gets a list with all registered role change position listeners.
     *
     * @return A list with all registered role change position listeners.
     */
    java.util.List<RoleChangePositionListener> getRoleChangePositionListeners();

    /**
     * Adds a listener, which listens to overwritten permission changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
            addServerChannelChangeOverwrittenPermissionsListener(
                    ServerChannelChangeOverwrittenPermissionsListener listener);

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    java.util.List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners();

    /**
     * Adds a listener, which listens to this role being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener);

    /**
     * Gets a list with all registered role delete listeners.
     *
     * @return A list with all registered role delete listeners.
     */
    java.util.List<RoleDeleteListener> getRoleDeleteListeners();

    /**
     * Adds a listener, which listens to this user being added to this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener);

    /**
     * Gets a list with all registered user role add listeners.
     *
     * @return A list with all registered user role add listeners.
     */
    java.util.List<UserRoleAddListener> getUserRoleAddListeners();

    /**
     * Adds a listener, which listens to this user being removed from this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener);

    /**
     * Gets a list with all registered user role remove listeners.
     *
     * @return A list with all registered user role remove listeners.
     */
    java.util.List<UserRoleRemoveListener> getUserRoleRemoveListeners();

    /**
     * Adds a listener that implements one or more {@code RoleAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends RoleAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addRoleAttachableListener(T listener);

    /**
     * Removes a listener that implements one or more {@code RoleAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends RoleAttachableListener & ObjectAttachableListener> void removeRoleAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code RoleAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code RoleAttachableListener}s and their
     * assigned listener classes they listen to.
     */
    <T extends RoleAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>> getRoleAttachableListeners();

    /**
     * Removes a listener from this role.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends RoleAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

    @Override
    default Optional<Role> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getRoleById(getId()));
    }

}
