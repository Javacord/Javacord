package de.btobastian.javacord;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.GameType;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.impl.ImplGame;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplCustomEmoji;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listeners.connection.LostConnectionListener;
import de.btobastian.javacord.listeners.connection.ReconnectListener;
import de.btobastian.javacord.listeners.connection.ResumeListener;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.*;
import de.btobastian.javacord.listeners.server.channel.*;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listeners.server.role.*;
import de.btobastian.javacord.listeners.user.*;
import de.btobastian.javacord.utils.DiscordWebsocketAdapter;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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
     * The http client for this instance.
     */
    private final OkHttpClient httpClient;

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
     * Whether the {@link #disconnect()} method has been called before or not.
     */
    private volatile boolean disconnectCalled = false;

    /**
     * A lock to synchronize on {@link ImplDiscordApi#disconnectCalled}.
     */
    private final Object disconnectCalledLock = new Object();

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
     * The function to calculate the reconnect delay.
     */
    private Function<Integer, Integer> reconnectDelayProvider;

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
     * The client id of the application.
     */
    private long clientId = -1;

    /**
     * The id of the application's owner.
     */
    private long ownerId = -1;

    /**
     * The time offset between the Discord time and our local time.
     */
    private Long timeOffset = null;

    /**
     * A map which contains all users.
     */
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    /**
     * A map which contains all servers.
     */
    private final ConcurrentHashMap<Long, Server> servers = new ConcurrentHashMap<>();

    /**
     * A map which contains all group channels.
     */
    private final ConcurrentHashMap<Long, GroupChannel> groupChannels = new ConcurrentHashMap<>();

    /**
     * A set with all unavailable servers.
     */
    private final HashSet<Long> unavailableServers = new HashSet<>();

    /**
     * A map with all known custom emoji.
     */
    private final ConcurrentHashMap<Long, CustomEmoji> customEmojis = new ConcurrentHashMap<>();

    /**
     * A map with all cached messages.
     */
    private final ConcurrentHashMap<Long, Message> messages = new ConcurrentHashMap<>();

    /**
     * A map which contains all listeners.
     * The key is the class of the listener.
     */
    private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

    /**
     * A map which contains all listeners which are assigned to a specific object instead of being global.
     * The key of the outer map is the class which the listener was registered to (e.g. Message.class).
     * The key of the first inner map is the id of the object.
     * The key of the second inner map is the class of the listener.
     * The final value is the listener itself.
     */
    private final ConcurrentHashMap<Class<?>, Map<Long, Map<Class<?>, List<Object>>>> objectListeners =
            new ConcurrentHashMap<>();

    /**
     * Creates a new discord api instance.
     *
     * @param accountType The account type of the instance.
     * @param token The token used to connect without any account type specific prefix.
     * @param currentShard The current shard the bot should connect to.
     * @param totalShards  The total amount of shards.
     * @param ready The future which will be completed when the connection to Discord was successful.
     */
    public ImplDiscordApi(
            AccountType accountType,
            String token,
            int currentShard,
            int totalShards,
            CompletableFuture<DiscordApi> ready
    ) {
        this.accountType = accountType;
        this.token = accountType.getTokenPrefix() + token;
        this.currentShard = currentShard;
        this.totalShards = totalShards;
        this.reconnectDelayProvider = x ->
                (int) Math.round(Math.pow(x, 1.5)-(1/(1/(0.1*x)+1))*Math.pow(x,1.5))+(currentShard*6);

        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", Javacord.USER_AGENT)
                        .build()))
                .build();

        RestEndpoint endpoint = RestEndpoint.GATEWAY_BOT;
        if (accountType == AccountType.CLIENT) {
            endpoint = RestEndpoint.GATEWAY;
        }

        new RestRequest<String>(this, RestMethod.GET, endpoint)
                .includeAuthorizationHeader(accountType == AccountType.BOT)
                .execute(result -> result.getJsonBody().get("url").asText())
                .whenComplete((gateway, t) -> {
                    if (t != null) {
                        ready.completeExceptionally(t);
                        return;
                    }

                    websocketAdapter = new DiscordWebsocketAdapter(this, gateway);
                    websocketAdapter.isReady().whenComplete((readyReceived, throwable) -> {
                        if (readyReceived) {
                            if (accountType == AccountType.BOT) {
                                getApplicationInfo().whenComplete((applicationInfo, exception) -> {
                                    if (exception != null) {
                                       logger.error("Could not access self application info on startup!", exception);
                                    } else {
                                        clientId = applicationInfo.getClientId();
                                        ownerId = applicationInfo.getOwnerId();
                                    }
                                    ready.complete(this);
                                });
                            } else {
                                ready.complete(this);
                            }
                        } else {
                            ready.completeExceptionally(
                                    new IllegalStateException("Websocket closed before READY packet was received!"));
                        }
                    });

                    getThreadPool().getScheduler().scheduleAtFixedRate(
                            () -> messages.entrySet().removeIf(entry -> !((ImplMessage) entry.getValue()).keepCached())
                            , 30, 30, TimeUnit.SECONDS);
                });

        // Add shutdown hook
        ready.thenAccept(api -> Runtime.getRuntime().addShutdownHook(new Thread(api::disconnect)));
    }

    /**
     * Purges all cached entities.
     * This method is only meant to be called after receiving a READY packet.
     */
    public void purgeCache() {
        users.clear();
        servers.clear();
        groupChannels.clear();
        unavailableServers.clear();
        customEmojis.clear();
        messages.clear();
        timeOffset = null;
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
     * Adds a group channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addGroupChannelToCache(GroupChannel channel) {
        groupChannels.put(channel.getId(), channel);
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
     * Gets the time offset between the Discord time and our local time.
     * Might be <code>null</code> if it hasn't been calculated yet.
     *
     * @return The time offset between the Discord time and our local time.
     */
    public Long getTimeOffset() {
        return timeOffset;
    }

    /**
     * Sets the time offset between the Discord time and our local time.
     *
     * @param timeOffset The time offset to set.
     */
    public void setTimeOffset(Long timeOffset) {
        this.timeOffset = timeOffset;
    }

    /**
     * Gets a user or creates a new one from the given data.
     *
     * @param data The json data of the user.
     * @return The user.
     */
    public User getOrCreateUser(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
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
     * Gets or creates a new custom emoji object.
     *
     * @param server The server of the emoji.
     * @param data The data of the emoji.
     * @return The emoji for the given json object.
     */
    public CustomEmoji getOrCreateCustomEmoji(Server server, JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        return customEmojis.computeIfAbsent(id, key -> new ImplCustomEmoji(this, server, data));
    }

    /**
     * Gets or creates a new message object.
     *
     * @param channel The channel of the message.
     * @param data The data of the message.
     * @return The message for the given json object.
     */
    public Message getOrCreateMessage(TextChannel channel, JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        // The constructor already adds the message to the cache.
        // If we use #computeIfAbsent() here, it would cause a deadlock
        return messages.getOrDefault(id, new ImplMessage(this, channel, data));
    }

    /**
     * Adds a message to the cache.
     *
     * @param message The message to add.
     */
    public void addMessageToCache(Message message) {
        messages.computeIfAbsent(message.getId(), key -> message);
    }

    /**
     * Sets the current game, along with type and streaming Url.
     *
     * @param name The name of the game.
     * @param type The game's type.
     * @param streamingUrl The Url used for streaming.
     */
    private void updateGame(String name, GameType type, String streamingUrl){
        if (name == null) {
            game = null;
        } else if (streamingUrl == null) {
            game = new ImplGame(type, name, null);
        } else {
            game = new ImplGame(type, name, streamingUrl);
        }
        websocketAdapter.updateStatus();
    }

    /**
     * Adds an object listener.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The manager for the added listener.
     */
    public <T> ListenerManager<T> addObjectListener(
            Class<?> objectClass, long objectId, Class<T> listenerClass, T listener) {
        synchronized (objectListeners) {
            Map<Long, Map<Class<?>, List<Object>>> objectListener =
                    objectListeners.computeIfAbsent(objectClass, key -> new ConcurrentHashMap<>());
            Map<Class<?>, List<Object>> listeners =
                    objectListener.computeIfAbsent(objectId, key -> new ConcurrentHashMap<>());
            List<Object> classListeners = listeners.computeIfAbsent(listenerClass, c -> new ArrayList<>());
            classListeners.add(listener);
        }
        return new ListenerManager<>(this, listener, listenerClass, objectClass, objectId);
    }

    /**
     * Removes an object listener.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     */
    public void removeObjectListener(Class<?> objectClass, long objectId, Class<?> listenerClass, Object listener) {
        synchronized (objectListeners) {
            Map<Long, Map<Class<?>, List<Object>>> objectListener = objectListeners.get(objectClass);
            if (objectClass == null) {
                return;
            }
            Map<Class<?>, List<Object>> listeners = objectListener.get(objectId);
            if (listeners == null) {
                return;
            }
            List<Object> classListeners = listeners.get(listenerClass);
            if (classListeners == null) {
                return;
            }
            classListeners.remove(listener);
            // Clean it up
            if (classListeners.isEmpty()) {
                listeners.remove(listenerClass);
                if (listeners.isEmpty()) {
                    objectListener.remove(objectId);
                    if (objectListener.isEmpty()) {
                        objectListeners.remove(objectClass);
                    }
                }
            }
        }
    }

    /**
     * Gets all object listeners of the given class.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param <T> The listener class.
     * @return A list with all object listeners of the given type.
     */
    @SuppressWarnings("unchecked") // We make sure it's the right type when adding elements
    public <T> List<T> getObjectListeners(Class<?> objectClass, long objectId, Class<?> listenerClass) {
        Map<Long, Map<Class<?>, List<Object>>> objectListener = objectListeners.get(objectClass);
        if (objectListener == null) {
            return Collections.emptyList();
        }
        Map<Class<?>, List<Object>> listeners = objectListener.get(objectId);
        if (listeners == null) {
            return Collections.emptyList();
        }
        List<Object> classListeners = listeners.getOrDefault(listenerClass, Collections.emptyList());
        return classListeners.stream().map(o -> (T) o).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Adds a listener.
     *
     * @param clazz The listener class.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The manager for the added listener.
     */
    public <T> ListenerManager<T> addListener(Class<T> clazz, T listener) {
        synchronized (listeners) {
            List<Object> classListeners = listeners.computeIfAbsent(clazz, c -> new ArrayList<>());
            classListeners.add(listener);
        }
        return new ListenerManager<>(this, listener, clazz);
    }

    /**
     * Removes a global listener.
     *
     * @param clazz The listener class.
     * @param listener The listener to remove.
     */
    public void removeListener(Class<?> clazz, Object listener) {
        synchronized (listeners) {
            List<Object> classListeners = listeners.get(clazz);
            if (classListeners != null) {
                classListeners.remove(listener);
                if (classListeners.isEmpty()) {
                    listeners.remove(clazz);
                }
            }
        }
    }

    /**
     * Gets all listeners of the given class.
     *
     * @param clazz The class of the listener.
     * @param <T> The class of the listener.
     * @return A list with all listeners of the given type.
     */
    @SuppressWarnings("unchecked") // We make sure it's the right type when adding elements
    public <T> List<T> getListeners(Class<?> clazz) {
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
    public OkHttpClient getHttpClient() {
        return httpClient;
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
    public AccountType getAccountType() {
        return accountType;
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
        updateGame(name, GameType.GAME, null);
    }

    @Override
    public void updateGame(String name, GameType type) {
        updateGame(name, type, null);
    }

    @Override
    public void updateGame(String name, String streamingUrl) {
        updateGame(name, GameType.STREAMING, streamingUrl);
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
    public long getOwnerId() {
        if (accountType != AccountType.BOT) {
            throw new IllegalStateException("Cannot get owner id of non bot accounts");
        }
        return ownerId;
    }

    @Override
    public long getClientId() {
        if (accountType != AccountType.BOT) {
            throw new IllegalStateException("Cannot get client id of non bot accounts");
        }
        return clientId;
    }

    @Override
    public void disconnect() {
        synchronized (disconnectCalledLock) {
            if (!disconnectCalled) {
                websocketAdapter.disconnect();
                threadPool.shutdown();
            }
            disconnectCalled = true;
        }
    }

    @Override
    public void setReconnectDelay(Function<Integer, Integer> reconnectDelayProvider) {
        this.reconnectDelayProvider = reconnectDelayProvider;
    }

    @Override
    public int getReconnectDelay(int attempt) {
        if (attempt < 0) {
            throw new IllegalArgumentException("attempt must be 1 or greater");
        }
        return reconnectDelayProvider.apply(attempt);
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
        return messages.values();
    }

    @Override
    public Optional<Message> getCachedMessageById(long id) {
        return Optional.ofNullable(messages.get(id));
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
    public Collection<CustomEmoji> getCustomEmojis() {
        return customEmojis.values();
    }

    @Override
    public Optional<CustomEmoji> getCustomEmojiById(long id) {
        return Optional.ofNullable(customEmojis.get(id));
    }

    @Override
    public Collection<GroupChannel> getGroupChannels() {
        return groupChannels.values();
    }

    @Override
    public Optional<GroupChannel> getGroupChannelById(long id) {
        return Optional.ofNullable(groupChannels.get(id));
    }

    @Override
    public ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return addListener(MessageCreateListener.class, listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return getListeners(MessageCreateListener.class);
    }

    @Override
    public ListenerManager<ServerJoinListener> addServerJoinListener(ServerJoinListener listener) {
        return addListener(ServerJoinListener.class, listener);
    }

    @Override
    public List<ServerJoinListener> getServerJoinListeners() {
        return getListeners(ServerJoinListener.class);
    }

    @Override
    public ListenerManager<ServerLeaveListener> addServerLeaveListener(ServerLeaveListener listener) {
        return addListener(ServerLeaveListener.class, listener);
    }

    @Override
    public List<ServerLeaveListener> getServerLeaveListeners() {
        return getListeners(ServerLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerBecomesAvailableListener> addServerBecomesAvailableListener(
            ServerBecomesAvailableListener listener) {
        return addListener(ServerBecomesAvailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesAvailableListener> getServerBecomesAvailableListeners() {
        return getListeners(ServerBecomesAvailableListener.class);
    }

    @Override
    public ListenerManager<ServerBecomesUnavailableListener> addServerBecomesUnavailableListener(
            ServerBecomesUnavailableListener listener) {
        return addListener(ServerBecomesUnavailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners() {
        return getListeners(ServerBecomesUnavailableListener.class);
    }

    @Override
    public ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return addListener(UserStartTypingListener.class, listener);
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {
        return getListeners(UserStartTypingListener.class);
    }

    @Override
    public ListenerManager<ServerChannelCreateListener> addServerChannelCreateListener(
            ServerChannelCreateListener listener) {
        return addListener(ServerChannelCreateListener.class, listener);
    }

    @Override
    public List<ServerChannelCreateListener> getServerChannelCreateListeners() {
        return getListeners(ServerChannelCreateListener.class);
    }

    @Override
    public ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(
            ServerChannelDeleteListener listener) {
        return addListener(ServerChannelDeleteListener.class, listener);
    }

    @Override
    public List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return getListeners(ServerChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {
        return addListener(MessageDeleteListener.class, listener);
    }

    @Override
    public ListenerManager<MessageDeleteListener> addMessageDeleteListener(
            long messageId, MessageDeleteListener listener) {
        return addObjectListener(Message.class, messageId, MessageDeleteListener.class, listener);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners() {
        return getListeners(MessageDeleteListener.class);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners(long messageId) {
        return getObjectListeners(Message.class, messageId, MessageDeleteListener.class);
    }

    @Override
    public ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {
        return addListener(MessageEditListener.class, listener);
    }

    @Override
    public ListenerManager<MessageEditListener> addMessageEditListener(long messageId, MessageEditListener listener) {
        return addObjectListener(Message.class, messageId, MessageEditListener.class, listener);
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners() {
        return getListeners(MessageEditListener.class);
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners(long messageId) {
        return getObjectListeners(Message.class, messageId, MessageEditListener.class);
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return addListener(ReactionAddListener.class, listener);
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(long messageId, ReactionAddListener listener) {
        return addObjectListener(Message.class, messageId, ReactionAddListener.class, listener);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners() {
        return getListeners(ReactionAddListener.class);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners(long messageId) {
        return getObjectListeners(Message.class, messageId, ReactionAddListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return addListener(ReactionRemoveListener.class, listener);
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(
            long messageId, ReactionRemoveListener listener) {
        return addObjectListener(Message.class, messageId, ReactionRemoveListener.class, listener);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners() {
        return getListeners(ReactionRemoveListener.class);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners(long messageId) {
        return getObjectListeners(Message.class, messageId, ReactionRemoveListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(ReactionRemoveAllListener listener) {
        return addListener(ReactionRemoveAllListener.class, listener);
    }

    @Override
    public ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(
            long messageId, ReactionRemoveAllListener listener) {
        return addObjectListener(Message.class, messageId, ReactionRemoveAllListener.class, listener);
    }

    @Override
    public List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {
        return getListeners(ReactionRemoveAllListener.class);
    }

    @Override
    public List<ReactionRemoveAllListener> getReactionRemoveAllListeners(long messageId) {
        return getObjectListeners(Message.class, messageId, ReactionRemoveAllListener.class);
    }

    @Override
    public ListenerManager<ServerMemberAddListener> addServerMemberAddListener(ServerMemberAddListener listener) {
        return addListener(ServerMemberAddListener.class, listener);
    }

    @Override
    public List<ServerMemberAddListener> getServerMemberAddListeners() {
        return getListeners(ServerMemberAddListener.class);
    }

    @Override
    public ListenerManager<ServerMemberRemoveListener> addServerMemberRemoveListener(
            ServerMemberRemoveListener listener) {
        return addListener(ServerMemberRemoveListener.class, listener);
    }

    @Override
    public List<ServerMemberRemoveListener> getServerMemberRemoveListeners() {
        return getListeners(ServerMemberRemoveListener.class);
    }

    @Override
    public ListenerManager<ServerChangeNameListener> addServerChangeNameListener(ServerChangeNameListener listener) {
        return addListener(ServerChangeNameListener.class, listener);
    }

    @Override
    public List<ServerChangeNameListener> getServerChangeNameListeners() {
        return getListeners(ServerChangeNameListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener) {
        return addListener(ServerChannelChangeNameListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
        return getListeners(ServerChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener) {
        return addListener(ServerChannelChangePositionListener.class, listener);
    }

    @Override
    public List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
        return getListeners(ServerChannelChangePositionListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiCreateListener> addCustomEmojiCreateListener(CustomEmojiCreateListener listener) {
        return addListener(CustomEmojiCreateListener.class, listener);
    }

    @Override
    public List<CustomEmojiCreateListener> getCustomEmojiCreateListeners() {
        return getListeners(CustomEmojiCreateListener.class);
    }

    @Override
    public ListenerManager<UserChangeGameListener> addUserChangeGameListener(UserChangeGameListener listener) {
        return addListener(UserChangeGameListener.class, listener);
    }

    @Override
    public List<UserChangeGameListener> getUserChangeGameListeners() {
        return getListeners(UserChangeGameListener.class);
    }

    @Override
    public ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener) {
        return addListener(UserChangeStatusListener.class, listener);
    }

    @Override
    public List<UserChangeStatusListener> getUserChangeStatusListeners() {
        return getListeners(UserChangeStatusListener.class);
    }

    @Override
    public ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener) {
        return addListener(RoleChangePermissionsListener.class, listener);
    }

    @Override
    public List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
        return getListeners(RoleChangePermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(
            RoleChangePositionListener listener) {
        return addListener(RoleChangePositionListener.class, listener);
    }

    @Override
    public List<RoleChangePositionListener> getRoleChangePositionListeners() {
        return getListeners(RoleChangePositionListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        return addListener(ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeOverwrittenPermissionsListener>
    getServerChannelChangeOverwrittenPermissionsListeners() {
        return getListeners(ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleCreateListener> addRoleCreateListener(RoleCreateListener listener) {
        return addListener(RoleCreateListener.class, listener);
    }

    @Override
    public List<RoleCreateListener> getRoleCreateListeners() {
        return getListeners(RoleCreateListener.class);
    }

    @Override
    public ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener) {
        return addListener(RoleDeleteListener.class, listener);
    }

    @Override
    public List<RoleDeleteListener> getRoleDeleteListeners() {
        return getListeners(RoleDeleteListener.class);
    }

    @Override
    public ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(
            UserChangeNicknameListener listener) {
        return addListener(UserChangeNicknameListener.class, listener);
    }

    @Override
    public List<UserChangeNicknameListener> getUserChangeNicknameListeners() {
        return getListeners(UserChangeNicknameListener.class);
    }

    @Override
    public ListenerManager<LostConnectionListener> addLostConnectionListener(LostConnectionListener listener) {
        return addListener(LostConnectionListener.class, listener);
    }

    @Override
    public List<LostConnectionListener> getLostConnectionListeners() {
        return getListeners(LostConnectionListener.class);
    }

    @Override
    public ListenerManager<ReconnectListener> addReconnectListener(ReconnectListener listener) {
        return addListener(ReconnectListener.class, listener);
    }

    @Override
    public List<ReconnectListener> getReconnectListeners() {
        return getListeners(ReconnectListener.class);
    }

    @Override
    public ListenerManager<ResumeListener> addResumeListener(ResumeListener listener) {
        return addListener(ResumeListener.class, listener);
    }

    @Override
    public List<ResumeListener> getResumeListeners() {
        return getListeners(ResumeListener.class);
    }

    @Override
    public ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return addListener(ServerTextChannelChangeTopicListener.class, listener);
    }

    @Override
    public List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return getListeners(ServerTextChannelChangeTopicListener.class);
    }

    @Override
    public ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return addListener(UserRoleAddListener.class, listener);
    }

    @Override
    public List<UserRoleAddListener> getUserRoleAddListeners() {
        return getListeners(UserRoleAddListener.class);
    }

    @Override
    public ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return addListener(UserRoleRemoveListener.class, listener);
    }

    @Override
    public List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return getListeners(UserRoleRemoveListener.class);
    }

    @Override
    public ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener) {
        return addListener(UserChangeNameListener.class, listener);
    }

    @Override
    public List<UserChangeNameListener> getUserChangeNameListeners() {
        return getListeners(UserChangeNameListener.class);
    }
}
