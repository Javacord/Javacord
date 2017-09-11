package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.events.server.ServerEvent;

/**
 * A server channel event.
 */
public abstract class ServerChannelEvent extends ServerEvent {

    /**
     * The channel of the event.
     */
    private final ServerChannel channel;

    /**
     * Creates a new server event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param channel The channel of the event.
     */
    public ServerChannelEvent(DiscordApi api, Server server, ServerChannel channel) {
        super(api, server);
        this.channel = channel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public ServerChannel getChannel() {
        return channel;
    }

}
