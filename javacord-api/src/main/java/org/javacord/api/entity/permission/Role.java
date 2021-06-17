package org.javacord.api.entity.permission;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.server.role.RoleAttachableListenerManager;

import java.awt.Color;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a Discord role, e.g. "moderator".
 */
public interface Role extends DiscordEntity, Mentionable, Nameable, Permissionable, Comparable<Role>,
        UpdatableFromCache<Role>, RoleAttachableListenerManager {

    /**
     * Gets the server of the role.
     *
     * @return The server of the role.
     */
    Server getServer();

    /**
     * Gets the role tags of the role.
     *
     * @return The role tags of the role.
     */
    Optional<RoleTags> getRoleTags();

    /**
     * Gets the real position of the role.
     *
     * <p>Will return <code>-1</code> if the Role got deleted.
     *
     * @return The real position of the role.
     */
    default int getPosition() {
        return getServer().getRoles().indexOf(this);
    }

    /**
     * Gets the raw position of the role.
     *
     * <p>This is the position that gets send by discord. It might not be unique and there might be a gap between
     * roles.
     *
     * @return The raw position of the role.
     */
    int getRawPosition();

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
     * Checks whether the specified users has this role.
     *
     * @param user the user to check
     * @return true if the user has this role; false otherwise
     */
    boolean hasUser(User user);

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
        return isEveryoneRole() ? "@everyone" : "<@&" + getIdAsString() + ">";
    }

    @Override
    default Optional<Role> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getRoleById(getId()));
    }

}