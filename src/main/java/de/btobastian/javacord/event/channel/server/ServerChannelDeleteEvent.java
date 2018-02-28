package de.btobastian.javacord.event.channel.server;

import de.btobastian.javacord.entity.channel.ServerChannel;

/**
 * A server channel delete event.
 */
public class ServerChannelDeleteEvent extends ServerChannelEvent {

    /**
     * Creates a new server channel delete event.
     *
     * @param channel The channel of the event.
     */
    public ServerChannelDeleteEvent(ServerChannel channel) {
        super(channel);
    }

}
