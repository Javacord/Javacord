package de.btobastian.javacord.entities.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.impl.ImplServerTextChannel;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of {@link de.btobastian.javacord.entities.Server}.
 */
public class ImplServer implements Server {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the server.
     */
    private final long id;

    /**
     * The name of the server.
     */
    private String name;

    /**
     * A map with all channels of the server.
     */
    private final ConcurrentHashMap<Long, ServerChannel> channels = new ConcurrentHashMap<>();

    // All listeners
    private final ArrayList<MessageCreateListener> messageCreateListeners = new ArrayList<>();

    /**
     * Creates a new server object.
     *
     * @param api The discord api instance.
     * @param data The json data of the server.
     */
    public ImplServer(ImplDiscordApi api, JSONObject data) {
        this.api = api;

        id = Long.parseLong(data.getString("id"));
        name = data.getString("name");

        if (data.has("channels")) {
            JSONArray channels = data.getJSONArray("channels");
            for (int i = 0; i < channels.length(); i++) {
                JSONObject channel = channels.getJSONObject(i);
                switch (channel.getInt("type")) {
                    case 0:
                        new ImplServerTextChannel(api, this, channel);
                        break;
                    case 2:
                        // TODO create server voice channel
                        break;
                }
            }
        }

        api.addServerToCache(this);
    }

    /**
     * Adds a channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addChannelToCache(ServerChannel channel) {
        channels.put(channel.getId(), channel);
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
    public Collection<ServerChannel> getChannels() {
        return channels.values();
    }

    @Override
    public Optional<ServerChannel> getChannelById(long id) {
        return Optional.ofNullable(channels.get(id));
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
