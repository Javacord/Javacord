package de.btobastian.javacord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;
import de.btobastian.javacord.entities.Activity;
import de.btobastian.javacord.entities.ActivityType;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.impl.ImplActivity;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplCustomEmoji;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listeners.connection.LostConnectionListener;
import de.btobastian.javacord.listeners.connection.ReconnectListener;
import de.btobastian.javacord.listeners.connection.ResumeListener;
import de.btobastian.javacord.listeners.group.channel.GroupChannelChangeNameListener;
import de.btobastian.javacord.listeners.group.channel.GroupChannelCreateListener;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerBecomesAvailableListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerChangeDefaultMessageNotificationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeExplicitContentFilterLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeIconListener;
import de.btobastian.javacord.listeners.server.ServerChangeMultiFactorAuthenticationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeNameListener;
import de.btobastian.javacord.listeners.server.ServerChangeOwnerListener;
import de.btobastian.javacord.listeners.server.ServerChangeRegionListener;
import de.btobastian.javacord.listeners.server.ServerChangeVerificationLevelListener;
import de.btobastian.javacord.listeners.server.ServerJoinListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.server.channel.ServerTextChannelChangeTopicListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeNameListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiDeleteListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberBanListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberJoinListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberLeaveListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberUnbanListener;
import de.btobastian.javacord.listeners.server.role.RoleChangePermissionsListener;
import de.btobastian.javacord.listeners.server.role.RoleChangePositionListener;
import de.btobastian.javacord.listeners.server.role.RoleCreateListener;
import de.btobastian.javacord.listeners.server.role.RoleDeleteListener;
import de.btobastian.javacord.listeners.server.role.UserRoleAddListener;
import de.btobastian.javacord.listeners.server.role.UserRoleRemoveListener;
import de.btobastian.javacord.listeners.user.UserChangeActivityListener;
import de.btobastian.javacord.listeners.user.UserChangeAvatarListener;
import de.btobastian.javacord.listeners.user.UserChangeNameListener;
import de.btobastian.javacord.listeners.user.UserChangeNicknameListener;
import de.btobastian.javacord.listeners.user.UserChangeStatusListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelCreateListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelDeleteListener;
import de.btobastian.javacord.utils.Cleanupable;
import de.btobastian.javacord.utils.DiscordWebSocketAdapter;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
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
     * The object mapper for this instance.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * The ratelimit manager for this bot.
     */
    private final RatelimitManager ratelimitManager = new RatelimitManager(this);

    /**
     * The websocket adapter used to connect to Discord.
     */
    private DiscordWebSocketAdapter websocketAdapter = null;

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
     * The status which should be displayed for the bot.
     */
    private UserStatus status = UserStatus.ONLINE;

    /**
     * The activity which should be displayed. May be <code>null</code>.
     */
    private Activity activity;

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
    private final Map<Long, WeakReference<Message>> messages = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * A map to retrieve message IDs by the weak ref that point to the respective
     * message or used to point to it for usage in the messages cleanup,
     * as at cleanup time the weak ref is already emptied.
     */
    private final Map<Reference<? extends Message>, Long> messageIdByRef = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * The queue that is notified if a message became weakly-reachable.
     */
    private final ReferenceQueue<Message> messagesCleanupQueue = new ReferenceQueue<>();

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
     * Creates a new discord api instance that can be used for auto-ratelimited REST calls,
     * but does not connect to the Discord WebSocket.
     *
     * @param token The token used to connect without any account type specific prefix.
     */
    public ImplDiscordApi(String token) {
        this(AccountType.BOT, token, 0, 1, null);
    }

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

        if (ready != null) {
            getThreadPool().getExecutorService().submit(() -> {
                try {
                    this.websocketAdapter = new DiscordWebSocketAdapter(this);
                    this.websocketAdapter.isReady().whenComplete((readyReceived, throwable) -> {
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
                } catch (Throwable t) {
                    if (websocketAdapter != null) {
                        websocketAdapter.disconnect();
                    }
                    ready.completeExceptionally(t);
                }
            });

            // After minimum JDK 9 is required this can be switched to use a Cleaner
            getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
                for (Reference<? extends Message> messageRef = messagesCleanupQueue.poll();
                     messageRef != null;
                     messageRef = messagesCleanupQueue.poll()) {
                    Long messageId = messageIdByRef.remove(messageRef);
                    if (messageId != null) {
                        messages.remove(messageId, messageRef);
                    }
                }
            }, 30, 30, TimeUnit.SECONDS);

            // Add shutdown hook
            ready.thenAccept(api -> {
                WeakReference<DiscordApi> discordApiReference = new WeakReference<>(api);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> Optional.ofNullable(discordApiReference.get()).ifPresent(DiscordApi::disconnect),
                                                                String.format("Javacord - Shutdown Disconnector (%s)", api)));
            });
        } else {
            WeakReference<DiscordApi> discordApiReference = new WeakReference<>(this);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> Optional.ofNullable(discordApiReference.get()).ifPresent(DiscordApi::disconnect),
                                                            String.format("Javacord - Shutdown Disconnector (%s)", this)));
        }
    }

    @Override
    protected void finalize() {
        disconnect();
    }

    /**
     * Purges all cached entities.
     * This method is only meant to be called after receiving a READY packet.
     */
    public void purgeCache() {
        users.values().stream()
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
        users.clear();
        servers.values().stream()
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
        servers.clear();
        groupChannels.values().stream()
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
        groupChannels.clear();
        unavailableServers.clear();
        customEmojis.clear();
        messages.clear();
        messageIdByRef.clear();
        timeOffset = null;
    }

    /**
     * Adds the given server to the cache.
     *
     * @param server The server to add.
     */
    public void addServerToCache(Server server) {
        Server oldServer = servers.put(server.getId(), server);
        if ((oldServer != null) && (oldServer != server)) {
            ((Cleanupable) oldServer).cleanup();
        }
    }

    /**
     * Removes the given server from the cache.
     *
     * @param serverId The id of the server to remove.
     */
    public void removeServerFromCache(long serverId) {
        servers.computeIfPresent(serverId, (key, server) -> {
            ((Cleanupable) server).cleanup();
            return null;
        });
    }

    /**
     * Adds the given user to the cache.
     *
     * @param user The user to add.
     */
    public void addUserToCache(User user) {
        User oldUser = users.put(user.getId(), user);
        if ((oldUser != null) && (oldUser != user)) {
            ((Cleanupable) oldUser).cleanup();
        }
    }

    /**
     * Adds a group channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addGroupChannelToCache(GroupChannel channel) {
        GroupChannel oldChannel = groupChannels.put(channel.getId(), channel);
        if ((oldChannel != null) && (oldChannel != channel)) {
            ((Cleanupable) oldChannel).cleanup();
        }
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
    public void removeUnavailableServerFromCache(long serverId) {
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
            return getCachedUserById(id).orElseGet(() -> {
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
        // If we use #compute() here, it would cause a deadlock
        synchronized (messages) {
            WeakReference<Message> resultRef = messages.get(id);
            // If the map has no entry or the weak ref was still in the map but cleared, create a new message
            return (resultRef == null) || (resultRef.get() == null)
                   ? new ImplMessage(this, channel, data)
                   : resultRef.get();
        }
    }

    /**
     * Adds a message to the cache.
     *
     * @param message The message to add.
     */
    public void addMessageToCache(Message message) {
        messages.compute(message.getId(), (key, value) -> {
            if ((value == null) || (value.get() == null)) {
                WeakReference<Message> result = new WeakReference<>(message, messagesCleanupQueue);
                messageIdByRef.put(result, message.getId());
                return result;
            }
            return value;
        });
    }

    /**
     * Removes a message from the cache.
     *
     * @param messageId The id of the message to remove.
     */
    public void removeMessageFromCache(long messageId) {
        WeakReference<Message> messageRef = messages.remove(messageId);
        if (messageRef != null) {
            messageIdByRef.remove(messageRef, messageId);
        }
    }

    /**
     * Sets the current activity, along with type and streaming Url.
     *
     * @param name The name of the activity.
     * @param type The activity's type.
     * @param streamingUrl The Url used for streaming.
     */
    private void updateActivity(String name, ActivityType type, String streamingUrl){
        if (name == null) {
            activity = null;
        } else if (streamingUrl == null) {
            activity = new ImplActivity(type, name, null);
        } else {
            activity = new ImplActivity(type, name, streamingUrl);
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
            if (objectClass == null) {
                return;
            }
            Map<Long, Map<Class<?>, List<Object>>> objectListener = objectListeners.get(objectClass);
            if (objectListener == null) {
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
    public ObjectMapper getObjectMapper() {
        return objectMapper;
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
    public DiscordWebSocketAdapter getWebSocketAdapter() {
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
    public void updateStatus(UserStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("The status cannot be null");
        }
        this.status = status;
        websocketAdapter.updateStatus();
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    @Override
    public void updateActivity(String name) {
        updateActivity(name, ActivityType.PLAYING, null);
    }

    @Override
    public void updateActivity(String name, ActivityType type) {
        updateActivity(name, type, null);
    }

    @Override
    public void updateActivity(String name, String streamingUrl) {
        updateActivity(name, ActivityType.STREAMING, streamingUrl);
    }

    @Override
    public Optional<Activity> getActivity() {
        return Optional.ofNullable(activity);
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
                if (websocketAdapter == null) {
                    // if no web socket is connected, immediately shutdown thread pool
                    threadPool.shutdown();
                } else {
                    // shutdown thread pool after web socket disconnected event was dispatched
                    websocketAdapter.getWebSocket().addListener(new WebSocketAdapter() {
                        @Override
                        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                                   WebSocketFrame clientCloseFrame,
                                                   boolean closedByServer) throws Exception {
                            threadPool.shutdown();
                        }
                    });
                    // disconnect web socket
                    websocketAdapter.disconnect();
                    // shutdown thread pool if within one minute no disconnect event was dispatched
                    threadPool.getScheduler().schedule(threadPool::shutdown, 1, TimeUnit.MINUTES);
                }
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
    public Collection<User> getCachedUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getCachedUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public CompletableFuture<User> getUserById(long id) {
        return getCachedUserById(id)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> new RestRequest<User>(this, RestMethod.GET, RestEndpoint.USER)
                .setUrlParameters(String.valueOf(id))
                .execute(result -> this.getOrCreateUser(result.getJsonBody())));
    }

    @Override
    public Collection<Message> getCachedMessages() {
        synchronized (messages) {
            return messages.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Optional<Message> getCachedMessageById(long id) {
        return Optional.ofNullable(messages.get(id)).map(Reference::get);
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
    public ListenerManager<PrivateChannelCreateListener> addPrivateChannelCreateListener(
            PrivateChannelCreateListener listener) {
        return addListener(PrivateChannelCreateListener.class, listener);
    }

    @Override
    public List<PrivateChannelCreateListener> getPrivateChannelCreateListeners() {
        return getListeners(PrivateChannelCreateListener.class);
    }

    @Override
    public ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener) {
        return addListener(PrivateChannelDeleteListener.class, listener);
    }

    @Override
    public List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners() {
        return getListeners(PrivateChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<GroupChannelCreateListener> addGroupChannelCreateListener(
            GroupChannelCreateListener listener) {
        return addListener(GroupChannelCreateListener.class, listener);
    }

    @Override
    public List<GroupChannelCreateListener> getGroupChannelCreateListeners() {
        return getListeners(GroupChannelCreateListener.class);
    }

    @Override
    public ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener) {
        return addListener(GroupChannelChangeNameListener.class, listener);
    }

    @Override
    public ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(
            GroupChannelDeleteListener listener) {
        return addListener(GroupChannelDeleteListener.class, listener);
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
    public ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener) {
        return addListener(ServerMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerMemberJoinListener> getServerMemberJoinListeners() {
        return getListeners(ServerMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(
            ServerMemberLeaveListener listener) {
        return addListener(ServerMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerMemberLeaveListener> getServerMemberLeaveListeners() {
        return getListeners(ServerMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener) {
        return addListener(ServerMemberBanListener.class, listener);
    }

    @Override
    public List<ServerMemberBanListener> getServerMemberBanListeners() {
        return getListeners(ServerMemberBanListener.class);
    }

    @Override
    public ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener) {
        return addListener(ServerMemberUnbanListener.class, listener);
    }

    @Override
    public List<ServerMemberUnbanListener> getServerMemberUnbanListeners() {
        return getListeners(ServerMemberUnbanListener.class);
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
    public ListenerManager<ServerChangeIconListener> addServerChangeIconListener(ServerChangeIconListener listener) {
        return addListener(ServerChangeIconListener.class, listener);
    }

    @Override
    public List<ServerChangeIconListener> getServerChangeIconListeners() {
        return getListeners(ServerChangeIconListener.class);
    }

    @Override
    public ListenerManager<ServerChangeVerificationLevelListener> addServerChangeVerificationLevelListener(
            ServerChangeVerificationLevelListener listener) {
        return addListener(ServerChangeVerificationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeVerificationLevelListener> getServerChangeVerificationLevelListeners() {
        return getListeners(ServerChangeVerificationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeRegionListener> addServerChangeRegionListener(
            ServerChangeRegionListener listener) {
        return addListener(ServerChangeRegionListener.class, listener);
    }

    @Override
    public List<ServerChangeRegionListener> getServerChangeRegionListeners() {
        return getListeners(ServerChangeRegionListener.class);
    }

    @Override
    public ListenerManager<ServerChangeDefaultMessageNotificationLevelListener>
    addServerChangeDefaultMessageNotificationLevelListener(
            ServerChangeDefaultMessageNotificationLevelListener listener) {
        return addListener(ServerChangeDefaultMessageNotificationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeDefaultMessageNotificationLevelListener>
    getServerChangeDefaultMessageNotificationLevelListeners() {
        return getListeners(ServerChangeDefaultMessageNotificationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeMultiFactorAuthenticationLevelListener>
    addServerChangeMultiFactorAuthenticationLevelListener(ServerChangeMultiFactorAuthenticationLevelListener listener) {
        return addListener(ServerChangeMultiFactorAuthenticationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeMultiFactorAuthenticationLevelListener>
    getServerChangeMultiFactorAuthenticationLevelListeners() {
        return getListeners(ServerChangeMultiFactorAuthenticationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeOwnerListener> addServerChangeOwnerListener(ServerChangeOwnerListener listener) {
        return addListener(ServerChangeOwnerListener.class, listener);
    }

    @Override
    public List<ServerChangeOwnerListener> getServerChangeOwnerListeners() {
        return getListeners(ServerChangeOwnerListener.class);
    }

    @Override
    public ListenerManager<ServerChangeExplicitContentFilterLevelListener>
    addServerChangeExplicitContentFilterLevelListener(ServerChangeExplicitContentFilterLevelListener listener) {
        return addListener(ServerChangeExplicitContentFilterLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeExplicitContentFilterLevelListener> getServerChangeExplicitContentFilterLevelListeners() {
        return getListeners(ServerChangeExplicitContentFilterLevelListener.class);
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
    public ListenerManager<CustomEmojiChangeNameListener> addCustomEmojiChangeNameListener(
            CustomEmojiChangeNameListener listener) {
        return addListener(CustomEmojiChangeNameListener.class, listener);
    }

    @Override
    public List<CustomEmojiChangeNameListener> getCustomEmojiChangeNameListeners() {
        return getListeners(CustomEmojiChangeNameListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiDeleteListener> addCustomEmojiDeleteListener(CustomEmojiDeleteListener listener) {
        return addListener(CustomEmojiDeleteListener.class, listener);
    }

    @Override
    public List<CustomEmojiDeleteListener> getCustomEmojiDeleteListeners() {
        return getListeners(CustomEmojiDeleteListener.class);
    }

    @Override
    public ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(UserChangeActivityListener listener) {
        return addListener(UserChangeActivityListener.class, listener);
    }

    @Override
    public List<UserChangeActivityListener> getUserChangeActivityListeners() {
        return getListeners(UserChangeActivityListener.class);
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

    @Override
    public ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener) {
        return addListener(UserChangeAvatarListener.class, listener);
    }

    @Override
    public List<UserChangeAvatarListener> getUserChangeAvatarListeners() {
        return getListeners(UserChangeAvatarListener.class);
    }
}
