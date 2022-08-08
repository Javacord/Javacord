package org.javacord.api.entity.member;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.server.member.ServerMemberAttachableListenerManager;
import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A member of a server.
 */
public interface Member extends DiscordEntity, Messageable, Mentionable, Permissionable,
        ServerMemberAttachableListenerManager {

    @Override
    default String getMentionTag() {
        return "<@!" + getIdAsString() + ">";
    }

    /**
     * Gets the display name of this member.
     *
     * @return The display name of this member.
     */
    default String getDisplayName() {
        return getNickname().orElse(getUser().getName());
    }

    /**
     * Gets the server, this user is a part of.
     *
     * @return The server.
     */
    Server getServer();

    /**
     * Gets the user object linked to this member.
     *
     * @return The user.
     */
    User getUser();

    /**
     * Gets the nickname of this member.
     *
     * @return The nickname of this member.
     */
    Optional<String> getNickname();

    /**
     * Gets a sorted list (by position) with all roles of this member.
     *
     * @return A sorted list (by position) with all roles of this member.
     */
    List<Role> getRoles();

    /**
     * Checks if this member has the given role.
     *
     * @param role The role to check.
     * @return Whether the member has the role or not.
     */
    boolean hasRole(Role role);

    /**
     * Gets the displayed color of this member based on his roles.
     *
     * @return The color.
     */
    Optional<Color> getRoleColor();

    /**
     * Gets the member's server avatar hash.
     *
     * @return The member's server avatar hash.
     */
    Optional<String> getServerAvatarHash();

    /**
     * Gets the member's server avatar.
     *
     * @return The member's server avatar.
     */
    Optional<Icon> getServerAvatar();

    /**
     * Gets the member's server avatar in the given size.
     *
     * @param size The size of the image, must be any power of 2 between 16 and 4096.
     * @return The member's server avatar in the given size.
     */
    Optional<Icon> getServerAvatar(int size);

    /**
     * Gets the timestamp of when this member joined the server.
     *
     * @return The timestamp of when this member joined the server.
     */
    Instant getJoinedAtTimestamp();

    /**
     * Gets the timestamp of when this member started boosting the server.
     *
     * @return The timestamp of when this member started boosting joined the server.
     */
    Optional<Instant> getServerBoostingSinceTimestamp();

    /**
     * Gets the muted state of this member.
     *
     * @return Whether this member is muted.
     */
    boolean isMuted();

    /**
     * Gets the deafened state of this member.
     *
     * @return Whether this member is deafened.
     */
    boolean isDeafened();

    /**
     * Gets the self-muted state of this member.
     *
     * @return Whether this member is self-muted.
     */
    boolean isSelfMuted();

    /**
     * Gets the self-deafened state of this member.
     *
     * @return Whether this member is self-deafened.
     */
    boolean isSelfDeafened();

    /**
     * Gets the pending state of this member.
     *
     * @return Whether this user has passed the membership screening.
     */
    boolean isPending();

    /**
     * Gets the timestamp of the timeout when the timeout will expire
     * and the user will be able to communicate in the server again.
     * The returned Instant may be in the past which indicates that the user is not timed out.
     *
     * @return The timestamp of when this member will no longer be timed out.
     */
    Optional<Instant> getTimeout();


    /**
     * Checks if the given user is an administrator of the server.
     *
     * @return Whether the given user is an administrator of the server or not.
     */
    default boolean isAdmin() {
        return hasPermission(PermissionType.ADMINISTRATOR);
    }

    /**
     * Get the allowed permissions of a given user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#VIEW_CHANNEL} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @return The allowed permissions of the given user.
     */
    default Set<PermissionType> getAllowedPermissions() {
        Set<PermissionType> allowed = new HashSet<>();
        if (getServer().isOwner(getUser())) {
            allowed.addAll(Arrays.asList(PermissionType.values()));
        } else {
            getRoles().forEach(role -> allowed.addAll(role.getAllowedPermissions()));
        }
        return Collections.unmodifiableSet(allowed);
    }

    /**
     * Get the unset permissions of a given user.
     *
     * @return The unset permissions of the given user.
     */
    default Set<PermissionType> getUnsetPermissions() {
        if (getServer().isOwner(getUser())) {
            return Collections.emptySet();
        }
        Set<PermissionType> unset = new HashSet<>();
        getRoles().forEach(role -> unset.addAll(role.getUnsetPermissions()));
        return Collections.unmodifiableSet(unset);
    }

    /**
     * Checks if a user has a given permission.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#VIEW_CHANNEL} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     * This method also do not take into account overwritten permissions in some channels!
     *
     * @param permission The permission to check.
     * @return Whether the user has the permission or not.
     */
    default boolean hasPermission(PermissionType permission) {
        return getAllowedPermissions().contains(permission);
    }

    /**
     * Checks if the user has a given set of permissions.
     *
     * @param type The permission type(s) to check.
     * @return Whether the user has all given permissions or not.
     * @see #getAllowedPermissions()
     */
    default boolean hasPermissions(PermissionType... type) {
        return getAllowedPermissions().containsAll(Arrays.asList(type));
    }

    /**
     * Checks if the user has any of a given set of permissions.
     *
     * @param type The permission type(s) to check.
     * @return Whether the user has any of the given permissions or not.
     * @see #getAllowedPermissions()
     */
    default boolean hasAnyPermission(PermissionType... type) {
        return getAllowedPermissions().stream()
                .anyMatch(allowedPermissionType -> Arrays.asList(type).contains(allowedPermissionType));
    }

    /**
     * Gets the permissions of a user.
     *
     * @return The permissions of the user.
     */
    default Permissions getPermissions() {
        PermissionsBuilder builder = new PermissionsBuilder();
        getAllowedPermissions().forEach(type -> builder.setState(type, PermissionState.ALLOWED));
        return builder.build();
    }

    /**
     * Gets the highest role of the given user in this server.
     * The optional is empty, if the user is not a member of this server.
     *
     * @return The highest role of the given user.
     */
    default Optional<Role> getHighestRole() {
        List<Role> roles = getRoles();
        if (roles.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(roles.get(roles.size() - 1));
    }

    /**
     * Checks if the given user can kick the other user.
     * This method also considers the position of the user roles and the owner.
     * If the to-be-kicked user is not part of this server, still {@code true} is returned as Discord allows this.
     *
     * @param userToKick The user which should be kicked.
     * @return Whether the given user can kick the other user or not.
     */
    default boolean canKickUser(Member userToKick) {
        // owner can always kick, regardless of role for example
        if (getServer().isOwner(getUser())) {
            return true;
        }
        // user cannot kick at all
        if (!canKickUsers()) {
            return false;
        }
        // only returns empty optional if user is not member of server
        // but then canKickUsers should already have kicked in
        Role ownRole = getHighestRole().orElseThrow(AssertionError::new);
        Optional<Role> otherRole = userToKick.getHighestRole();
        // otherRole empty => userToKick is not on the server => kick is allowed as Discord allows it
        boolean userToKickOnServer = otherRole.isPresent();
        return !userToKickOnServer || (ownRole.compareTo(otherRole.get()) > 0);
    }

    /**
     * Checks if the given user can ban the other user.
     * This method also considers the position of the user roles and the owner.
     *
     * @param userToBan The user which should be banned.
     * @return Whether the given user can ban the other user or not.
     */
    default boolean canBanUser(Member userToBan) {
        // owner can always ban, regardless of role for example
        if (getServer().isOwner(getUser())) {
            return true;
        }
        // user cannot ban at all
        if (!canBanUsers()) {
            return false;
        }
        // only returns empty optional if user is not member of server
        // but then canBanUsers should already have kicked in
        Role ownRole = getHighestRole().orElseThrow(AssertionError::new);
        Optional<Role> otherRole = userToBan.getHighestRole();
        // otherRole empty => userToBan is not on the server => ban is allowed
        boolean userToBanOnServer = otherRole.isPresent();
        return !userToBanOnServer || (ownRole.compareTo(otherRole.get()) > 0);
    }

    /**
     * Checks if the given user can create new channels.
     *
     * @return Whether the given user can create channels or not.
     */
    default boolean canCreateChannels() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.MANAGE_CHANNELS);
    }

    /**
     * Checks if the given user can view the audit log of the server.
     *
     * @return Whether the given user can view the audit log or not.
     */
    default boolean canViewAuditLog() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.VIEW_AUDIT_LOG);
    }

    /**
     * Checks if the given user can change its own nickname in the server.
     *
     * @return Whether the given user can change its own nickname or not.
     */
    default boolean canChangeOwnNickname() {
        return hasAnyPermission(
                PermissionType.ADMINISTRATOR,
                PermissionType.CHANGE_NICKNAME,
                PermissionType.MANAGE_NICKNAMES);
    }

    /**
     * Checks if the given user can manage nicknames on the server.
     *
     * @return Whether the given user can manage nicknames or not.
     */
    default boolean canManageNicknames() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.MANAGE_NICKNAMES);
    }

    /**
     * Checks if the given user can mute members on the server.
     *
     * @return Whether the given user can mute members or not.
     */
    default boolean canMuteMembers() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.MUTE_MEMBERS);
    }

    /**
     * Checks if the given user can deafen members on the server.
     *
     * @return Whether the given user can deafen members or not.
     */
    default boolean canDeafenMembers() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.DEAFEN_MEMBERS);
    }

    /**
     * Checks if the given user can move members on the server.
     *
     * @return Whether the given user can move members or not.
     */
    default boolean canMoveMembers() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.MOVE_MEMBERS);
    }

    /**
     * Checks if the given user can manage emojis on the server.
     *
     * @return Whether the given user can manage emojis or not.
     */
    default boolean canManageEmojis() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.MANAGE_EMOJIS);
    }

    /**
     * Checks if the given user can use slash commands on the server.
     *
     * @return Whether the given user can use slash commands or not.
     */
    default boolean canUseSlashCommands() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.USE_APPLICATION_COMMANDS);
    }

    /**
     * Checks if the given user can manage roles on the server.
     *
     * @return Whether the given user can manage roles or not.
     */
    default boolean canManageRoles() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.MANAGE_ROLES);
    }

    /**
     * Checks if the user can manage the roles of the target user.
     *
     * @param target The user whose roles are to be managed.
     * @return Whether the user can manage the target's roles.
     */
    default boolean canManageRolesOf(Member target) {
        return canManageRole(target.getHighestRole().orElseGet(getServer()::getEveryoneRole));
    }

    /**
     * Checks if the user can manage the target role.
     *
     * @param target The role that is to be managed.
     * @return Whether the user can manage the role.
     */
    default boolean canManageRole(Role target) {
        return target.getServer() == getServer()
                && (getServer().isOwner(getUser())
                || (canManageRoles()
                && getHighestRole().orElseGet(getServer()::getEveryoneRole).compareTo(target) > 0));
    }

    /**
     * Checks if the given user can manage the server.
     *
     * @return Whether the given user can manage the server or not.
     */
    default boolean canManage() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.MANAGE_SERVER);
    }

    /**
     * Checks if the given user can kick users from the server.
     *
     * @return Whether the given user can kick users or not.
     */
    default boolean canKickUsers() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.KICK_MEMBERS);
    }

    /**
     * Checks if the given user can ban users from the server.
     *
     * @return Whether the given user can ban users or not.
     */
    default boolean canBanUsers() {
        return hasAnyPermission(PermissionType.ADMINISTRATOR, PermissionType.BAN_MEMBERS);
    }


    /**
     * Gets the voice channel the given user is connected to on this server if any.
     *
     * @return The voice channel the user is connected to.
     */
    default Optional<ServerVoiceChannel> getConnectedVoiceChannel() {
        return getServer().getConnectedVoiceChannel(getId());
    }

    /**
     * Checks whether this user is connected to the given channel.
     *
     * @param channel The channel to check.
     * @return Whether this user is connected to the given channel or not.
     */
    default boolean isConnected(ServerVoiceChannel channel) {
        return channel.isConnected(getId());
    }

    /**
     * Gets a sorted (by position) list with all channels of this server the given user can see.
     * Returns an empty list, if the user is not a member of this server.
     *
     * @return The visible channels of this server.
     */
    default List<ServerChannel> getVisibleChannels() {
        return Collections.unmodifiableList(getServer().getChannels().stream()
                .filter(channel -> channel.canSee(getUser()))
                .collect(Collectors.toList()));
    }

    ///////////////////////////////////////////////////////////////////////////
    // REST METHODS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Creates an updater for this member.
     *
     * @return An updater for this member.
     */
    default MemberUpdater createUpdater() {
        return new MemberUpdater(getServer());
    }


    /**
     * Changes the nickname of the member in the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param nickname The new nickname of the member.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(String nickname) {
        return createUpdater().setNickname(this, nickname).update();
    }

    /**
     * Changes the nickname of the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param nickname The new nickname of the user.
     * @param reason   The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(String nickname, String reason) {
        return createUpdater().setNickname(this, nickname).setAuditLogReason(reason).update();
    }

    /**
     * Removes the nickname of the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname() {
        return createUpdater().setNickname(this, null).update();
    }

    /**
     * Removes the nickname of the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user   The user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(User user, String reason) {
        return createUpdater().setNickname(this, null).setAuditLogReason(reason).update();
    }

    /**
     * Timeout the given user on this server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param timeout The timestamp until the user should be timed out.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Instant timeout) {
        return createUpdater().setMemberTimeout(this, timeout).update();
    }

    /**
     * Timeout the given user on this server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param timeout The timestamp until the user should be timed out.
     * @param reason  The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Instant timeout, String reason) {
        return createUpdater().setMemberTimeout(this, timeout).setAuditLogReason(reason).update();
    }

    /**
     * Timeout the given user on this server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param duration The duration of the timeout.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Duration duration) {
        return createUpdater().setMemberTimeout(this, Instant.now().plus(duration)).update();
    }

    /**
     * Timeout the given user on this server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param duration The duration of the timeout.
     * @param reason   The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Duration duration, String reason) {
        return createUpdater().setMemberTimeout(this, Instant.now().plus(duration))
                .setAuditLogReason(reason)
                .update();
    }

    /**
     * Remove a timeout for the given user on this server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeTimeout() {
        return createUpdater().setMemberTimeout(this, Instant.MIN).update();
    }

    /**
     * Remove a timeout for the given user on this server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeTimeout(String reason) {
        return createUpdater().setMemberTimeout(this, Instant.MIN).setAuditLogReason(reason).update();
    }


    /**
     * Gets the timestamp of when the member's timeout will expire
     * and the user will be able to communicate in the server again.
     * The returned Instant will be checked against {@link Instant#now()} and will only return an Instant,
     * if the timeout is active at the moment when this method is called.
     *
     * @return The timestamp of when this user will no longer be timed out.
     */
    default Optional<Instant> getActiveTimeout() {
        return getTimeout().filter(Instant.now()::isBefore);
    }

    /**
     * Moves the given user to the given channel on the server.
     *
     * @param channel The channel to move the user to.
     * @return A future to check if the move was successful.
     */
    default CompletableFuture<Void> moveToVoiceChannel(ServerVoiceChannel channel) {
        return createUpdater().setVoiceChannel(this, channel).update();
    }

    /**
     * Kicks the given user from any voice channel.
     *
     * @return A future to check if the kick was successful.
     */
    default CompletableFuture<Void> kickFromVoiceChannel() {
        return createUpdater().setVoiceChannel(this, null).update();
    }


    /**
     * Mutes this user on the given server.
     *
     * @return A future to check if the mute was successful.
     */
    default CompletableFuture<Void> mute() {
        return createUpdater().setMuted(this, true).update();
    }

    /**
     * Mutes this user on the given server.
     *
     * @param reason The audit log reason for this action.
     * @return A future to check if the mute was successful.
     */
    default CompletableFuture<Void> mute(String reason) {
        return createUpdater().setMuted(this, true).setAuditLogReason(reason).update();
    }

    /**
     * Unmutes this user on the given server.
     *
     * @return A future to check if the unmute was successful.
     */
    default CompletableFuture<Void> unmute() {
        return createUpdater().setMuted(this, false).update();
    }

    /**
     * Unmutes this user on the given server.
     *
     * @param reason The audit log reason for this action.
     * @return A future to check if the unmute was successful.
     */
    default CompletableFuture<Void> unmute(String reason) {
        return createUpdater().setMuted(this, false).setAuditLogReason(reason).update();
    }

    /**
     * Deafens this user on the given server.
     *
     * @return A future to check if the deafen was successful.
     */
    default CompletableFuture<Void> deafen() {
        return createUpdater().setDeafened(this, true).update();
    }

    /**
     * Deafens this user on the given server.
     *
     * @param reason The audit log reason for this action.
     * @return A future to check if the deafen was successful.
     */
    default CompletableFuture<Void> deafen(String reason) {
        return createUpdater().setDeafened(this, true).setAuditLogReason(reason).update();
    }

    /**
     * Undeafens this user on the given server.
     *
     * @return A future to check if the undeafen was successful.
     */
    default CompletableFuture<Void> undeafen() {
        return createUpdater().setDeafened(this, false).update();
    }

    /**
     * Undeafens this user on the given server.
     *
     * @param reason The audit log reason for this action.
     * @return A future to check if the undeafen was successful.
     */
    default CompletableFuture<Void> undeafen(String reason) {
        return createUpdater().setDeafened(this, false).setAuditLogReason(reason).update();
    }


    /**
     * Adds the given role to the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link MemberUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param role The role which should be added to the server member.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> addRole(Role role) {
        return addRole(role, null);
    }

    /**
     * Adds the given role to the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param role   The role which should be added to the server member.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> addRole(Role role, String reason);

    /**
     * Removes the given role from the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The server member the role should be removed from.
     * @param role The role which should be removed from the server member.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeRole(User user, Role role) {
        return removeRole(role, null);
    }

    /**
     * Removes the given role from the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param role   The role which should be removed from the server member.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> removeRole(Role role, String reason);

    /**
     * Updates the roles of a server member.
     * This will replace the roles of the server member with a provided collection.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param roles The collection of roles to replace the user's roles.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRoles(Collection<Role> roles) {
        return createUpdater()
                .removeAllRolesFromMember(this)
                .addRolesToMember(this, roles)
                .update();
    }

    /**
     * Updates the roles of a server member.
     * This will replace the roles of the server member with a provided collection.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param roles  The collection of roles to replace the user's roles.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRoles(Collection<Role> roles, String reason) {
        return createUpdater()
                .removeAllRolesFromMember(this)
                .addRolesToMember(this, roles)
                .setAuditLogReason(reason)
                .update();
    }


    /**
     * Kicks the given user from the server.
     *
     * @return A future to check if the kick was successful.
     */
    default CompletableFuture<Void> kick() {
        return kick(null);
    }

    /**
     * Kicks the given user from the server.
     *
     * @param reason The audit log reason for this action.
     * @return A future to check if the kick was successful.
     */
    CompletableFuture<Void> kick(String reason);

    /**
     * Bans the given user from the server.
     *
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> ban() {
        return getServer().banUser(getId(), null);
    }

    /**
     * Bans the given user from the server.
     *
     * @param deleteMessageSeconds The number of messages to delete within the duration.
     *                             (Between 0 and 604800 seconds (7 days)).
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> ban(int deleteMessageSeconds) {
        return getServer().banUser(getId(), deleteMessageSeconds, TimeUnit.SECONDS);
    }

    /**
     * Bans the given user from the server.
     *
     * @param deleteMessageSeconds The number of messages to delete within the duration.
     *                             (Between 0 and 604800 seconds (7 days)).
     * @param reason               The reason for the ban.
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> ban(int deleteMessageSeconds, String reason) {
        return getServer().banUser(getId(), deleteMessageSeconds, TimeUnit.SECONDS, reason);
    }

}
