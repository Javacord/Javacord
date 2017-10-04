package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerChannel;

/**
 * A server channel create event.
 */
public class ServerChannelCreateEvent extends ServerChannelEvent {

    /**
     * Creates a new server channel create event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param channel The channel of the event.
     */
    public ServerChannelCreateEvent(DiscordApi api, Server server, ServerChannel channel) {
        super(api, server, channel);
    }

}
