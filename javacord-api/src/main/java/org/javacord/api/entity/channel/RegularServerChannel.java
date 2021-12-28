package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface RegularServerChannel extends ServerChannel, Comparable<RegularServerChannel> {

    /**
     * Gets the raw position of the channel.
     *
     * <p>This is the positions sent from Discord and might not be unique and have gaps.
     * Also, every channel type (text, voice and category) has its own position counter.
     *
     * @return The raw position of the channel.
     */
    int getRawPosition();

    /**
     * Gets the real position of the channel.
     *
     * <p>Returns <code>-1</code> if the channel is deleted.
     *
     * @return The real position of the channel.
     */
    default int getPosition() {
        return getServer().getChannels().indexOf(this);
    }

    /**
     * Updates the raw position of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link RegularServerChannel#getRawPosition()} instead of
     *                    {@link RegularServerChannel#getPosition()}!
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRawPosition(final int rawPosition) {
        return createUpdater().setRawPosition(rawPosition).update();
    }

    /**
     * Checks if the given user can create an instant invite to this channel.
     *
     * @param user The user to check.
     * @return Whether the given user can create an instant invite or not.
     */
    default boolean canCreateInstantInvite(User user) {
        // The user must be able to see the channel
        if (!canSee(user)) {
            return false;
        }
        // You cannot create invites for categories
        if (getType() == ChannelType.CHANNEL_CATEGORY) {
            return false;
        }
        // The user must be admin or have the CREATE_INSTANT_INVITE permission
        return hasAnyPermission(user,
                PermissionType.ADMINISTRATOR,
                PermissionType.CREATE_INSTANT_INVITE);
    }

    /**
     * Checks if the user of the connected account can create an instant invite to this channel.
     *
     * @return Whether the user of the connected account can create an instant invite or not.
     */
    default boolean canYouCreateInstantInvite() {
        return canCreateInstantInvite(getApi().getYourself());
    }

    /**
     * Gets the overwritten permissions of an entity in this channel.
     *
     * @param <T>            The type of permissionable discord entity.
     * @param permissionable The permissionable entity.
     * @return The overwritten permissions of an entity.
     */
    <T extends Permissionable & DiscordEntity> Permissions getOverwrittenPermissions(T permissionable);

    /**
     * Gets the overwritten permissions in this channel.
     *
     * @return The overwritten permissions.
     */
    default Map<Long, Permissions> getOverwrittenPermissions() {
        Map<Long, Permissions> result = new HashMap<>();
        result.putAll(getOverwrittenRolePermissions());
        result.putAll(getOverwrittenUserPermissions());
        return Collections.unmodifiableMap(result);
    }

    /**
     * Gets the overwritten permissions for users in this channel.
     *
     * @return The overwritten permissions for users.
     */
    Map<Long, Permissions> getOverwrittenUserPermissions();

    /**
     * Gets the overwritten permissions for roles in this channel.
     *
     * @return The overwritten permissions for roles.
     */
    Map<Long, Permissions> getOverwrittenRolePermissions();

    /**
     * Gets the effective overwritten permissions of a user.
     * This method also takes into account the roles of the user.
     * It doesn't take into account the "global" permissions!
     *
     * @param user The user.
     * @return The effective overwritten permissions of the user.
     */
    Permissions getEffectiveOverwrittenPermissions(User user);

    /**
     * Gets the effective permissions of a user in this channel.
     * The returned permission object will only have {@link PermissionState#ALLOWED} and
     * {@link PermissionState#DENIED} states!
     * It takes into account global permissions and the effective overwritten permissions of a user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user The user.
     * @return The effective permissions of the user in this channel.
     */
    default Permissions getEffectivePermissions(User user) {
        if (getServer().isOwner(user)) {
            return getServer().getPermissions(user);
        }
        PermissionsBuilder builder = new PermissionsBuilder(getServer().getPermissions(user));
        Permissions effectiveOverwrittenPermissions = getEffectiveOverwrittenPermissions(user);
        Arrays.stream(PermissionType.values())
                .filter(type -> effectiveOverwrittenPermissions.getState(type) != PermissionState.UNSET)
                .forEachOrdered(type -> builder.setState(type, effectiveOverwrittenPermissions.getState(type)));
        Arrays.stream(PermissionType.values())
                .filter(type -> builder.getState(type) == PermissionState.UNSET)
                .forEachOrdered(type -> builder.setState(type, PermissionState.DENIED));
        return builder.build();
    }

    /**
     * Gets the effective allowed permissions of a user in this channel.
     * It takes into account global permissions and the effective overwritten permissions of a user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user The user.
     * @return The effective allowed permissions of a user in this channel.
     */
    default Collection<PermissionType> getEffectiveAllowedPermissions(User user) {
        return getEffectivePermissions(user).getAllowedPermission();
    }

    /**
     * Gets the effective denied permissions of a user in this channel.
     * It takes into account global permissions and the effective overwritten permissions of a user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user The user.
     * @return The effective denied permissions of a user in this channel.
     */
    default Collection<PermissionType> getEffectiveDeniedPermissions(User user) {
        return getEffectivePermissions(user).getDeniedPermissions();
    }

    /**
     * Checks if the user has a given set of permissions.
     *
     * @param user The user to check.
     * @param type The permission type(s) to check.
     * @return Whether the user has all given permissions or not.
     * @see #getEffectiveAllowedPermissions(User)
     */
    default boolean hasPermissions(User user, PermissionType... type) {
        return getEffectiveAllowedPermissions(user).containsAll(Arrays.asList(type));
    }

    /**
     * Checks if the user has any of a given set of permissions.
     *
     * @param user The user to check.
     * @param type The permission type(s) to check.
     * @return Whether the user has any of the given permissions or not.
     * @see #getEffectiveAllowedPermissions(User)
     */
    default boolean hasAnyPermission(User user, PermissionType... type) {
        return getEffectiveAllowedPermissions(user).stream().anyMatch(
                allowedPermissionType -> Arrays.stream(type).anyMatch(allowedPermissionType::equals)
        );
    }

    /**
     * Checks if a user has a given permission.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user       The user.
     * @param permission The permission to check.
     * @return Whether the user has the permission or not.
     */
    default boolean hasPermission(User user, PermissionType permission) {
        return getEffectiveAllowedPermissions(user).contains(permission);
    }

    /**
     * {@inheritDoc}
     *
     * <p><b><i>Implementation note:</i></b> Only channels from the same server can be compared
     *
     * @throws IllegalArgumentException If the channels are on different servers.
     */
    @Override
    default int compareTo(final RegularServerChannel channel) {
        if (!getServer().equals(channel.getServer())) {
            throw new IllegalArgumentException("Only channels from the same server can be compared for order");
        }
        return getPosition() - channel.getPosition();
    }

    /**
     * Create an updater for this channel.
     *
     * @return An updater for this channel.
     */
    @Override
    default RegularServerChannelUpdater createUpdater() {
        return new RegularServerChannelUpdater(this);
    }

    @Override
    default Optional<? extends RegularServerChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getChannelById(getId()))
                .flatMap(Channel::asRegularServerChannel);
    }

    @Override
    default CompletableFuture<? extends RegularServerChannel> getLatestInstance() {
        Optional<? extends RegularServerChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<? extends RegularServerChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }
}
