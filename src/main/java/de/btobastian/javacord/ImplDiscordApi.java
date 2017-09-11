package de.btobastian.javacord;

import com.mashape.unirest.http.HttpMethod;
import com.neovisionaries.ws.client.WebSocketAdapter;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.GameType;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplGame;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.utils.DiscordWebsocketAdapter;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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
     * A map which contains all servers.
     */
    private final ConcurrentHashMap<Long, Server> servers = new ConcurrentHashMap<>();

    /**
     * A set with all unavailable servers.
     */
    private final HashSet<Long> unavailableServers = new HashSet<>();

    // All listeners
    private final ArrayList<MessageCreateListener> messageCreateListeners = new ArrayList<>();

    public ImplDiscordApi(AccountType accountType, String token, CompletableFuture<DiscordApi> ready) {
        this.accountType = accountType;
        this.token = accountType.getTokenPrefix() + token;

        RestEndpoint endpoint = RestEndpoint.GATEWAY_BOT;
        if (accountType == AccountType.CLIENT) {
            endpoint = RestEndpoint.GATEWAY;
        }

        new RestRequest(this, HttpMethod.GET, endpoint).execute().whenComplete((res, t) -> {
            if (t != null) {
                ready.completeExceptionally(t);
                return;
            }

            String gateway = res.getBody().getObject().getString("url");

            websocketAdapter = new DiscordWebsocketAdapter(this, gateway);
            websocketAdapter.isReady().whenComplete((readyReceived, throwable) -> {
                if (readyReceived) {
                    ready.complete(this);
                } else {
                    ready.completeExceptionally(
                            new IllegalStateException("Websocket closed before READY packet was received!"));
                }
            });
        });
    }

    /**
     * Adds the given server to cache.
     *
     * @param server The server to add.
     */
    public void addServerToCache(Server server) {
        servers.put(server.getId(), server);
    }

    /**
     * Gets a set with all unavailable servers.
     *
     * @return A set with all unavailable servers.
     */
    public HashSet<Long> getUnavailableServers() {
        return unavailableServers;
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
    public WebSocketAdapter getWebSocketAdapter() {
        return websocketAdapter;
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
    public void disconnect() {
        websocketAdapter.disconnect();
    }

    @Override
    public void setReconnectRatelimit(int attempts, int seconds) {
        websocketAdapter.setReconnectAttempts(attempts);
        websocketAdapter.setRatelimitResetIntervalInSeconds(seconds);
    }

    @Override
    public Optional<Server> getServerById(long id) {
        return Optional.ofNullable(servers.get(id));
    }

    @Override
    public Collection<Server> getServers() {
        return servers.values();
    }

    @Override
    public void addMessageCreateListener(MessageCreateListener listener) {
        messageCreateListeners.add(listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return Collections.unmodifiableList(messageCreateListeners);
    }
}
