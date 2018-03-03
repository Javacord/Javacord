package org.javacord.entity.permission;

import org.javacord.ImplDiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Mentionable;
import org.javacord.entity.UpdatableFromCache;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.listener.server.role.RoleAttachableListener;
import org.javacord.listener.server.role.RoleChangeColorListener;
import org.javacord.listener.server.role.RoleChangeHoistListener;
import org.javacord.listener.server.role.RoleChangeMentionableListener;
import org.javacord.listener.server.role.RoleChangeNameListener;
import org.javacord.listener.server.role.RoleChangePermissionsListener;
import org.javacord.listener.server.role.RoleChangePositionListener;
import org.javacord.listener.server.role.RoleDeleteListener;
import org.javacord.listener.server.role.UserRoleAddListener;
import org.javacord.listener.server.role.UserRoleRemoveListener;
import org.javacord.util.ClassHelper;
import org.javacord.util.event.ListenerManager;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Mentionable;
import org.javacord.entity.UpdatableFromCache;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a Discord role, e.g. "moderator".
 */
public interface Role extends DiscordEntity, Mentionable, UpdatableFromCache<Role> {

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
                .setUrlParameters(getServer().getIdAsString(), getIdAsString())
                .execute(result -> null);
    }

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
    default ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeColorListener.class, listener);
    }

    /**
     * Gets a list with all registered role change color listeners.
     *
     * @return A list with all registered role change color listeners.
     */
    default List<RoleChangeColorListener> getRoleChangeColorListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeColorListener.class);
    }

    /**
     * Adds a listener, which listens to hoist changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeHoistListener.class, listener);
    }

    /**
     * Gets a list with all registered role change hoist listeners.
     *
     * @return A list with all registered role change hoist listeners.
     */
    default List<RoleChangeHoistListener> getRoleChangeHoistListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeHoistListener.class);
    }

    /**
     * Adds a listener, which listens to mentionable changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeMentionableListener.class, listener);
    }

    /**
     * Gets a list with all registered role change mentionable listeners.
     *
     * @return A list with all registered role change mentionable listeners.
     */
    default List<RoleChangeMentionableListener> getRoleChangeMentionableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeMentionableListener.class);
    }

    /**
     * Adds a listener, which listens to name changes of this role.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered role change name listeners.
     *
     * @return A list with all registered role change name listeners.
     */
    default List<RoleChangeNameListener> getRoleChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeNameListener.class);
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
    default List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
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
    @SuppressWarnings("unchecked")
    default <T extends RoleAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addRoleAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(RoleAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(Role.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code RoleAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends RoleAttachableListener & ObjectAttachableListener> void removeRoleAttachableListener(
            T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(RoleAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(Role.class, getId(),
                                                                                           listenerClass, listener));
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code RoleAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code RoleAttachableListener}s and their
     * assigned listener classes they listen to.
     */
    default <T extends RoleAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getRoleAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId());
    }

    /**
     * Removes a listener from this role.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends RoleAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(Role.class, getId(), listenerClass, listener);
    }

    @Override
    default Optional<Role> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getRoleById(getId()));
    }

}
