package de.btobastian.javacord;

import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.*;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.server.ServerBecomesAvailableListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerJoinListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.DiscordWebsocketAdapter;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is the most important class for your bot, containing all important methods, like registering listener.
 */
public interface DiscordApi {

    /**
     * Gets the used token.
     * The returned token already includes the {@link AccountType#getTokenPrefix()}, so you can use it directly in the
     * authentication header for custom REST calls.
     *
     * @return The used token.
     */
    String getToken();

    /**
     * Gets the thread pool which is internally used.
     *
     * @return The internally used thread pool.
     */
    ThreadPool getThreadPool();

    /**
     * Gets the ratelimit manager for this bot.
     *
     * @return The ratelimit manager for this bot.
     */
    RatelimitManager getRatelimitManager();

    /**
     * Gets the websocket adapter which is used to connect to Discord.
     *
     * @return The websocket adapter.
     */
    DiscordWebsocketAdapter getWebSocketAdapter();

    /**
     * Sets the cache size of all caches.
     * This settings are applied on a per-channel basis.
     * It overrides all previous settings, so it's recommended to directly set it after logging in, if you want to
     * change apply some channel specific channel settings, too.
     * Please notice that the cache is cleared only once every minute!
     *
     * @param capacity The capacity of the message cache.
     * @param storageTimeInSeconds The maximum age of cached messages.
     */
    void setMessageCacheSize(int capacity, int storageTimeInSeconds);

    /**
     * Gets the default message cache capacity which is applied for every newly created channel.
     *
     * @return The default message cache capacity which is applied for every newly created channel.
     */
    int getDefaultMessageCacheCapacity();

    /**
     * Gets the default maximum age of cached messages.
     *
     * @return The default maximum age of cached messages.
     */
    int getDefaultMessageCacheStorageTimeInSeconds();

    /**
     * Updates the game of this bot, represented as "Playing Half-Life 3" for example.
     *
     * @param name The name of the game.
     */
    void updateGame(String name);

    /**
     * Updates the game of this bot with a streaming url, represented as "Streaming Half-Life 3" for example.
     * The update might not be visible immediately as it's through the websocket and only a limited amount of
     * game status changes is allowed per minute.
     *
     * @param name The name of the game.
     * @param streamingUrl The streaming url of the game.
     */
    void updateGame(String name, String streamingUrl);

    /**
     * Gets the game which should be displayed.
     * This might not be the game which is really displayed in the client, but it's the game which Javacord is trying
     * to set for your bot, so it might change in the client a few seconds afterwards. If you want the game which
     * is currently displayed, get the user object for your bot and get the game from this object.
     *
     * @return The game which should be displayed.
     */
    Optional<Game> getGame();

    /**
     * Disconnects the bot.
     * After disconnecting you should NOT use this instance again.
     */
    void disconnect();

    /**
     * Sets the maximum reconnect attempts in a given time before the bot stops reconnecting.
     * By default the bot stops reconnecting, if the connection failed more than 5 times in the last 5 minutes.
     * It's not recommended to change these values!
     *
     * @param attempts The amount of attempts. Default: 5.
     * @param seconds The time, in which the attempts can happen in seconds. Default: 300.
     */
    void setReconnectRatelimit(int attempts, int seconds);

    /**
     * Gets a collection with the ids of all unavailable servers.
     *
     * @return A collection with the ids of all unavailable servers.
     */
    Collection<Long> getUnavailableServers();

    /**
     * Gets a collection with all users the bot knows of.
     *
     * @return A collection with all users the bot knows of.
     */
    Collection<User> getUsers();

    /**
     * Gets a user by it's id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    Optional<User> getUserById(long id);
    
    /**
     * Gets a user by it's id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    default Optional<User> getUserById(String id) {
        try {
            return getUserById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all cached messages.
     *
     * @return A collection with all cached messages.
     */
    Collection<Message> getCachedMessages();

    /**
     * Gets a cached message by it's id.
     *
     * @param id The id of the message.
     * @return The cached message.
     */
    Optional<Message> getCachedMessageById(long id);

    /**
     * Gets a cached message by it's id.
     *
     * @param id The id of the message.
     * @return The cached message.
     */
    default Optional<Message> getCachedMessageById(String id) {
        try {
            return getCachedMessageById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all servers the bot is in.
     *
     * @return A collection with all servers the bot is in.
     */
    Collection<Server> getServers();

    /**
     * Gets a server by it's id.
     *
     * @param id The id of the server.
     * @return The server with the given id.
     */
    default Optional<Server> getServerById(long id) {
        return getServers().stream()
                .filter(server -> server.getId() == id)
                .findAny();
    }

    /**
     * Gets a server by it's id.
     *
     * @param id The id of the server.
     * @return The server with the given id.
     */
    default Optional<Server> getServerById(String id) {
        try {
            return getServerById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all channels of the bot.
     *
     * @return A collection with all channels of the bot.
     */
    default Collection<Channel> getChannels() {
        Collection<Channel> channels = new ArrayList<>();
        channels.addAll(getPrivateChannels());
        channels.addAll(getServerChannels());
        channels.addAll(getGroupChannels());
        return channels;
    }

    /**
     * Gets a collection with all group channels of the bot.
     *
     * @return A collection with all group channels of the bot.
     */
    default Collection<GroupChannel> getGroupChannels() {
        return new ArrayList<>();
    }

    /**
     * Gets a collection with all private channels of the bot.
     *
     * @return A collection with all private channels of the bot.
     */
    default Collection<PrivateChannel> getPrivateChannels() {
        return getUsers().stream()
                .map(User::getPrivateChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all server channels of the bot.
     *
     * @return A collection with all server channels of the bot.
     */
    default Collection<ServerChannel> getServerChannels() {
        Collection<ServerChannel> channels = new ArrayList<>();
        getServers().stream().forEach(server -> channels.addAll(server.getChannels()));
        return channels;
    }

    /**
     * Gets a collection with all server text channels of the bot.
     *
     * @return A collection with all server text channels of the bot.
     */
    default Collection<ServerTextChannel> getServerTextChannels() {
        Collection<ServerTextChannel> channels = new ArrayList<>();
        getServers().stream().forEach(server -> channels.addAll(server.getTextChannels()));
        return channels;
    }

    /**
     * Gets a collection with all server voice channels of the bot.
     *
     * @return A collection with all server voice channels of the bot.
     */
    default Collection<ServerVoiceChannel> getServerVoiceChannels() {
        Collection<ServerVoiceChannel> channels = new ArrayList<>();
        getServers().stream().forEach(server -> channels.addAll(server.getVoiceChannels()));
        return channels;
    }

    /**
     * Gets a collection with all text channels of the bot.
     *
     * @return A collection with all text channels of the bot.
     */
    default Collection<TextChannel> getTextChannels() {
        Collection<TextChannel> channels = new ArrayList<>();
        channels.addAll(getPrivateChannels());
        channels.addAll(getServerTextChannels());
        channels.addAll(getGroupChannels());
        return channels;
    }

    /**
     * Gets a collection with all voice channels of the bot.
     *
     * @return A collection with all voice channels of the bot.
     */
    default Collection<VoiceChannel> getVoiceChannels() {
        Collection<VoiceChannel> channels = new ArrayList<>();
        channels.addAll(getPrivateChannels());
        channels.addAll(getServerVoiceChannels());
        channels.addAll(getGroupChannels());
        return channels;
    }

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<Channel> getChannelById(long id) {
        return getChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<Channel> getChannelById(String id) {
        try {
            return getChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<TextChannel> getTextChannelById(long id) {
        return getTextChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<TextChannel> getTextChannelById(String id) {
        try {
            return getTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<VoiceChannel> getVoiceChannelById(long id) {
        return getVoiceChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<VoiceChannel> getVoiceChannelById(String id) {
        try {
            return getVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a server text channel by it's id.
     *
     * @param id The id of the server text channel.
     * @return The server text channel with the given id.
     */
    default Optional<ServerTextChannel> getServerTextChannelById(long id) {
        return getServerTextChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a server text channel by it's id.
     *
     * @param id The id of the server text channel.
     * @return The server text channel with the given id.
     */
    default Optional<ServerTextChannel> getServerTextChannelById(String id) {
        try {
            return getServerTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a server voice channel by it's id.
     *
     * @param id The id of the server voice channel.
     * @return The server voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getServerVoiceChannelById(long id) {
        return getServerVoiceChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a server voice channel by it's id.
     *
     * @param id The id of the server voice channel.
     * @return The server voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getServerVoiceChannelById(String id) {
        try {
            return getServerVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a private channel by it's id.
     *
     * @param id The id of the private channel.
     * @return The private channel with the given id.
     */
    default Optional<PrivateChannel> getPrivateChannelById(long id) {
        return getPrivateChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a private channel by it's id.
     *
     * @param id The id of the private channel.
     * @return The private channel with the given id.
     */
    default Optional<PrivateChannel> getPrivateChannelById(String id) {
        try {
            return getPrivateChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a group channel by it's id.
     *
     * @param id The id of the group channel.
     * @return The group channel with the given id.
     */
    default Optional<GroupChannel> getGroupChannelById(long id) {
        return getGroupChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a group channel by it's id.
     *
     * @param id The id of the group channel.
     * @return The group channel with the given id.
     */
    default Optional<GroupChannel> getGroupChannelById(String id) {
        try {
            return getGroupChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Adds a listener, which listens to global message creates.
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
     * Adds a listener, which listens to server joins.
     *
     * @param listener The listener to add.
     */
    void addServerJoinListener(ServerJoinListener listener);

    /**
     * Gets a list with all registered server join listeners.
     *
     * @return A list with all registered server join listeners.
     */
    List<ServerJoinListener> getServerJoinListeners();

    /**
     * Adds a listener, which listens to server leaves.
     *
     * @param listener The listener to add.
     */
    void addServerLeaveListener(ServerLeaveListener listener);

    /**
     * Gets a list with all registered server leaves listeners.
     *
     * @return A list with all registered server leaves listeners.
     */
    List<ServerLeaveListener> getServerLeaveListeners();

    /**
     * Adds a listener, which listens to servers becoming available.
     *
     * @param listener The listener to add.
     */
    void addServerBecomesAvailableListener(ServerBecomesAvailableListener listener);

    /**
     * Gets a list with all registered server becomes available listeners.
     *
     * @return A list with all registered server becomes available listeners.
     */
    List<ServerBecomesAvailableListener> getServerBecomesAvailableListeners();

    /**
     * Adds a listener, which listens to servers becoming unavailable.
     *
     * @param listener The listener to add.
     */
    void addServerBecomesUnavailableListener(ServerBecomesUnavailableListener listener);

    /**
     * Gets a list with all registered server becomes unavailable listeners.
     *
     * @return A list with all registered server becomes unavailable listeners.
     */
    List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners();

    /**
     * Adds a listener, which listens to users starting to type.
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

}
