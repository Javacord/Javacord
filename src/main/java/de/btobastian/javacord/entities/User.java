package de.btobastian.javacord.entities;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.message.Messageable;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerMemberAddListener;
import de.btobastian.javacord.listeners.server.ServerMemberRemoveListener;
import de.btobastian.javacord.listeners.user.UserChangeGameListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a user.
 */
public interface User extends DiscordEntity, Messageable, Mentionable, AvatarHolder {

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
     * Gets the nickname of the user in the given server.
     *
     * @param server The server to check.
     * @return The nickname of the user.
     */
    default Optional<String> getNickname(Server server) {
        return server.getNickname(this);
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
    void addMessageCreateListener(MessageCreateListener listener);

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
     */
    void addUserStartTypingListener(UserStartTypingListener listener);

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
     */
    void addReactionAddListener(ReactionAddListener listener);

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
     */
    void addReactionRemoveListener(ReactionRemoveListener listener);

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
     */
    void addServerMemberAddListener(ServerMemberAddListener listener);

    /**
     * Gets a list with all registered server member add listeners.
     *
     * @return A list with all registered server member add listeners.
     */
    List<ServerMemberAddListener> getServerMemberAddListeners();

    /**
     * Adds a listener, which listens to this user leaving known servers.
     *
     * @param listener The listener to add.
     */
    void addServerMemberRemoveListener(ServerMemberRemoveListener listener);

    /**
     * Gets a list with all registered server member remove listeners.
     *
     * @return A list with all registered server member remove listeners.
     */
    List<ServerMemberRemoveListener> getServerMemberRemoveListeners();

    /**
     * Adds a listener, which listens to this user's game changes.
     *
     * @param listener The listener to add.
     */
    void addUserChangeGameListener(UserChangeGameListener listener);

    /**
     * Gets a list with all registered user change game listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    List<UserChangeGameListener> getUserChangeGameListeners();

}
