package org.javacord.api.entity.user;

import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.group.GroupChannelChangeNameListener;
import org.javacord.api.listener.channel.group.GroupChannelCreateListener;
import org.javacord.api.listener.channel.group.GroupChannelDeleteListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.api.listener.channel.user.PrivateChannelCreateListener;
import org.javacord.api.listener.channel.user.PrivateChannelDeleteListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.api.listener.server.member.ServerMemberBanListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
import org.javacord.api.listener.server.member.ServerMemberUnbanListener;
import org.javacord.api.listener.server.role.UserRoleAddListener;
import org.javacord.api.listener.server.role.UserRoleRemoveListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.listener.user.UserChangeActivityListener;
import org.javacord.api.listener.user.UserChangeAvatarListener;
import org.javacord.api.listener.user.UserChangeDiscriminatorListener;
import org.javacord.api.listener.user.UserChangeNameListener;
import org.javacord.api.listener.user.UserChangeNicknameListener;
import org.javacord.api.listener.user.UserChangeSelfMutedListener;
import org.javacord.api.listener.user.UserChangeStatusListener;
import org.javacord.api.listener.user.UserStartTypingListener;
import org.javacord.api.util.event.ListenerManager;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a user.
 */
public interface User extends DiscordEntity, Messageable, Mentionable, UpdatableFromCache<User> {

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
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    String getName();

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
     * Checks if this user is the owner of the current account.
     * Always returns <code>false</code> if logged in to a user account.
     *
     * @return Whether this user is the owner of the current account.
     */
    default boolean isBotOwner() {
        return getApi().getAccountType() == AccountType.BOT && getApi().getOwnerId() == getId();
    }

    /**
     * Gets the activity of the user.
     *
     * @return The activity of the user.
     */
    Optional<Activity> getActivity();

    /**
     * Gets the server voice channels the user is connected to.
     *
     * @return The server voice channels the user is connected to.
     */
    default Collection<ServerVoiceChannel> getConnectedVoiceChannels() {
        return Collections.unmodifiableCollection(getApi().getServerVoiceChannels().stream()
                                                          .filter(this::isConnected)
                                                          .collect(Collectors.toList()));
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
     * Gets the status of the user.
     *
     * @return The status of the user.
     */
    UserStatus getStatus();

    /**
     * Gets the avatar of the user.
     *
     * @return The avatar of the user.
     */
    Icon getAvatar();

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
    default Collection<Server> getMutualServers() {
        // TODO This is probably not the most efficient way to do it
        return getApi().getServers().stream()
                .filter(server -> server.getMembers().contains(this))
                .collect(Collectors.toList());
    }

    /**
     * Gets the display name of the user.
     * If the user has a nickname, it will return the nickname, otherwise it will return the "normal" name.
     *
     * @param server The server.
     * @return The display name of the user.
     */
    default String getDisplayName(Server server) {
        return server.getNickname(this).orElseGet(this::getName);
    }

    /**
     * Gets the discriminated name of the user, e. g. {@code Bastian#8222}.
     *
     * @return The discriminated name of the user.
     */
    default String getDiscriminatedName() {
        return getName() + "#" + getDiscriminator();
    }

    /**
     * Changes the nickname of the user in the given server.
     *
     * @param server The server.
     * @param nickname The new nickname of the user.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(Server server, String nickname) {
        return server.updateNickname(this, nickname);
    }

    /**
     * Removes the nickname of the user in the given server.
     *
     * @param server The server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(Server server) {
        return server.resetNickname(this);
    }

    /**
     * Gets the nickname of the user in the given server.
     *
     * @param server The server to check.
     * @return The nickname of the user.
     */
    default Optional<String> getNickname(Server server) {
        return server.getNickname(this);
    }

    /**
     * Gets the self-muted state of the user in the given server.
     *
     * @param server The server to check.
     * @return Whether the user is self-muted in the given server.
     */
    default boolean isSelfMuted(Server server) {
        return server.isSelfMuted(getId());
    }

    /**
     * Gets the timestamp of when the user joined the given server.
     *
     * @param server The server to check.
     * @return The timestamp of when the user joined the server.
     */
    default Optional<Instant> getJoinedAtTimestamp(Server server) {
        return server.getJoinedAtTimestamp(this);
    }

    /**
     * Gets a sorted list (by position) with all roles of the user in the given server.
     *
     * @param server The server.
     * @return A sorted list (by position) with all roles of the user in the given server.
     * @see Server#getRolesOf(User)
     */
    default List<Role> getRoles(Server server) {
        return server.getRolesOf(this);
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
     * This will only be present, if there was an conversation with the user in the past or you manually opened a
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
     * Gets the currently existing group channels with the user.
     *
     * @return The group channels with the user.
     */
    default Collection<GroupChannel> getGroupChannels() {
        return getApi().getGroupChannels().stream()
                .filter(groupChannel -> groupChannel.getMembers().contains(this))
                .collect(Collectors.toList());
    }

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
     * @param role The role which should be added to the user.
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
     * @param role The role which should be removed from the user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     * @see Server#removeRoleFromUser(User, Role, String)
     */
    default CompletableFuture<Void> removeRole(Role role, String reason) {
        return role.getServer().removeRoleFromUser(this, role, reason);
    }

    /**
     * Adds a listener, which listens to private channel creations for this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<PrivateChannelCreateListener> addPrivateChannelCreateListener(
            PrivateChannelCreateListener listener);

    /**
     * Gets a list with all registered private channel create listeners.
     *
     * @return A list with all registered private channel create listeners.
     */
    List<PrivateChannelCreateListener> getPrivateChannelCreateListeners();

    /**
     * Adds a listener, which listens to private channel deletions for this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener);

    /**
     * Gets a list with all registered private channel delete listeners.
     *
     * @return A list with all registered private channel delete listeners.
     */
    List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners();

    /**
     * Adds a listener, which listens to group channel creations for this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelCreateListener> addGroupChannelCreateListener(GroupChannelCreateListener listener);

    /**
     * Gets a list with all registered group channel create listeners.
     *
     * @return A list with all registered group channel create listeners.
     */
    List<GroupChannelCreateListener> getGroupChannelCreateListeners();

    /**
     * Adds a listener, which listens to group channel name changes for this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener);

    /**
     * Gets a list with all registered group channel change name listeners.
     *
     * @return A list with all registered group channel change name listeners.
     */
    List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners();

    /**
     * Adds a listener, which listens to group channel deletions for this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(GroupChannelDeleteListener listener);

    /**
     * Gets a list with all registered group channel delete listeners.
     *
     * @return A list with all registered group channel delete listeners.
     */
    List<GroupChannelDeleteListener> getGroupChannelDeleteListeners();

    /**
     * Adds a listener, which listens to message creates from this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener);

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    List<MessageCreateListener> getMessageCreateListeners();

    /**
     * Adds a listener, which listens to this user starting to type.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener);

    /**
     * Gets a list with all registered user starts typing listeners.
     *
     * @return A list with all registered user starts typing listeners.
     */
    List<UserStartTypingListener> getUserStartTypingListeners();

    /**
     * Adds a listener, which listens to reactions being added by this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener);

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners();

    /**
     * Adds a listener, which listens to reactions being removed by this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener);

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners();

    /**
     * Adds a listener, which listens to this user joining known servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener);

    /**
     * Gets a list with all registered server member join listeners.
     *
     * @return A list with all registered server member join listeners.
     */
    List<ServerMemberJoinListener> getServerMemberJoinListeners();

    /**
     * Adds a listener, which listens to this user leaving known servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(ServerMemberLeaveListener listener);

    /**
     * Gets a list with all registered server member leave listeners.
     *
     * @return A list with all registered server member leave listeners.
     */
    List<ServerMemberLeaveListener> getServerMemberLeaveListeners();

    /**
     * Adds a listener, which listens to this user getting banned from known servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener);

    /**
     * Gets a list with all registered server member ban listeners.
     *
     * @return A list with all registered server member ban listeners.
     */
    List<ServerMemberBanListener> getServerMemberBanListeners();

    /**
     * Adds a listener, which listens to this user getting unbanned from known servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener);

    /**
     * Gets a list with all registered server member unban listeners.
     *
     * @return A list with all registered server member unban listeners.
     */
    List<ServerMemberUnbanListener> getServerMemberUnbanListeners();

    /**
     * Adds a listener, which listens to this user's activity changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(UserChangeActivityListener listener);

    /**
     * Gets a list with all registered user change activity listeners.
     *
     * @return A list with all registered user change activity listeners.
     */
    List<UserChangeActivityListener> getUserChangeActivityListeners();

    /**
     * Adds a listener, which listens to this user's status changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener);

    /**
     * Gets a list with all registered user change status listeners.
     *
     * @return A list with all registered user change status listeners.
     */
    List<UserChangeStatusListener> getUserChangeStatusListeners();

    /**
     * Adds a listener, which listens to overwritten permission changes of this user.
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
    List<ServerChannelChangeOverwrittenPermissionsListener> getServerChannelChangeOverwrittenPermissionsListeners();

    /**
     * Adds a listener, which listens to nickname changes of this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(UserChangeNicknameListener listener);

    /**
     * Gets a list with all registered user change nickname listeners.
     *
     * @return A list with all registered user change nickname listeners.
     */
    List<UserChangeNicknameListener> getUserChangeNicknameListeners();

    /**
     * Adds a listener, which listens to self-muted changes of this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeSelfMutedListener> addUserChangeSelfMutedListener(UserChangeSelfMutedListener listener);

    /**
     * Gets a list with all registered user change self-muted listeners.
     *
     * @return A list with all registered user change self-muted listeners.
     */
    List<UserChangeSelfMutedListener> getUserChangeSelfMutedListeners();

    /**
     * Adds a listener, which listens to this user being added to roles.
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
    List<UserRoleAddListener> getUserRoleAddListeners();

    /**
     * Adds a listener, which listens to this user being removed from roles in this server.
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
    List<UserRoleRemoveListener> getUserRoleRemoveListeners();

    /**
     * Adds a listener, which listens to name changes of this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener);

    /**
     * Gets a list with all registered user change name listeners.
     *
     * @return A list with all registered user change name listeners.
     */
    List<UserChangeNameListener> getUserChangeNameListeners();

    /**
     * Adds a listener, which listens to discriminator changes of this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeDiscriminatorListener> addUserChangeDiscriminatorListener(
            UserChangeDiscriminatorListener listener);

    /**
     * Gets a list with all registered user change discriminator listeners.
     *
     * @return A list with all registered user change discriminator listeners.
     */
    List<UserChangeDiscriminatorListener> getUserChangeDiscriminatorListeners();

    /**
     * Adds a listener, which listens to avatar changes of this user.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener);

    /**
     * Gets a list with all registered user change avatar listeners.
     *
     * @return A list with all registered user change avatar listeners.
     */
    List<UserChangeAvatarListener> getUserChangeAvatarListeners();

    /**
     * Adds a listener, which listens to this user joining a server voice channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener);

    /**
     * Gets a list with all registered server voice channel member join listeners.
     *
     * @return A list with all registered server voice channel member join listeners.
     */
    List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners();

    /**
     * Adds a listener, which listens to this user leaving a server voice channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener);

    /**
     * Gets a list with all registered server voice channel member leave listeners.
     *
     * @return A list with all registered server voice channel member leave listeners.
     */
    List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners();

    /**
     * Adds a listener that implements one or more {@code UserAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends UserAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addUserAttachableListener(T listener);

    /**
     * Removes a listener that implements one or more {@code UserAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends UserAttachableListener & ObjectAttachableListener> void removeUserAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code UserAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code UserAttachableListener}s and their
     * assigned listener classes they listen to.
     */
    <T extends UserAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>> getUserAttachableListeners();

    /**
     * Removes a listener from this user.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends UserAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

    @Override
    default Optional<User> getCurrentCachedInstance() {
        return getApi().getCachedUserById(getId());
    }

    @Override
    default CompletableFuture<User> getLatestInstance() {
        return getApi().getUserById(getId());
    }

}
