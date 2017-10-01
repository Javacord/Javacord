package de.btobastian.javacord.entities.impl;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.IconHolder;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.channels.impl.ImplPrivateChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerMemberAddListener;
import de.btobastian.javacord.listeners.server.ServerMemberRemoveListener;
import de.btobastian.javacord.listeners.user.UserChangeGameListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link User}.
 */
public class ImplUser implements User, IconHolder {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ImplUser.class);

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the user.
     */
    private final long id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The private channel with the given user.
     */
    private PrivateChannel channel = null;

    /**
     * A map which contains all listeners.
     * The key is the class of the listener.
     */
    private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

    /**
     * The avatar id of the user. Might be <code>null</code>!
     */
    private String avatarId = null;

    /**
     * The discriminator of the user.
     */
    private String discriminator;

    /**
     * Whether the user is a bot account or not.
     */
    private final boolean bot;

    /**
     * The game of the user.
     */
    private Game game = null;

    /**
     * The status of the user.
     */
    private UserStatus status = UserStatus.OFFLINE;

    /**
     * Creates a new user.
     *
     * @param api The discord api instance.
     * @param data The json data of the user.
     */
    public ImplUser(ImplDiscordApi api, JSONObject data) {
        this.api = api;

        id = Long.parseLong(data.getString("id"));
        name = data.getString("username");
        discriminator = data.getString("discriminator");
        if (data.has("avatar") && !data.isNull("avatar")) {
            avatarId = data.getString("avatar");
        }
        bot = data.has("bot") && data.getBoolean("bot");

        api.addUserToCache(this);
    }

    /**
     * Sets the private channel with the user.
     *
     * @param channel The channel to set.
     */
    public void setChannel(PrivateChannel channel) {
        this.channel = channel;
    }

    /**
     * Sets the game of the user.
     *
     * @param game The game to set.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Sets the status of the user.
     *
     * @param status The status to set.
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Gets or creates a new private channel.
     *
     * @param data The data of the private channel.
     * @return The private channel for the given data.
     */
    public PrivateChannel getOrCreateChannel(JSONObject data) {
        synchronized (this) {
            if (channel != null) {
                return channel;
            }
            return new ImplPrivateChannel(api, data);
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
    public String getName() {
        return name;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    @Override
    public Optional<PrivateChannel> getPrivateChannel() {
        return Optional.ofNullable(channel);
    }

    @Override
    public CompletableFuture<PrivateChannel> openPrivateChannel() {
        if (channel != null) {
            CompletableFuture<PrivateChannel> future = new CompletableFuture<>();
            future.complete(channel);
            return future;
        }
        return new RestRequest<PrivateChannel>(api, HttpMethod.POST, RestEndpoint.USER_CHANNEL)
                .setBody(new JSONObject().put("recipient_id", String.valueOf(getId())))
                .execute(res -> getOrCreateChannel(res.getBody().getObject()));
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Optional<URL> getIconUrl() {
        String url = "https://cdn.discordapp.com/embed/avatars/" + Integer.parseInt(discriminator) % 5 + ".png";
        if (avatarId != null) {
            url = "https://cdn.discordapp.com/avatars/" + getId() + "/" + avatarId +
                    (avatarId.startsWith("a_") ? ".gif" : ".png");

        }
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Message> sendMessage(
            String content, EmbedBuilder embed, boolean tts, String nonce, InputStream stream, String fileName) {
        return openPrivateChannel().thenApplyAsync(
                channel -> channel.sendMessage(content, embed, tts, nonce, stream, fileName).join(),
                api.getThreadPool().getExecutorService()
        );
    }

    @Override
    public String toString() {
        return String.format("User (id: %s, name: %s)", getId(), getName());
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
    public void addUserStartTypingListener(UserStartTypingListener listener) {
        addListener(UserStartTypingListener.class, listener);
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {
        return getListeners(UserStartTypingListener.class);
    }

    @Override
    public void addReactionAddListener(ReactionAddListener listener) {
        addListener(ReactionAddListener.class, listener);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners() {
        return getListeners(ReactionAddListener.class);
    }

    @Override
    public void addReactionRemoveListener(ReactionRemoveListener listener) {
        addListener(ReactionRemoveListener.class, listener);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners() {
        return getListeners(ReactionRemoveListener.class);
    }

    @Override
    public void addServerMemberAddListener(ServerMemberAddListener listener) {
        addListener(ServerMemberAddListener.class, listener);
    }

    @Override
    public List<ServerMemberAddListener> getServerMemberAddListeners() {
        return getListeners(ServerMemberAddListener.class);
    }

    @Override
    public void addServerMemberRemoveListener(ServerMemberRemoveListener listener) {
        addListener(ServerMemberRemoveListener.class, listener);
    }

    @Override
    public List<ServerMemberRemoveListener> getServerMemberRemoveListeners() {
        return getListeners(ServerMemberRemoveListener.class);
    }

    @Override
    public void addUserChangeGameListener(UserChangeGameListener listener) {
        addListener(UserChangeGameListener.class, listener);
    }

    @Override
    public List<UserChangeGameListener> getUserChangeGameListeners() {
        return getListeners(UserChangeGameListener.class);
    }
}
