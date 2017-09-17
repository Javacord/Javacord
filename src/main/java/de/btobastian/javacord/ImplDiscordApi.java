package de.btobastian.javacord;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.GameType;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.impl.ImplGame;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.server.ServerBecomesAvailableListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerJoinListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.DiscordWebsocketAdapter;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The implementation of {@link DiscordApi}.
 */
public class ImplDiscordApi implements DiscordApi {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplDiscordApi.class);

    /**
     * The thread pool which is used internally.
     */
    private final ThreadPool threadPool = new ThreadPool();

    /**
     * The ratelimit manager for this bot.
     */
    private final RatelimitManager ratelimitManager = new RatelimitManager(this);

    /**
     * The websocket adapter used to connect to Discord.
     */
    private DiscordWebsocketAdapter websocketAdapter = null;

    /**
     * The account type of the bot.
     */
    private final AccountType accountType;

    /**
     * The token used for authentication.
     */
    private String token;

    /**
     * The game which is currently displayed. May be <code>null</code>.
     */
    private Game game;

    /**
     * The default message cache capacity which is applied for every newly created channel.
     */
    private int defaultMessageCacheCapacity = 50;

    /**
     * The default maximum age of cached messages.
     */
    private int defaultMessageCacheStorageTimeInSeconds = 60*60*12;

    /**
     * The current shard of the bot.
     */
    private final int currentShard;

    /**
     * The total amount of shards.
     */
    private final int totalShards;

    /**
     * The user of the connected account.
     */
    private User you;

    /**
     * A map which contains all users.
     */
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    /**
     * A map which contains all servers.
     */
    private final ConcurrentHashMap<Long, Server> servers = new ConcurrentHashMap<>();

    /**
     * A set with all unavailable servers.
     */
    private final HashSet<Long> unavailableServers = new HashSet<>();

    /**
     * A map with all cached messages.
     * The values use weak references as the strong references are hold inside the
     * {@link de.btobastian.javacord.utils.cache.MessageCache} classes.
     */
    private final ConcurrentHashMap<Long, WeakReference<Message>> weakMessages = new ConcurrentHashMap<>();

    /**
     * A map which contains all listeners.
     * The key is the class of the listener.
     */
    private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

    /**
     * Creates a new discord api instance.
     *
     * @param accountType The account type of the instance.
     * @param token The token used to connect without any account type specific prefix.
     * @param ready The future which will be completed when the connection to Discord was successful.
     */
    public ImplDiscordApi(
            AccountType accountType,
            String token,
            int currentShard,
            int totalShards,
            CompletableFuture<DiscordApi> ready
    ){
        this.accountType = accountType;
        this.token = accountType.getTokenPrefix() + token;
        this.currentShard = currentShard;
        this.totalShards = totalShards;

        RestEndpoint endpoint = RestEndpoint.GATEWAY_BOT;
        if (accountType == AccountType.CLIENT) {
            endpoint = RestEndpoint.GATEWAY;
        }

        new RestRequest<String>(this, HttpMethod.GET, endpoint)
                .execute(res -> res.getBody().getObject().getString("url"))
                .whenComplete((gateway, t) -> {
                    if (t != null) {
                        ready.completeExceptionally(t);
                        return;
                    }

                    websocketAdapter = new DiscordWebsocketAdapter(this, gateway);
                    websocketAdapter.isReady().whenComplete((readyReceived, throwable) -> {
                        if (readyReceived) {
                            ready.complete(this);
                        } else {
                            ready.completeExceptionally(
                                    new IllegalStateException("Websocket closed before READY packet was received!"));
                        }
                    });

                    getThreadPool().getScheduler().scheduleAtFixedRate(
                            () -> weakMessages.entrySet().removeIf(entry -> entry.getValue().get() == null)
                            , 30, 30, TimeUnit.SECONDS);
                });
    }

    /**
     * Adds the given server to the cache.
     *
     * @param server The server to add.
     */
    public void addServerToCache(Server server) {
        servers.put(server.getId(), server);
    }

    /**
     * Removes the given server from the cache.
     *
     * @param serverId The id of the server to remove.
     */
    public void removeServerFromCache(long serverId) {
        servers.remove(serverId);
    }

    /**
     * Adds the given user to the cache.
     *
     * @param user The user to add.
     */
    public void addUserToCache(User user) {
        users.put(user.getId(), user);
    }

    /**
     * Adds a server id to the list with unavailable servers.
     *
     * @param serverId The id of the server.
     */
    public void addUnavailableServerToCache(long serverId) {
        unavailableServers.add(serverId);
    }

    /**
     * Removes a server id from the list with unavailable servers.
     *
     * @param serverId The id of the server.
     */
    public void removeUnavailableServerToCache(long serverId) {
        unavailableServers.remove(serverId);
    }

    /**
     * Sets the user of the connected account.
     *
     * @param yourself The user of the connected account.
     */
    public void setYourself(User yourself){
        you = yourself;
    }

    /**
     * Gets a user or creates a new one from the given data.
     *
     * @param data The json data of the user.
     * @return The user.
     */
    public User getOrCreateUser(JSONObject data) {
        long id = Long.parseLong(data.getString("id"));
        synchronized (this) {
            return getUserById(id).orElseGet(() -> {
                if (!data.has("username")) {
                    throw new IllegalStateException("Couldn't get or created user. Please inform the developer!");
                }
                return new ImplUser(this, data);
            });
        }
    }

    /**
     * Gets or creates new message object.
     *
     * @param channel The channel of the message.
     * @param data The data of the message.
     * @return The message for the given json object.
     */
    public Message getOrCreateMessage(TextChannel channel, JSONObject data) {
        long id = Long.parseLong(data.getString("id"));
        synchronized (weakMessages) {
            WeakReference<Message> value = weakMessages.get(id);
            Message message = value != null ? value.get() : null;
            if (message == null) {
                weakMessages.remove(id);
                message = new ImplMessage(this, channel, data);
                weakMessages.put(id, new WeakReference<>(message));
            }
            return message;
        }
    }

    /**
     * Adds a listener.
     *
     * @param clazz The listener class.
     * @param listener The listener to add.
     */
    private void addListener(Class<?> clazz, Object listener) {
        List<Object> classListeners = listeners.computeIfAbsent(clazz, c -> new ArrayList<>());
        classListeners.add(listener);
    }

    /**
     * Gets all listeners of the given class.
     *
     * @param clazz The class of the listener.
     * @param <T> The class of the listener.
     * @return A list with all listeners of the given type.
     */
    @SuppressWarnings("unchecked") // We make sure it's the right type when adding elements
    private <T> List<T> getListeners(Class<?> clazz) {
        List<Object> classListeners = listeners.getOrDefault(clazz, new ArrayList<>());
        return classListeners.stream().map(o -> (T) o).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    @Override
    public RatelimitManager getRatelimitManager() {
        return ratelimitManager;
    }

    /*
     * Note: You might think the return type should be Optional<WebsocketAdapter>, because it's null till we receive
     *       the gateway from Discord. However the DiscordApi instance is only passed to the user, AFTER we connect
     *       so for the end user it is in fact never null.
     */
    @Override
    public DiscordWebsocketAdapter getWebSocketAdapter() {
        return websocketAdapter;
    }

    @Override
    public void setMessageCacheSize(int capacity, int storageTimeInSeconds) {
        this.defaultMessageCacheCapacity = capacity;
        this.defaultMessageCacheStorageTimeInSeconds = storageTimeInSeconds;
        getChannels().stream()
                .filter(channel -> channel instanceof TextChannel)
                .map(channel -> (TextChannel) channel)
                .forEach(channel -> {
                    channel.getMessageCache().setCapacity(capacity);
                    channel.getMessageCache().setStorageTimeInSeconds(storageTimeInSeconds);
                });
    }

    @Override
    public int getDefaultMessageCacheCapacity() {
        return defaultMessageCacheCapacity;
    }

    @Override
    public int getDefaultMessageCacheStorageTimeInSeconds() {
        return defaultMessageCacheStorageTimeInSeconds;
    }

    @Override
    public int getCurrentShard() {
        return currentShard;
    }

    @Override
    public int getTotalShards() {
        return totalShards;
    }

    @Override
    public void updateGame(String name) {
        updateGame(name, null);
    }

    @Override
    public void updateGame(String name, String streamingUrl) {
        if (name == null) {
            game = null;
        } else if (streamingUrl == null) {
            game = new ImplGame(GameType.GAME, name, null);
        } else {
            game = new ImplGame(GameType.STREAMING, name, streamingUrl);
        }
        websocketAdapter.updateStatus();
    }

    @Override
    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    @Override
    public User getYourself(){
        return you;
    }

    @Override
    public void disconnect() {
        websocketAdapter.disconnect();
        threadPool.shutdown();
    }

    @Override
    public void setReconnectRatelimit(int attempts, int seconds) {
        websocketAdapter.setReconnectAttempts(attempts);
        websocketAdapter.setRatelimitResetIntervalInSeconds(seconds);
    }

    @Override
    public Collection<Long> getUnavailableServers() {
        return Collections.unmodifiableCollection(unavailableServers);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<Message> getCachedMessages() {
        return weakMessages.values().stream()
                .map(Reference::get)
                .filter(message -> message != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<Message> getCachedMessageById(long id) {
        WeakReference<Message> message = weakMessages.get(id);
        return Optional.ofNullable(message == null ? null : message.get());
    }

    @Override
    public Collection<Server> getServers() {
        return servers.values();
    }

    @Override
    public Optional<Server> getServerById(long id) {
        return Optional.ofNullable(servers.get(id));
    }

    @Override
    public void addMessageCreateListener(MessageCreateListener listener) {
        addListener(MessageCreateListener.class, listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return getListeners(MessageCreateListener.class);
    }

    @Override
    public void addServerJoinListener(ServerJoinListener listener) {
        addListener(ServerJoinListener.class, listener);
    }

    @Override
    public List<ServerJoinListener> getServerJoinListeners() {
        return getListeners(ServerJoinListener.class);
    }

    @Override
    public void addServerLeaveListener(ServerLeaveListener listener) {
        addListener(ServerLeaveListener.class, listener);
    }

    @Override
    public List<ServerLeaveListener> getServerLeaveListeners() {
        return getListeners(ServerLeaveListener.class);
    }

    @Override
    public void addServerBecomesAvailableListener(ServerBecomesAvailableListener listener) {
        addListener(ServerBecomesAvailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesAvailableListener> getServerBecomesAvailableListeners() {
        return getListeners(ServerBecomesAvailableListener.class);
    }

    @Override
    public void addServerBecomesUnavailableListener(ServerBecomesUnavailableListener listener) {
        addListener(ServerBecomesUnavailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners() {
        return getListeners(ServerBecomesUnavailableListener.class);
    }

    @Override
    public void addUserStartTypingListener(UserStartTypingListener listener) {
        addListener(UserStartTypingListener.class, listener);
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {
        return getListeners(UserStartTypingListener.class);
    }

    @Override
    public void addServerChannelCreateListener(ServerChannelCreateListener listener) {
        addListener(ServerChannelCreateListener.class, listener);
    }

    @Override
    public List<ServerChannelCreateListener> getServerChannelCreateListeners() {
        return getListeners(ServerChannelCreateListener.class);
    }

    @Override
    public void addServerChannelDeleteListener(ServerChannelDeleteListener listener) {
        addListener(ServerChannelDeleteListener.class, listener);
    }

    @Override
    public List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return getListeners(ServerChannelDeleteListener.class);
    }

    @Override
    public void addMessageDeleteListener(MessageDeleteListener listener) {
        addListener(MessageDeleteListener.class, listener);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners() {
        return getListeners(MessageDeleteListener.class);
    }

    @Override
    public void addMessageEditListener(MessageEditListener listener) {
        addListener(MessageEditListener.class, listener);
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners() {
        return getListeners(MessageEditListener.class);
    }
}
