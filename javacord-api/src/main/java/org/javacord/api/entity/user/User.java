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
import org.javacord.api.entity.server.Server;
import org.javacord.api.listener.user.UserAttachableListenerManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
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
     */
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
     * Gets the discriminated name of the user, e.g. {@code Bastian#8222}.
     *
     * @return The discriminated name of the user.
     */
    default String getDiscriminatedName() {
        return getName() + "#" + getDiscriminator();
    }

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

    @Override
    default Optional<User> getCurrentCachedInstance() {
        return getApi().getCachedUserById(getId());
    }

    @Override
    default CompletableFuture<User> getLatestInstance() {
        return getApi().getUserById(getId());
    }

}
