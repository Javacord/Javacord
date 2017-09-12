package de.btobastian.javacord.entities.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
        // TODO return openChannel().sendMessage(...);
        return null;
    }

    @Override
    public void addMessageCreateListener(MessageCreateListener listener) {
        addListener(MessageCreateListener.class, listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return getListeners(MessageCreateListener.class);
    }
}
