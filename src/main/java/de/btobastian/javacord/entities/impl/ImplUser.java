package de.btobastian.javacord.entities.impl;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.channels.impl.ImplPrivateChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.JavacordCompletableFuture;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link User}.
 */
public class ImplUser implements User {

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
     * Creates a new user.
     *
     * @param api The discord api instance.
     * @param data The json data of the user.
     */
    public ImplUser(ImplDiscordApi api, JSONObject data) {
        this.api = api;

        id = Long.parseLong(data.getString("id"));
        name = data.getString("username");

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
    public Optional<PrivateChannel> getPrivateChannel() {
        return Optional.ofNullable(channel);
    }

    @Override
    public CompletableFuture<PrivateChannel> openPrivateChannel() {
        if (channel != null) {
            CompletableFuture<PrivateChannel> future = new JavacordCompletableFuture<>();
            future.complete(channel);
            return future;
        }
        return new RestRequest<PrivateChannel>(api, HttpMethod.POST, RestEndpoint.USER_CHANNEL)
                .setBody(new JSONObject().put("recipient_id", String.valueOf(getId())))
                .execute(res -> getOrCreateChannel(res.getBody().getObject()));
    }

    @Override
    public boolean isYourself(){
        return this == api.getYourself();
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
    public String getMentionTag() {
        return "<@" + getId() + ">";
    }

    @Override
    public CompletableFuture<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, String nonce) {
        return openPrivateChannel().thenApplyAsync(
                channel -> channel.sendMessage(content, embed, tts, nonce).join(),
                api.getThreadPool().getExecutorService()
        );
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
}
