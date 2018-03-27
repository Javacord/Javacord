package org.javacord.core.event.channel.server;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.server.ServerChannelCreateEvent;

/**
 * The implementation of {@link ServerChannelCreateEvent}.
 */
public class ServerChannelCreateEventImpl extends ServerChannelEventImpl implements ServerChannelCreateEvent {

    /**
     * Creates a new server channel create event.
     *
     * @param channel The channel of the event.
     */
    public ServerChannelCreateEventImpl(ServerChannel channel) {
        super(channel);
    }

}
