package de.btobastian.javacord.entities.channels.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import de.btobastian.javacord.utils.cache.MessageCache;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ImplServerTextChannel implements ServerTextChannel {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The name of the channel.
     */
    private String name;

    /**
     * The server of the channel.
     */
    private final ImplServer server;

    /**
     * The message cache of the server text channel.
     */
    private final ImplMessageCache messageCache;

    /**
     * A map which contains all listeners.
     * The key is the class of the listener.
     */
    private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

    /**
     * Creates a new server text channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ImplServerTextChannel(ImplDiscordApi api, ImplServer server, JSONObject data) {
        this.api = api;
        this.server = server;
        this.messageCache = new ImplMessageCache(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.getString("id"));
        name = data.getString("name");

        server.addChannelToCache(this);
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
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public MessageCache getMessageCache() {
        return messageCache;
    }

    @Override
    public String getMentionTag() {
        return "<#" + getId() + ">";
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
}
