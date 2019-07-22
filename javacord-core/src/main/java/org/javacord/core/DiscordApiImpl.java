package org.javacord.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Dns;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.apache.logging.log4j.Logger;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.ApplicationInfo;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.UncachedMessageUtil;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.auth.Authenticator;
import org.javacord.api.util.concurrent.ThreadPool;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.entity.activity.ActivityImpl;
import org.javacord.core.entity.activity.ApplicationInfoImpl;
import org.javacord.core.entity.emoji.CustomEmojiImpl;
import org.javacord.core.entity.emoji.KnownCustomEmojiImpl;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.entity.message.MessageSetImpl;
import org.javacord.core.entity.message.UncachedMessageUtilImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.server.invite.InviteImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.entity.webhook.WebhookImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.concurrent.ThreadPoolImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.event.EventDispatcher;
import org.javacord.core.util.event.ListenerManagerImpl;
import org.javacord.core.util.gateway.DiscordWebSocketAdapter;
import org.javacord.core.util.http.ProxyAuthenticator;
import org.javacord.core.util.http.TrustAllTrustManager;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.ratelimit.RatelimitManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The implementation of {@link DiscordApi}.
 */
public class DiscordApiImpl implements DiscordApi, DispatchQueueSelector {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordApiImpl.class);

    /**
     * The thread pool which is used internally.
     */
    private final ThreadPoolImpl threadPool = new ThreadPoolImpl();

    /**
     * The http client for this instance.
     */
    private final OkHttpClient httpClient;

    /**
     * The event dispatcher.
     */
    private final EventDispatcher eventDispatcher;

    /**
     * The object mapper for this instance.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * The ratelimit manager for this bot.
     */
    private final RatelimitManager ratelimitManager = new RatelimitManager(this);

    /**
     * The utility class to interact with uncached messages.
     */
    private final UncachedMessageUtil uncachedMessageUtil = new UncachedMessageUtilImpl(this);

    /**
     * The websocket adapter used to connect to Discord.
     */
    private volatile DiscordWebSocketAdapter websocketAdapter = null;

    /**
     * The account type of the bot.
     */
    private final AccountType accountType;

    /**
     * The token used for authentication.
     */
    private final String token;

    /**
     * Whether the {@link #disconnect()} method has been called before or not.
     */
    private volatile boolean disconnectCalled = false;

    /**
     * A lock to synchronize on {@link DiscordApiImpl#disconnectCalled}.
     */
    private final Object disconnectCalledLock = new Object();

    /**
     * The status which should be displayed for the bot.
     */
    private volatile UserStatus status = UserStatus.ONLINE;

    /**
     * The activity which should be displayed. May be <code>null</code>.
     */
    private volatile Activity activity;

    /**
     * The default message cache capacity which is applied for every newly created channel.
     */
    private volatile int defaultMessageCacheCapacity = 50;

    /**
     * The default maximum age of cached messages.
     */
    private volatile int defaultMessageCacheStorageTimeInSeconds = 60 * 60 * 12;

    /**
     * Whether automatic message cache cleanup is enabled by default.
     */
    private boolean defaultAutomaticMessageCacheCleanupEnabled = true;

    /**
     * The function to calculate the reconnect delay.
     */
    private volatile Function<Integer, Integer> reconnectDelayProvider;

    /**
     * The current shard of the bot.
     */
    private final int currentShard;

    /**
     * The total amount of shards.
     */
    private final int totalShards;

    /**
     * Whether Javacord should wait for all servers to become available on startup or not.
     */
    private final boolean waitForServersOnStartup;

    /**
     * The proxy selector which should be used to determine the proxies that should be used to connect to the Discord
     * REST API and websocket.
     */
    private final ProxySelector proxySelector;

    /**
     * The proxy which should be used to connect to the Discord REST API and websocket.
     */
    private final Proxy proxy;

    /**
     * The authenticator that should be used to authenticate against proxies that require it.
     */
    private final Authenticator proxyAuthenticator;

    /**
     * Whether to trust all SSL certificates.
     */
    private final boolean trustAllCertificates;

    /**
     * The user of the connected account.
     */
    private volatile User you;

    /**
     * The client id of the application.
     */
    private volatile long clientId = -1;

    /**
     * The id of the application's owner.
     */
    private volatile long ownerId = -1;

    /**
     * The time offset between the Discord time and our local time.
     */
    private volatile Long timeOffset = null;

    /**
     * A map which contains all users.
     */
    private final Map<Long, WeakReference<User>> users = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * A map to retrieve user IDs by the weak ref that point to the respective
     * user or used to point to it for usage in the users cleanup,
     * as at cleanup time the weak ref is already emptied.
     */
    private final Map<Reference<? extends User>, Long> userIdByRef = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * The queue that is notified if a user became weakly-reachable.
     */
    private final ReferenceQueue<User> usersCleanupQueue = new ReferenceQueue<>();

    /**
     * Allows for a quick lookup for channels by their id.
     */
    private final ConcurrentHashMap<Long, Channel> channels = new ConcurrentHashMap<>();

    /**
     * A map which contains all servers that are ready.
     */
    private final ConcurrentHashMap<Long, Server> servers = new ConcurrentHashMap<>();

    /**
     * A map which contains all servers that are not ready.
     */
    private final ConcurrentHashMap<Long, Server> nonReadyServers = new ConcurrentHashMap<>();

    /**
     * A set with all unavailable servers.
     */
    private final HashSet<Long> unavailableServers = new HashSet<>();

    /**
     * A map with all known custom emoji.
     */
    private final ConcurrentHashMap<Long, KnownCustomEmoji> customEmojis = new ConcurrentHashMap<>();

    /**
     * A map with all cached messages.
     */
    private final Map<Long, WeakReference<Message>> messages = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * A map to retrieve message IDs by the weak ref that point to the respective
     * message or used to point to it for usage in the messages cleanup,
     * as at cleanup time the weak ref is already emptied.
     */
    private final Map<Reference<? extends Message>, Long> messageIdByRef
            = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * The queue that is notified if a message became weakly-reachable.
     */
    private final ReferenceQueue<Message> messagesCleanupQueue = new ReferenceQueue<>();

    /**
     * A map which contains all globally attachable listeners.
     * The key is the class of the listener.
     */
    private final Map<Class<? extends GloballyAttachableListener>,
            Map<GloballyAttachableListener, ListenerManagerImpl<? extends GloballyAttachableListener>>>
            listeners = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * A map which contains all listeners which are assigned to a specific object instead of being global.
     * The key of the outer map is the class which the listener was registered to (e.g. Message.class).
     * The key of the first inner map is the id of the object.
     * The key of the second inner map is the class of the listener.
     * The key of the third inner map is the listener itself.
     * The final value is the listener manager.
     */
    private final Map<Class<?>, Map<Long, Map<Class<? extends ObjectAttachableListener>,
            Map<ObjectAttachableListener, ListenerManagerImpl<? extends ObjectAttachableListener>>>>>
            objectListeners = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * Creates a new discord api instance that can be used for auto-ratelimited REST calls,
     * but does not connect to the Discord WebSocket.
     *
     * @param token                The token used to connect without any account type specific prefix.
     * @param proxySelector        The proxy selector which should be used to determine the proxies that should be used
     *                             to connect to the Discord REST API and websocket.
     * @param proxy                The proxy which should be used to connect to the Discord REST API and websocket.
     * @param proxyAuthenticator   The authenticator that should be used to authenticate against proxies that require
     *                             it.
     * @param trustAllCertificates Whether to trust all SSL certificates.
     */
    public DiscordApiImpl(String token, ProxySelector proxySelector, Proxy proxy, Authenticator proxyAuthenticator,
                          boolean trustAllCertificates) {
        this(AccountType.BOT, token, 0, 1, false, proxySelector, proxy, proxyAuthenticator, trustAllCertificates, null);
    }

    /**
     * Creates a new discord api instance.
     *
     * @param accountType             The account type of the instance.
     * @param token                   The token used to connect without any account type specific prefix.
     * @param currentShard            The current shard the bot should connect to.
     * @param totalShards             The total amount of shards.
     * @param waitForServersOnStartup Whether Javacord should wait for all servers
     *                                to become available on startup or not.
     * @param proxySelector           The proxy selector which should be used to determine the proxies that should be
     *                                used to connect to the Discord REST API and websocket.
     * @param proxy                   The proxy which should be used to connect to the Discord REST API and websocket.
     * @param proxyAuthenticator      The authenticator that should be used to authenticate against proxies that require
     *                                it.
     * @param trustAllCertificates    Whether to trust all SSL certificates.
     * @param ready                   The future which will be completed when the connection to Discord was successful.
     */
    public DiscordApiImpl(
            AccountType accountType,
            String token,
            int currentShard,
            int totalShards,
            boolean waitForServersOnStartup,
            ProxySelector proxySelector,
            Proxy proxy,
            Authenticator proxyAuthenticator,
            boolean trustAllCertificates,
            CompletableFuture<DiscordApi> ready
    ) {
        this(accountType, token, currentShard, totalShards, waitForServersOnStartup, proxySelector, proxy,
                proxyAuthenticator, trustAllCertificates, ready, null, Collections.emptyMap(), Collections.emptyList());
    }

    /**
     * Creates a new discord api instance.
     *
     * @param accountType             The account type of the instance.
     * @param token                   The token used to connect without any account type specific prefix.
     * @param currentShard            The current shard the bot should connect to.
     * @param totalShards             The total amount of shards.
     * @param waitForServersOnStartup Whether Javacord should wait for all servers
     *                                to become available on startup or not.
     * @param proxySelector           The proxy selector which should be used to determine the proxies that should be
     *                                used to connect to the Discord REST API and websocket.
     * @param proxy                   The proxy which should be used to connect to the Discord REST API and websocket.
     * @param proxyAuthenticator      The authenticator that should be used to authenticate against proxies that require
     *                                it.
     * @param trustAllCertificates    Whether to trust all SSL certificates.
     * @param ready                   The future which will be completed when the connection to Discord was successful.
     * @param dns                     The DNS instance to use in the OkHttp client. This should only be used in testing.
     */
    private DiscordApiImpl(
            AccountType accountType,
            String token,
            int currentShard,
            int totalShards,
            boolean waitForServersOnStartup,
            ProxySelector proxySelector,
            Proxy proxy,
            Authenticator proxyAuthenticator,
            boolean trustAllCertificates,
            CompletableFuture<DiscordApi> ready,
            Dns dns) {
        this(accountType, token, currentShard, totalShards, waitForServersOnStartup, proxySelector, proxy,
                proxyAuthenticator, trustAllCertificates, ready, dns, Collections.emptyMap(), Collections.emptyList());
    }

    /**
     * Creates a new discord api instance.
     * @param accountType             The account type of the instance.
     * @param token                   The token used to connect without any account type specific prefix.
     * @param currentShard            The current shard the bot should connect to.
     * @param totalShards             The total amount of shards.
     * @param waitForServersOnStartup Whether Javacord should wait for all servers
     *                                to become available on startup or not.
     * @param proxySelector           The proxy selector which should be used to determine the proxies that should be
     *                                used to connect to the Discord REST API and websocket.
     * @param proxy                   The proxy which should be used to connect to the Discord REST API and websocket.
     * @param proxyAuthenticator      The authenticator that should be used to authenticate against proxies that require
     *                                it.
     * @param trustAllCertificates    Whether to trust all SSL certificates.
     * @param ready                   The future which will be completed when the connection to Discord was successful.
     * @param dns                     The DNS instance to use in the OkHttp client. This should only be used in testing.
     * @param listenerSourceMap       The functions to create listeners for pre-registration.
     * @param unspecifiedListeners    The listeners of unspecified types to pre-register.
     */
    @SuppressWarnings("unchecked")
    public DiscordApiImpl(
            AccountType accountType,
            String token,
            int currentShard,
            int totalShards,
            boolean waitForServersOnStartup,
            ProxySelector proxySelector,
            Proxy proxy,
            Authenticator proxyAuthenticator,
            boolean trustAllCertificates,
            CompletableFuture<DiscordApi> ready,
            Dns dns,
            Map<Class<? extends GloballyAttachableListener>,
                    List<Function<DiscordApi,GloballyAttachableListener>>
                    > listenerSourceMap,
            List<Function<DiscordApi, GloballyAttachableListener>> unspecifiedListeners) {
        this.accountType = accountType;
        this.token = token;
        this.currentShard = currentShard;
        this.totalShards = totalShards;
        this.waitForServersOnStartup = waitForServersOnStartup;
        this.proxySelector = proxySelector;
        this.proxy = proxy;
        this.proxyAuthenticator = proxyAuthenticator;
        this.trustAllCertificates = trustAllCertificates;
        this.reconnectDelayProvider = x ->
                (int) Math.round(Math.pow(x, 1.5) - (1 / (1 / (0.1 * x) + 1)) * Math.pow(x, 1.5));

        if ((proxySelector != null) && (proxy != null)) {
            throw new IllegalStateException("proxy and proxySelector must not be configured both");
        }

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", Javacord.USER_AGENT)
                        .build()))
                .addInterceptor(
                        new HttpLoggingInterceptor(LoggerUtil.getLogger(OkHttpClient.class)::trace).setLevel(Level.BODY)
                )
                .proxyAuthenticator(new ProxyAuthenticator(proxyAuthenticator))
                .proxy(proxy);
        if (proxySelector != null) {
            httpClientBuilder.proxySelector(proxySelector);
        }
        if (dns != null) {
            httpClientBuilder.dns(dns);
        }
        if (trustAllCertificates) {
            logger.warn("All SSL certificates are trusted when connecting to the Discord API and websocket. "
                    + "This increases the risk of man-in-the-middle attacks!");
            TrustAllTrustManager trustManager = new TrustAllTrustManager();
            httpClientBuilder.sslSocketFactory(trustManager.createSslSocketFactory(), trustManager);
        }
        this.httpClient = httpClientBuilder.build();
        this.eventDispatcher = new EventDispatcher(this);

        if (ready != null) {
            getThreadPool().getExecutorService().submit(() -> {
                try {
                    this.websocketAdapter = new DiscordWebSocketAdapter(this);
                    this.websocketAdapter.isReady().whenComplete((readyReceived, throwable) -> {
                        if (readyReceived) {
                            // Register listeners
                            listenerSourceMap.forEach((clazz, listenerSources) ->
                                    listenerSources.forEach(listenerSource -> {
                                        Class<GloballyAttachableListener> type
                                                = (Class<GloballyAttachableListener>) clazz;
                                        GloballyAttachableListener listener = listenerSource.apply(this);
                                        addListener(type, type.cast(listener));
                                    }
                            ));
                            unspecifiedListeners.stream()
                                    .map(source -> source.apply(this))
                                    .forEach(this::addListener);
                            // Application information
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
                            threadPool.shutdown();
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
                try {
                    for (Reference<? extends User> userRef = usersCleanupQueue.poll();
                            userRef != null;
                            userRef = usersCleanupQueue.poll()) {
                        Long userId = userIdByRef.remove(userRef);
                        if (userId != null) {
                            users.remove(userId, userRef);
                        }
                    }
                } catch (Throwable t) {
                    logger.error("Failed to process users cleanup queue!", t);
                }
            }, 30, 30, TimeUnit.SECONDS);

            // After minimum JDK 9 is required this can be switched to use a Cleaner
            getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
                try {
                    for (Reference<? extends Message> messageRef = messagesCleanupQueue.poll();
                            messageRef != null;
                            messageRef = messagesCleanupQueue.poll()) {
                        Long messageId = messageIdByRef.remove(messageRef);
                        if (messageId != null) {
                            messages.remove(messageId, messageRef);
                        }
                    }
                } catch (Throwable t) {
                    logger.error("Failed to process messages cleanup queue!", t);
                }
            }, 30, 30, TimeUnit.SECONDS);

            // Add shutdown hook
            ready.thenAccept(api -> {
                WeakReference<DiscordApi> discordApiReference = new WeakReference<>(api);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> Optional.ofNullable(discordApiReference.get())
                        .ifPresent(DiscordApi::disconnect),
                        String.format("Javacord - Shutdown Disconnector (%s)", api)));
            });
        } else {
            WeakReference<DiscordApi> discordApiReference = new WeakReference<>(this);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> Optional.ofNullable(discordApiReference.get())
                    .ifPresent(DiscordApi::disconnect),
                    String.format("Javacord - Shutdown Disconnector (%s)", this)));
        }
    }

    /**
     * Gets the used {@link OkHttpClient http client} for this api instance.
     *
     * @return The used http client.
     */
    public OkHttpClient getHttpClient() {
        if (disconnectCalled) {
            throw new IllegalStateException("disconnect was called already");
        }
        return httpClient;
    }

    /**
     * Gets the event dispatcher which is used to dispatch events.
     *
     * @return The used event dispatcher.
     */
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * Gets the ratelimit manager for this bot.
     *
     * @return The ratelimit manager for this bot.
     */
    public RatelimitManager getRatelimitManager() {
        return ratelimitManager;
    }

    /**
     * Gets the object mapper used by this api instance.
     *
     * @return The object mapper used by this api instance.
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Purges all cached entities.
     * This method is only meant to be called after receiving a READY packet.
     */
    public void purgeCache() {
        synchronized (users) {
            users.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .map(Cleanupable.class::cast)
                    .forEach(Cleanupable::cleanup);
            users.clear();
        }
        userIdByRef.clear();
        servers.values().stream()
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
        servers.clear();
        channels.values().stream()
                .filter(Cleanupable.class::isInstance)
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
        channels.clear();
        unavailableServers.clear();
        customEmojis.clear();
        messages.clear();
        messageIdByRef.clear();
        timeOffset = null;
    }

    /**
     * Gets a collection with all servers, including ready and not ready ones.
     *
     * @return A collection with all servers.
     */
    public Collection<Server> getAllServers() {
        ArrayList<Server> allServers = new ArrayList<>(nonReadyServers.values());
        allServers.addAll(servers.values());
        return Collections.unmodifiableList(allServers);
    }

    /**
     * Gets a server by it's id, including ready and not ready ones.
     *
     * @param id The of the server.
     * @return The server with the given id.
     */
    public Optional<Server> getPossiblyUnreadyServerById(long id) {
        if (nonReadyServers.containsKey(id)) {
            return Optional.ofNullable(nonReadyServers.get(id));
        }
        return Optional.ofNullable(servers.get(id));
    }

    /**
     * Adds the given server to the cache.
     *
     * @param server The server to add.
     */
    public void addServerToCache(ServerImpl server) {
        // Remove in case, there's an old instance in cache
        removeServerFromCache(server.getId());

        nonReadyServers.put(server.getId(), server);
        server.addServerReadyConsumer(s -> {
            nonReadyServers.remove(s.getId());
            removeUnavailableServerFromCache(s.getId());
            servers.put(s.getId(), s);
        });
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
        nonReadyServers.computeIfPresent(serverId, (key, server) -> {
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
        users.compute(user.getId(), (key, value) -> {
            Optional.ofNullable(value)
                    .map(Reference::get)
                    .filter(oldUser -> oldUser != user)
                    .map(Cleanupable.class::cast)
                    .ifPresent(Cleanupable::cleanup);

            WeakReference<User> result = new WeakReference<>(user, usersCleanupQueue);
            userIdByRef.put(result, key);
            return result;
        });
    }

    /**
     * Adds a channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addChannelToCache(Channel channel) {
        Channel oldChannel = channels.put(channel.getId(), channel);
        if (oldChannel != channel && oldChannel instanceof Cleanupable) {
            ((Cleanupable) oldChannel).cleanup();
        }
    }

    /**
     * Removes a channel from the cache.
     *
     * @param channelId The id of the channel to remove.
     */
    public void removeChannelFromCache(long channelId) {
        channels.computeIfPresent(channelId, (key, channel) -> {
            if (channel instanceof Cleanupable) {
                ((Cleanupable) channel).cleanup();
            }
            return null;
        });
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
    private void removeUnavailableServerFromCache(long serverId) {
        unavailableServers.remove(serverId);
    }

    /**
     * Sets the user of the connected account.
     *
     * @param yourself The user of the connected account.
     */
    public void setYourself(User yourself) {
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
        synchronized (users) {
            return getCachedUserById(id).orElseGet(() -> {
                if (!data.has("username")) {
                    throw new IllegalStateException("Couldn't get or created user. Please inform the developer!");
                }
                return new UserImpl(this, data);
            });
        }
    }

    /**
     * Gets or creates a new known custom emoji object.
     *
     * @param server The server of the emoji.
     * @param data The data of the emoji.
     * @return The emoji for the given json object.
     */
    public KnownCustomEmoji getOrCreateKnownCustomEmoji(Server server, JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        return customEmojis.computeIfAbsent(id, key -> new KnownCustomEmojiImpl(this, server, data));
    }

    /**
     * Gets a known custom emoji or creates a new (unknown) custom emoji object.
     *
     * @param data The data of the emoji.
     * @return The emoji for the given json object.
     */
    public CustomEmoji getKnownCustomEmojiOrCreateCustomEmoji(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        CustomEmoji emoji = customEmojis.get(id);
        return emoji == null ? new CustomEmojiImpl(this, data) : emoji;
    }

    /**
     * Gets a known custom emoji or creates a new (unknown) custom emoji object.
     *
     * @param id The id of the emoji.
     * @param name The name of the emoji.
     * @param animated Whether the emoji is animated or not.
     * @return The emoji for the given json object.
     */
    public CustomEmoji getKnownCustomEmojiOrCreateCustomEmoji(long id, String name, boolean animated) {
        CustomEmoji emoji = customEmojis.get(id);
        return emoji == null ? new CustomEmojiImpl(this, id, name, animated) : emoji;
    }

    /**
     * Removes a known custom emoji object.
     *
     * @param emoji The emoji to remove.
     */
    public void removeCustomEmoji(KnownCustomEmoji emoji) {
        customEmojis.remove(emoji.getId());
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
        synchronized (messages) {
            return getCachedMessageById(id).orElseGet(() -> new MessageImpl(this, channel, data));
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
                messageIdByRef.put(result, key);
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
     * Adds an object listener.
     * Adding a listener multiple times to the same object will only add it once
     * and return the same listener manager on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The manager for the added listener.
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjectAttachableListener> ListenerManager<T> addObjectListener(
            Class<?> objectClass, long objectId, Class<T> listenerClass, T listener) {
        Map<ObjectAttachableListener, ListenerManagerImpl<? extends ObjectAttachableListener>> listeners =
                objectListeners
                        .computeIfAbsent(objectClass, key -> new ConcurrentHashMap<>())
                        .computeIfAbsent(objectId, key -> new ConcurrentHashMap<>())
                        .computeIfAbsent(listenerClass, c -> Collections.synchronizedMap(new LinkedHashMap<>()));
        return (ListenerManager<T>) listeners.computeIfAbsent(
                listener, key -> new ListenerManagerImpl<>(this, listener, listenerClass, objectClass, objectId));
    }

    /**
     * Removes an object listener.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    public <T extends ObjectAttachableListener> void removeObjectListener(
            Class<?> objectClass, long objectId, Class<T> listenerClass, T listener) {
        synchronized (objectListeners) {
            if (objectClass == null) {
                return;
            }
            Map<Long, Map<Class<? extends ObjectAttachableListener>, Map<ObjectAttachableListener,
                    ListenerManagerImpl<? extends ObjectAttachableListener>>>> objectListener =
                    objectListeners.get(objectClass);
            if (objectListener == null) {
                return;
            }
            Map<Class<? extends ObjectAttachableListener>, Map<ObjectAttachableListener,
                    ListenerManagerImpl<? extends ObjectAttachableListener>>> listeners = objectListener.get(objectId);
            if (listeners == null) {
                return;
            }
            Map<ObjectAttachableListener, ListenerManagerImpl<? extends ObjectAttachableListener>> classListeners =
                    listeners.get(listenerClass);
            if (classListeners == null) {
                return;
            }
            ListenerManagerImpl<? extends ObjectAttachableListener> listenerManager = classListeners.get(listener);
            if (listenerManager == null) {
                return;
            }
            classListeners.remove(listener);
            listenerManager.removed();
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
     * Remove all listeners attached to an object.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     */
    public void removeObjectListeners(Class<?> objectClass, long objectId) {
        if (objectClass == null) {
            return;
        }
        synchronized (objectListeners) {
            Map<Long, Map<Class<? extends ObjectAttachableListener>,
                    Map<ObjectAttachableListener, ListenerManagerImpl<? extends ObjectAttachableListener>>>>
                    objects = objectListeners.get(objectClass);
            if (objects == null) {
                return;
            }
            // Remove all listeners
            objects.computeIfPresent(objectId, (id, listeners) -> {
                listeners.values().stream()
                        .flatMap(map -> map.values().stream())
                        .forEach(ListenerManagerImpl::removed);
                listeners.clear();
                return null;
            });
            // Cleanup
            if (objects.isEmpty()) {
                objectListeners.remove(objectClass);
            }
        }
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code ObjectAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code ObjectAttachableListener}s and
     *     their assigned listener classes they listen to.
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjectAttachableListener> Map<T, List<Class<T>>> getObjectListeners(
            Class<?> objectClass, long objectId) {
        return Collections.unmodifiableMap(Optional.ofNullable(objectClass)
                .map(objectListeners::get)
                .map(objectListener -> objectListener.get(objectId))
                .map(Map::entrySet)
                .map(Set::stream)
                .map(entryStream -> entryStream
                        .flatMap(entry -> entry
                                .getValue()
                                .keySet()
                                .stream()
                                .map(listener -> new SimpleEntry<>((T) listener, (Class<T>) entry.getKey()))))
                .map(entryStream -> entryStream
                        .collect(Collectors.groupingBy(Entry::getKey,
                                                       Collectors.mapping(Entry::getValue, Collectors.toList()))))
                .orElseGet(HashMap::new));
    }

    /**
     * Gets all object listeners of the given class.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param <T> The type of the listener.
     * @return A list with all object listeners of the given type.
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjectAttachableListener> List<T> getObjectListeners(
            Class<?> objectClass, long objectId, Class<T> listenerClass) {
        return Collections.unmodifiableList((List<T>) Optional.ofNullable(objectClass)
                .map(objectListeners::get)
                .map(objectListener -> objectListener.get(objectId))
                .map(listeners -> listeners.get(listenerClass))
                .map(Map::keySet)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GloballyAttachableListener> Map<T, List<Class<T>>> getListeners() {
        return Collections.unmodifiableMap(listeners.entrySet().stream()
                .flatMap(entry -> entry
                        .getValue()
                        .keySet()
                        .stream()
                        .map(listener -> new SimpleEntry<>((T) listener, (Class<T>) entry.getKey())))
                .collect(Collectors.groupingBy(Entry::getKey,
                                               Collectors.mapping(Entry::getValue, Collectors.toList()))));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GloballyAttachableListener> List<T> getListeners(Class<T> listenerClass) {
        return Collections.unmodifiableList((List<T>) Optional.ofNullable(listenerClass)
                .map(listeners::get)
                .map(Map::keySet)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new));
    }

    @Override
    public String getPrefixedToken() {
        return accountType.getTokenPrefix() + token;
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
    public UncachedMessageUtil getUncachedMessageUtil() {
        return uncachedMessageUtil;
    }

    /*
     * Note: You might think the return type should be Optional<WebsocketAdapter>, because it's null till we receive
     *       the gateway from Discord. However the DiscordApi instance is only passed to the user, AFTER we connect
     *       so for the end user it is in fact never null.
     */
    /**
     * Gets the websocket adapter which is used to connect to Discord.
     *
     * @return The websocket adapter.
     */
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
    public void setAutomaticMessageCacheCleanupEnabled(boolean automaticMessageCacheCleanupEnabled) {
        this.defaultAutomaticMessageCacheCleanupEnabled = automaticMessageCacheCleanupEnabled;
        getChannels().stream()
                .filter(TextChannel.class::isInstance)
                .map(TextChannel.class::cast)
                .forEach(channel -> channel.getMessageCache()
                        .setAutomaticCleanupEnabled(automaticMessageCacheCleanupEnabled));
    }

    @Override
    public boolean isDefaultAutomaticMessageCacheCleanupEnabled() {
        return defaultAutomaticMessageCacheCleanupEnabled;
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
    public boolean isWaitingForServersOnStartup() {
        return waitForServersOnStartup;
    }

    /**
     * The proxy selector which should be used to determine the proxies that should be used to connect to the Discord
     * REST API and websocket.
     *
     * @return the proxy selector which should be used to determine the proxies that should be used to connect to the
     *     Discord REST API and websocket.
     */
    public Optional<ProxySelector> getProxySelector() {
        return Optional.ofNullable(proxySelector);
    }

    /**
     * The proxy which should be used to connect to the Discord REST API and websocket.
     *
     * @return the proxy which should be used to connect to the Discord REST API and websocket.
     */
    public Optional<Proxy> getProxy() {
        return Optional.ofNullable(proxy);
    }

    /**
     * The authenticator that should be used to authenticate against proxies that require it.
     *
     * @return the authenticator that should be used to authenticate against proxies that require it.
     */
    public Optional<Authenticator> getProxyAuthenticator() {
        return Optional.ofNullable(proxyAuthenticator);
    }

    /**
     * Whether to trust all SSL certificates.
     *
     * @return whether to trust all SSL certificates.
     */
    public boolean isTrustAllCertificates() {
        return trustAllCertificates;
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


    /**
     * Sets the current activity, along with type and streaming Url.
     *
     * @param type The activity's type.
     * @param name The name of the activity.
     * @param streamingUrl The Url used for streaming.
     */
    private void updateActivity(ActivityType type, String name, String streamingUrl) {
        if (name == null) {
            activity = null;
        } else if (streamingUrl == null) {
            activity = new ActivityImpl(type, name, null);
        } else {
            activity = new ActivityImpl(type, name, streamingUrl);
        }
        websocketAdapter.updateStatus();
    }


    @Override
    public void updateActivity(String name) {
        updateActivity(ActivityType.PLAYING, name, null);
    }

    @Override
    public void updateActivity(ActivityType type, String name) {
        updateActivity(type, name, null);
    }

    @Override
    public void updateActivity(String name, String streamingUrl) {
        updateActivity(ActivityType.STREAMING, name, streamingUrl);
    }

    @Override
    public void unsetActivity() {
        updateActivity(null);
    }

    @Override
    public Optional<Activity> getActivity() {
        return Optional.ofNullable(activity);
    }

    @Override
    public User getYourself() {
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
                    addLostConnectionListener(event -> threadPool.shutdown());
                    // disconnect web socket
                    websocketAdapter.disconnect();
                    // shutdown thread pool if within one minute no disconnect event was dispatched
                    threadPool.getDaemonScheduler().schedule(threadPool::shutdown, 1, TimeUnit.MINUTES);
                }
                disconnectCalled = true;
                httpClient.dispatcher().executorService().shutdown();
                httpClient.connectionPool().evictAll();
            }
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
    public CompletableFuture<ApplicationInfo> getApplicationInfo() {
        return new RestRequest<ApplicationInfo>(this, RestMethod.GET, RestEndpoint.SELF_INFO)
                .execute(result -> new ApplicationInfoImpl(this, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<Webhook> getWebhookById(long id) {
        return new RestRequest<Webhook>(this, RestMethod.GET, RestEndpoint.WEBHOOK)
                .setUrlParameters(Long.toUnsignedString(id))
                .execute(result -> new WebhookImpl(this, result.getJsonBody()));
    }

    @Override
    public Collection<Long> getUnavailableServers() {
        return Collections.unmodifiableCollection(unavailableServers);
    }

    @Override
    public CompletableFuture<Invite> getInviteByCode(String code) {
        return new RestRequest<Invite>(this, RestMethod.GET, RestEndpoint.INVITE)
                .setUrlParameters(code)
                .addQueryParameter("with_counts", "false")
                .execute(result -> new InviteImpl(this, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<Invite> getInviteWithMemberCountsByCode(String code) {
        return new RestRequest<Invite>(this, RestMethod.GET, RestEndpoint.INVITE)
                .setUrlParameters(code)
                .addQueryParameter("with_counts", "true")
                .execute(result -> new InviteImpl(this, result.getJsonBody()));
    }

    @Override
    public Collection<User> getCachedUsers() {
        synchronized (users) {
            return Collections.unmodifiableCollection(users.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public Optional<User> getCachedUserById(long id) {
        return Optional.ofNullable(users.get(id)).map(Reference::get);
    }

    @Override
    public CompletableFuture<User> getUserById(long id) {
        return getCachedUserById(id)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> new RestRequest<User>(this, RestMethod.GET, RestEndpoint.USER)
                .setUrlParameters(Long.toUnsignedString(id))
                .execute(result -> this.getOrCreateUser(result.getJsonBody())));
    }

    @Override
    public MessageSet getCachedMessages() {
        synchronized (messages) {
            return messages.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull).collect(Collectors.toCollection(MessageSetImpl::new));
        }
    }

    /**
     * Get messages from the cache that satisfy a given condition.
     *
     * @param filter The filter for messages to be included.
     * @return A set of cached messages satisfying the condition.
     */
    public MessageSet getCachedMessagesWhere(Predicate<Message> filter) {
        synchronized (messages) {
            return messages.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .filter(filter)
                    .collect(Collectors.toCollection(MessageSetImpl::new));
        }
    }

    /**
     * Execute a task for every message in cache that satisfied a given condition.
     *
     * @param filter The condition on which to execute the code.
     * @param action The action to be applied to the messages.
     */
    public void forEachCachedMessageWhere(Predicate<Message> filter, Consumer<Message> action) {
        synchronized (messages) {
            messages.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .filter(filter)
                    .forEach(action);
        }
    }

    @Override
    public Optional<Message> getCachedMessageById(long id) {
        return Optional.ofNullable(messages.get(id)).map(Reference::get);
    }

    @Override
    public Collection<Server> getServers() {
        return Collections.unmodifiableList(new ArrayList<>(servers.values()));
    }

    @Override
    public Optional<Server> getServerById(long id) {
        return Optional.ofNullable(servers.get(id));
    }

    @Override
    public Collection<KnownCustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableCollection(customEmojis.values());
    }

    @Override
    public Optional<KnownCustomEmoji> getCustomEmojiById(long id) {
        return Optional.ofNullable(customEmojis.get(id));
    }

    @Override
    public Collection<Channel> getChannels() {
        return Collections.unmodifiableCollection(new ArrayList<>(channels.values()));
    }

    @Override
    public Optional<Channel> getChannelById(long id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ListenerManager<? extends GloballyAttachableListener>> addListener(
            GloballyAttachableListener listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GloballyAttachableListener.class::isAssignableFrom)
                .filter(listenerClass -> listenerClass != GloballyAttachableListener.class)
                .map(listenerClass -> (Class<GloballyAttachableListener>) listenerClass)
                .map(listenerClass -> addListener(listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GloballyAttachableListener> ListenerManager<T> addListener(Class<T> listenerClass, T listener) {
        return (ListenerManager<T>) listeners
                .computeIfAbsent(listenerClass, key -> Collections.synchronizedMap(new LinkedHashMap<>()))
                .computeIfAbsent(listener, key -> new ListenerManagerImpl<>(this, listener, listenerClass));
    }

    @Override
    public <T extends GloballyAttachableListener> void removeListener(Class<T> listenerClass, T listener) {
        synchronized (listeners) {
            Map<GloballyAttachableListener, ListenerManagerImpl<? extends GloballyAttachableListener>> classListeners =
                    listeners.get(listenerClass);
            if (classListeners == null) {
                return;
            }
            ListenerManagerImpl<? extends GloballyAttachableListener> listenerManager = classListeners.get(listener);
            if (listenerManager == null) {
                return;
            }
            classListeners.remove(listener);
            listenerManager.removed();
            // Clean it up
            if (classListeners.isEmpty()) {
                listeners.remove(listenerClass);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeListener(GloballyAttachableListener listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GloballyAttachableListener.class::isAssignableFrom)
                .filter(listenerClass -> listenerClass != GloballyAttachableListener.class)
                .map(listenerClass -> (Class<GloballyAttachableListener>) listenerClass)
                .forEach(listenerClass -> removeListener(listenerClass, listener));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }
}
