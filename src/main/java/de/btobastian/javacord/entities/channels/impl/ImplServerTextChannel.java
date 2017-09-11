package de.btobastian.javacord.entities.channels.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    // All listeners
    private final ArrayList<MessageCreateListener> messageCreateListeners = new ArrayList<>();

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

        id = Long.parseLong(data.getString("id"));
        name = data.getString("name");

        server.addChannelToCache(this);
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
    public String getMentionTag() {
        return "<#" + getId() + ">";
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
