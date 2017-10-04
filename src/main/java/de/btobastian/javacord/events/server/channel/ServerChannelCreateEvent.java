package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ServerChannel;

/**
 * A server channel create event.
 */
public class ServerChannelCreateEvent extends ServerChannelEvent {

    /**
     * Creates a new server channel create event.
     *
     * @param channel The channel of the event.
     */
    public ServerChannelCreateEvent(ServerChannel channel) {
        super(channel);
    }

}
