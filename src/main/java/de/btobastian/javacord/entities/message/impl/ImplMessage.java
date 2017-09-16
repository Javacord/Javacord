package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.impl.ImplEmbed;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link Message}.
 */
public class ImplMessage implements Message {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The channel of the message.
     */
    private final TextChannel channel;

    /**
     * The id of the server.
     */
    private final long id;

    /**
     * The content of the message.
     */
    private final String content;

    /**
     * A map which contains all listeners.
     * The key is the class of the listener.
     */
    private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

    /**
     * The user author of the message. Can be <code>null</code> if the author is a webhook for example.
     */
    private final User userAuthor;

    /**
     * If the message should be cached forever or not.
     */
    private boolean cacheForever = false;

    /**
     * As soon as we receive a message delete event, we mark the message as deleted.
     */
    private boolean deleted = false;

    /**
     * A list with all embeds.
     */
    private final ArrayList<Embed> embeds = new ArrayList<>();

    /**
     * Creates a new message object.
     *
     * @param api The discord api instance.
     * @param channel The channel of the message.
     * @param data The json data of the message.
     */
    public ImplMessage(ImplDiscordApi api, TextChannel channel, JSONObject data) {
        this.api = api;
        this.channel = channel;

        id = Long.parseLong(data.getString("id"));
        content = data.getString("content");

        if (data.has("webhook_id")) {
            userAuthor = null;
        } else {
            userAuthor = api.getOrCreateUser(data.getJSONObject("author"));
        }

        ImplMessageCache cache = (ImplMessageCache) channel.getMessageCache();
        if (cache.getCapacity() != 0 && cache.getStorageTimeInSeconds() != 0) {
            cache.addMessage(this);
        }

        JSONArray embeds = data.getJSONArray("embeds");
        for (int i = 0; i < embeds.length(); i++) {
            Embed embed = new ImplEmbed(embeds.getJSONObject(i));
            this.embeds.add(embed);
        }
    }

    /**
     * Sets the deleted flag of the message.
     *
     * @param deleted The deleted flag.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
    public String getContent() {
        return content;
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public List<Embed> getEmbeds() {
        return Collections.unmodifiableList(embeds);
    }

    @Override
    public Optional<User> getAuthor() {
        return Optional.ofNullable(userAuthor);
    }

    @Override
    public boolean isCachedForever() {
        return cacheForever;
    }

    @Override
    public void setCachedForever(boolean cachedForever) {
        this.cacheForever = cachedForever;
        if (cachedForever) {
            // Just make sure it's in the cache
            ((ImplMessageCache) channel.getMessageCache()).addMessage(this);
        }
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public int compareTo(Message otherMessage) {
        return otherMessage.getCreationDate().compareTo(getCreationDate());
    }

    @Override
    public int hashCode() {
        return String.valueOf(getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Message && ((Message) obj).getId() == getId();
    }

    @Override
    public void addMessageDeleteListener(MessageDeleteListener listener) {
        addListener(MessageDeleteListener.class, listener);
        setCachedForever(true);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners() {
        return getListeners(MessageDeleteListener.class);
    }
}
