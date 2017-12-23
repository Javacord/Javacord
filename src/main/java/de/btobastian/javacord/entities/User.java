package de.btobastian.javacord.entities;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerMemberAddListener;
import de.btobastian.javacord.listeners.server.ServerMemberRemoveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.role.UserRoleAddListener;
import de.btobastian.javacord.listeners.server.role.UserRoleRemoveListener;
import de.btobastian.javacord.listeners.user.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a user.
 */
public interface User extends DiscordEntity, Messageable, Mentionable {

    @Override
    default String getMentionTag() {
        return "<@" + getId() + ">";
    }

    /**
     * Gets the mention tag, to mention the user with it's nickname, instead of it's normal name.
     *
     * @return The mention tag, to mention the user with it's nickname.
     */
    default String getNicknameMentionTag() {
        return "<@!" + getId() + ">";
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
     * Gets the game of the user.
     *
     * @return The game of the user.
     */
    Optional<Game> getGame();

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
        return this == getApi().getYourself();
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
     * Adds a listener, which listens to message creates from this user.
     *
     * @param listener The listener to add.
     */
    default void addMessageCreateListener(MessageCreateListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), MessageCreateListener.class, listener);
    }

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    default List<MessageCreateListener> getMessageCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), MessageCreateListener.class);
    }

    /**
     * Adds a listener, which listens to this user starting to type.
     *
     * @param listener The listener to add.
     */
    default void addUserStartTypingListener(UserStartTypingListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserStartTypingListener.class, listener);
    }

    /**
     * Gets a list with all registered user starts typing listeners.
     *
     * @return A list with all registered user starts typing listeners.
     */
    default List<UserStartTypingListener> getUserStartTypingListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserStartTypingListener.class);
    }

    /**
     * Adds a listener, which listens to reactions being added by this user.
     *
     * @param listener The listener to add.
     */
    default void addReactionAddListener(ReactionAddListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), ReactionAddListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    default List<ReactionAddListener> getReactionAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ReactionAddListener.class);
    }

    /**
     * Adds a listener, which listens to reactions being removed by this user.
     *
     * @param listener The listener to add.
     */
    default void addReactionRemoveListener(ReactionRemoveListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), ReactionRemoveListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    default List<ReactionRemoveListener> getReactionRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ReactionRemoveListener.class);
    }

    /**
     * Adds a listener, which listens to this user joining known servers.
     *
     * @param listener The listener to add.
     */
    default void addServerMemberAddListener(ServerMemberAddListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), ServerMemberAddListener.class, listener);
    }

    /**
     * Gets a list with all registered server member add listeners.
     *
     * @return A list with all registered server member add listeners.
     */
    default List<ServerMemberAddListener> getServerMemberAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ServerMemberAddListener.class);
    }

    /**
     * Adds a listener, which listens to this user leaving known servers.
     *
     * @param listener The listener to add.
     */
    default void addServerMemberRemoveListener(ServerMemberRemoveListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), ServerMemberRemoveListener.class, listener);
    }

    /**
     * Gets a list with all registered server member remove listeners.
     *
     * @return A list with all registered server member remove listeners.
     */
    default List<ServerMemberRemoveListener> getServerMemberRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ServerMemberRemoveListener.class);
    }

    /**
     * Adds a listener, which listens to this user's game changes.
     *
     * @param listener The listener to add.
     */
    default void addUserChangeGameListener(UserChangeGameListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserChangeGameListener.class, listener);
    }

    /**
     * Gets a list with all registered user change game listeners.
     *
     * @return A list with all registered user change game listeners.
     */
    default List<UserChangeGameListener> getUserChangeGameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeGameListener.class);
    }

    /**
     * Adds a listener, which listens to this user's status changes.
     *
     * @param listener The listener to add.
     */
    default void addUserChangeStatusListener(UserChangeStatusListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserChangeStatusListener.class, listener);
    }

    /**
     * Gets a list with all registered user change status listeners.
     *
     * @return A list with all registered user change status listeners.
     */
    default List<UserChangeStatusListener> getUserChangeStatusListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeStatusListener.class);
    }

    /**
     * Adds a listener, which listens to overwritten permission changes of this user.
     *
     * @param listener The listener to add.
     */
    default void addServerChannelChangeOverwrittenPermissionsListener(
            ServerChannelChangeOverwrittenPermissionsListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
                User.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    default List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                User.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    /**
     * Adds a listener, which listens to nickname changes of this user.
     *
     * @param listener The listener to add.
     */
    default void addUserChangeNicknameListener(UserChangeNicknameListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserChangeNicknameListener.class, listener);
    }

    /**
     * Gets a list with all registered user change nickname listeners.
     *
     * @return A list with all registered user change nickname listeners.
     */
    default List<UserChangeNicknameListener> getUserChangeNicknameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeNicknameListener.class);
    }

    /**
     * Adds a listener, which listens to this user being added to roles.
     *
     * @param listener The listener to add.
     */
    default void addUserRoleAddListener(UserRoleAddListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserRoleAddListener.class, listener);
    }

    /**
     * Gets a list with all registered user role add listeners.
     *
     * @return A list with all registered user role add listeners.
     */
    default List<UserRoleAddListener> getUserRoleAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserRoleAddListener.class);
    }

    /**
     * Adds a listener, which listens to this user being removed from roles in this server.
     *
     * @param listener The listener to add.
     */
    default void addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserRoleRemoveListener.class, listener);
    }

    /**
     * Gets a list with all registered user role remove listeners.
     *
     * @return A list with all registered user role remove listeners.
     */
    default List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserRoleRemoveListener.class);
    }

    /**
     * Adds a listener, which listens to name changes of this user.
     *
     * @param listener The listener to add.
     */
    default void addUserChangeNameListener(UserChangeNameListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered user change name listeners.
     *
     * @return A list with all registered user change name listeners.
     */
    default List<UserChangeNameListener> getUserChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeNameListener.class);
    }

}
