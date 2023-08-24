package org.javacord.api.entity.user;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.listener.user.UserAttachableListenerManager;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a user.
 */
public interface User extends DiscordEntity, Messageable, Nameable, Mentionable, Permissionable,
        UpdatableFromCache<User>, UserAttachableListenerManager {

    @Override
    default String getMentionTag() {
        return "<@" + getIdAsString() + ">";
    }

    /**
     * Gets the mention tag, to mention the user with its nickname, instead of its normal name.
     *
     * @return The mention tag, to mention the user with its nickname.
     */
    default String getNicknameMentionTag() {
        return "<@!" + getIdAsString() + ">";
    }

    /**
     * Gets the discriminator of the user.
     *
     * @return The discriminator of the user.
     * @deprecated Use {@link #getName()} to get the username and
     * {@link #getGlobalName()} to get the user's global name.
     */
    @Deprecated
    String getDiscriminator();

    /**
     * Checks if the user is a bot account.
     *
     * @return Whether the user is a bot account or not.
     */
    boolean isBot();

    /**
     * Checks if this user is the owner of the current account or the current account's team.
     *
     * @return Whether this user is the owner of the current account.
     */
    default boolean isBotOwner() {
        return getApi().getOwnerId().isPresent() && getApi().getOwnerId().get() == getId();
    }

    /**
     * Checks if this user is a member of the team of the current account.
     *
     * @return Whether this user is a member of the team of the current account.
     */
    default boolean isTeamMember() {
        return getApi().getCachedTeam().map(team -> team.getOwnerId() == getId()
                || team.getTeamMembers().stream().anyMatch(teamMember -> teamMember.getId() == getId())).orElse(false);
    }

    /**
     * Checks if this user is the owner or a member of the team of the current account.
     *
     * @return Whether this user is the owner or a member of the team of the current account.
     */
    default boolean isBotOwnerOrTeamMember() {
        return isBotOwner() || isTeamMember();
    }

    /**
     * Gets the activities of the user.
     *
     * @return The activities of the user.
     */
    Set<Activity> getActivities();

    /**
     * Checks if the user can manage the target role.
     *
     * @param role The role that is to be managed.
     * @return Whether the user can manage the role.
     */
    default boolean canManageRole(Role role) {
        return role.getServer().canManageRole(this, role);
    }

    /**
     * Gets the server voice channels the user is connected to.
     *
     * @return The server voice channels the user is connected to.
     */
    default Set<ServerVoiceChannel> getConnectedVoiceChannels() {
        return Collections.unmodifiableSet(getApi().getServerVoiceChannels().stream()
                .filter(this::isConnected)
                .collect(Collectors.toSet()));
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
     * Gets the voice channel this user is connected to on the given server if any.
     *
     * @param server The server to check.
     * @return The server voice channel the user is connected to.
     */
    default Optional<ServerVoiceChannel> getConnectedVoiceChannel(Server server) {
        return server.getConnectedVoiceChannel(getId());
    }

    /**
     * Gets the connection status of the user as it is displayed in the user list.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * <p>To see if a non-offline user is connected via a mobile client, a desktop client, a web client or any
     * combination of the three use the {@link #getStatusOnClient(DiscordClient)}} method.
     *
     * @return The status of the user.
     */
    UserStatus getStatus();

    /**
     * Gets the status of the user on the given client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @param client The client.
     * @return The status of the user
     * @see #getStatus()
     */
    UserStatus getStatusOnClient(DiscordClient client);

    /**
     * Gets the status of the user on the {@link DiscordClient#DESKTOP desktop} client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the user.
     * @see #getStatusOnClient(DiscordClient)
     */
    default UserStatus getDesktopStatus() {
        return getStatusOnClient(DiscordClient.DESKTOP);
    }

    /**
     * Gets the status of the user on the {@link DiscordClient#MOBILE mobile} client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the user.
     * @see #getStatusOnClient(DiscordClient)
     */
    default UserStatus getMobileStatus() {
        return getStatusOnClient(DiscordClient.MOBILE);
    }

    /**
     * Gets the status of the user on the {@link DiscordClient#WEB web} (browser) client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the user.
     * @see #getStatusOnClient(DiscordClient)
     */
    default UserStatus getWebStatus() {
        return getStatusOnClient(DiscordClient.WEB);
    }

    /**
     * Gets all clients of the user that are not {@link UserStatus#OFFLINE offline}.
     *
     * @return The DiscordClients.
     * @see #getStatusOnClient(DiscordClient)
     */
    default Set<DiscordClient> getCurrentClients() {
        Set<DiscordClient> connectedClients = Arrays
                .stream(DiscordClient.values())
                .filter(client -> getStatusOnClient(client) != UserStatus.OFFLINE)
                .collect(Collectors.toSet());
        return Collections.unmodifiableSet(connectedClients);
    }

    /**
     * Gets the public flags (badges) present on this account.
     *
     * @return The public flags for this account.
     */
    EnumSet<UserFlag> getUserFlags();

    /**
     * Gets the avatar hash of the user.
     *
     * @return The avatar hash.
     */
    Optional<String> getAvatarHash();

    /**
     * Gets the avatar of the user.
     *
     * @return The avatar of the user.
     */
    Icon getAvatar();

    /**
     * Gets the avatar of the user.
     *
     * @param size the size of the image. must be any power of 2 between 16 and 4096
     * @return The avatar of the user.
     */
    Icon getAvatar(int size);

    /**
     * Gets the member's server avatar hash.
     *
     * @param server The server.
     * @return The member's server avatar hash.
     */
    Optional<String> getServerAvatarHash(Server server);

    /**
     * Gets the user's server-specific avatar in the given server.
     *
     * @param server The server.
     * @return The user's avatar in the server
     */
    Optional<Icon> getServerAvatar(Server server);

    /**
     * Gets the user's server-specific avatar in the given server at the given image size.
     *
     * @param server The server.
     * @param size   The size of the image, must be any power of 2 between 16 and 4096.
     * @return The user's avatar in the server.
     */
    Optional<Icon> getServerAvatar(Server server, int size);

    /**
     * Gets the user's effective avatar in the given server.
     * This will return the user's server-specific avatar if they have one, otherwise it will return their account
     * avatar.
     *
     * @param server The server.
     * @return The user's effective avatar.
     */
    Icon getEffectiveAvatar(Server server);

    /**
     * Gets the user's effective avatar in the given server at the given size.
     * This will return the user's server-specific avatar if they have one, otherwise it will return their account
     * avatar.
     *
     * @param server The server.
     * @param size   The size of the image, must be any power of 2 between 16 and 4096.
     * @return The user's effective avatar.
     */
    Icon getEffectiveAvatar(Server server, int size);

    /**
     * Gets if the user has a default Discord avatar.
     *
     * @return Whether this user has a default avatar or not.
     */
    boolean hasDefaultAvatar();

    /**
     * Gets all mutual servers with this user.
     *
     * @return All mutual servers with this user.
     */
    Set<Server> getMutualServers();

    /**
     * Gets the display name of the user.
     *
     * @return The display name of the user.
     */
    Optional<String> getGlobalName();

    /**
     * Gets the display name of the user.
     * If the user has a nickname, it will return the nickname, otherwise it will return the "normal" name.
     *
     * @param server The server.
     * @return The display name of the user.
     */
    String getDisplayName(Server server);

    /**
     * Gets the discriminated name of the user, e.g. {@code Bastian#8222}.
     *
     * @return The discriminated name of the user.
     */
    default String getDiscriminatedName() {
        return getName() + "#" + getDiscriminator();
    }

    /**
     * Changes the nickname of the user in the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server   The server.
     * @param nickname The new nickname of the user.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(Server server, String nickname) {
        return server.updateNickname(this, nickname);
    }

    /**
     * Changes the nickname of the user in the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server   The server.
     * @param nickname The new nickname of the user.
     * @param reason   The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(Server server, String nickname, String reason) {
        return server.updateNickname(this, nickname, reason);
    }

    /**
     * Removes the nickname of the user in the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server The server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(Server server) {
        return server.resetNickname(this);
    }

    /**
     * Removes the nickname of the user in the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server The server.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(Server server, String reason) {
        return server.resetNickname(this, reason);
    }

    /**
     * Gets the nickname of the user in the given server.
     *
     * @param server The server to check.
     * @return The nickname of the user.
     */
    Optional<String> getNickname(Server server);

    /**
     * Gets the timestamp of when this member started boosting the server.
     *
     * @param server The server.
     * @return The timestamp of when this member started boosting the server.
     */
    Optional<Instant> getServerBoostingSinceTimestamp(Server server);

    /**
     * Timeouts the user on the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server  The server.
     * @param timeout The new timeout of the user.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Server server, Instant timeout) {
        return server.timeoutUser(this, timeout);
    }

    /**
     * Timeouts the user on the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server  The server.
     * @param timeout The new timeout of the user.
     * @param reason  The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Server server, Instant timeout, String reason) {
        return server.timeoutUser(this, timeout, reason);
    }

    /**
     * Timeouts the user on the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server  The server.
     * @param duration The duration of the timeout.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Server server, Duration duration) {
        return server.timeoutUser(this, Instant.now().plus(duration));
    }

    /**
     * Timeouts the user on the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server  The server.
     * @param duration The duration of the timeout.
     * @param reason  The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> timeout(Server server, Duration duration, String reason) {
        return server.timeoutUser(this, Instant.now().plus(duration), reason);
    }

    /**
     * Removes a timeout of the user on the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server The server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeTimeout(Server server) {
        return server.timeoutUser(this, Instant.MIN);
    }

    /**
     * Removes a timeout of the user on the given server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param server The server.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeTimeout(Server server, String reason) {
        return server.timeoutUser(this, Instant.MIN, reason);
    }

    /**
     * Gets the timestamp of when the user's timeout will expire
     * and the user will be able to communicate in the server again.
     * The returned Instant may be in the past which indicates that the user is not timed out.
     *
     * @param server The server to get the timeout for the user from.
     * @return The timestamp of when this user will no longer be timed out. Empty or a timestamp in the past,
     *         if user is currently not timed out.
     */
    Optional<Instant> getTimeout(Server server);

    /**
     * Gets the timestamp of when the user's timeout will expire
     * and the user will be able to communicate in the server again.
     * The returned Instant will be checked against {@link Instant#now()} and will only return an Instant,
     * if the timeout is active at the moment when this method is called.
     *
     * @param server The server to get the timeout for the user from.
     * @return The timestamp of when this user will no longer be timed out.
     */
    default Optional<Instant> getActiveTimeout(Server server) {
        return getTimeout(server).filter(Instant.now()::isBefore);
    }

    /**
     * Gets the pending state of the user in the given server.
     *
     * <p>This will always return false if the server doesn't have membership screening enable.
     * You can check through {@link Server#getFeatures()} if it has
     * {@link org.javacord.api.entity.server.ServerFeature#MEMBER_VERIFICATION_GATE_ENABLED}.
     *
     * @param server The server to check.
     * @return Whether the user has passed the Membership screening.
     */
    boolean isPending(Server server);

    /**
     * Moves this user to the given channel.
     *
     * @param channel The channel to move the user to.
     * @return A future to check if the move was successful.
     */
    default CompletableFuture<Void> move(ServerVoiceChannel channel) {
        return channel.getServer().moveUser(this, channel);
    }

    /**
     * Gets the self-muted state of the user in the given server.
     *
     * @param server The server to check.
     * @return Whether the user is self-muted in the given server.
     */
    boolean isSelfMuted(Server server);

    /**
     * Gets the self-deafened state of the user in the given server.
     *
     * @param server The server to check.
     * @return Whether the user is self-deafened in the given server.
     */
    boolean isSelfDeafened(Server server);

    /**
     * Mutes this user on the given server.
     *
     * @param server The server to mute this user on.
     * @return A future to check if the mute was successful.
     */
    default CompletableFuture<Void> mute(Server server) {
        return server.muteUser(this);
    }

    /**
     * Mutes this user on the given server.
     *
     * @param server The server to mute this user on.
     * @param reason The audit log reason for this action.
     * @return A future to check if the mute was successful.
     */
    default CompletableFuture<Void> mute(Server server, String reason) {
        return server.muteUser(this, reason);
    }

    /**
     * Unmutes this user on the given server.
     *
     * @param server The server to unmute this user on.
     * @return A future to check if the unmute was successful.
     */
    default CompletableFuture<Void> unmute(Server server) {
        return server.unmuteUser(this);
    }

    /**
     * Unmutes this user on the given server.
     *
     * @param server The server to unmute this user on.
     * @param reason The audit log reason for this action.
     * @return A future to check if the unmute was successful.
     */
    default CompletableFuture<Void> unmute(Server server, String reason) {
        return server.unmuteUser(this, reason);
    }

    /**
     * Gets the muted state of the user in the given server.
     *
     * @param server The server to check.
     * @return Whether the user is muted in the given server.
     */
    default boolean isMuted(Server server) {
        return server.isMuted(getId());
    }

    /**
     * Deafens this user on the given server.
     *
     * @param server The server to deafen this user on.
     * @return A future to check if the deafen was successful.
     */
    default CompletableFuture<Void> deafen(Server server) {
        return server.deafenUser(this);
    }

    /**
     * Deafens this user on the given server.
     *
     * @param server The server to deafen this user on.
     * @param reason The audit log reason for this action.
     * @return A future to check if the deafen was successful.
     */
    default CompletableFuture<Void> deafen(Server server, String reason) {
        return server.deafenUser(this, reason);
    }

    /**
     * Undeafens this user on the given server.
     *
     * @param server The server to undeafen this user on.
     * @return A future to check if the undeafen was successful.
     */
    default CompletableFuture<Void> undeafen(Server server) {
        return server.undeafenUser(this);
    }

    /**
     * Undeafens this user on the given server.
     *
     * @param server The server to undeafen this user on.
     * @param reason The audit log reason for this action.
     * @return A future to check if the undeafen was successful.
     */
    default CompletableFuture<Void> undeafen(Server server, String reason) {
        return server.undeafenUser(this, reason);
    }

    /**
     * Gets the deafened state of the user in the given server.
     *
     * @param server The server to check.
     * @return Whether the user is deafened in the given server.
     */
    default boolean isDeafened(Server server) {
        return server.isDeafened(getId());
    }

    /**
     * Gets the timestamp of when the user joined the given server.
     *
     * @param server The server to check.
     * @return The timestamp of when the user joined the server.
     */
    Optional<Instant> getJoinedAtTimestamp(Server server);

    /**
     * Gets a sorted list (by position) with all roles of the user in the given server.
     *
     * @param server The server.
     * @return A sorted list (by position) with all roles of the user in the given server.
     * @see Server#getRoles(User)
     */
    List<Role> getRoles(Server server);

    /**
     * Gets the displayed color of the user based on their roles in the given server.
     *
     * @param server The server.
     * @return The color.
     * @see Server#getRoleColor(User)
     */
    Optional<Color> getRoleColor(Server server);

    /**
     * Gets if this user is the user of the connected account.
     *
     * @return Whether this user is the user of the connected account or not.
     * @see DiscordApi#getYourself()
     */
    default boolean isYourself() {
        return getId() == getApi().getYourself().getId();
    }

    /**
     * Gets the private channel with the user.
     * This will only be present, if there was a conversation with the user in the past, or you manually opened a
     * private channel with the given user, using {@link #openPrivateChannel()}.
     *
     * @return The private channel with the user.
     */
    Optional<PrivateChannel> getPrivateChannel();

    /**
     * Opens a new private channel with the given user.
     * If there's already a private channel with the user, it will just return the one which already exists.
     *
     * @return The new (or old) private channel with the user.
     */
    CompletableFuture<PrivateChannel> openPrivateChannel();

    /**
     * Adds the given role to the user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param role The role which should be added to the user.
     * @return A future to check if the update was successful.
     * @see Server#addRoleToUser(User, Role)
     */
    default CompletableFuture<Void> addRole(Role role) {
        return addRole(role, null);
    }

    /**
     * Adds the given role to the user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param role   The role which should be added to the user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     * @see Server#addRoleToUser(User, Role, String)
     */
    default CompletableFuture<Void> addRole(Role role, String reason) {
        return role.getServer().addRoleToUser(this, role, reason);
    }

    /**
     * Removes the given role from the user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param role The role which should be removed from the user.
     * @return A future to check if the update was successful.
     * @see Server#removeRoleFromUser(User, Role)
     */
    default CompletableFuture<Void> removeRole(Role role) {
        return removeRole(role, null);
    }

    /**
     * Removes the given role from the user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link Server#createUpdater()} which provides a better performance!
     *
     * @param role   The role which should be removed from the user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     * @see Server#removeRoleFromUser(User, Role, String)
     */
    default CompletableFuture<Void> removeRole(Role role, String reason) {
        return role.getServer().removeRoleFromUser(this, role, reason);
    }

    @Override
    default Optional<User> getCurrentCachedInstance() {
        return getApi().getCachedUserById(getId());
    }

    @Override
    default CompletableFuture<User> getLatestInstance() {
        return getApi().getUserById(getId());
    }

}
