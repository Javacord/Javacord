package org.javacord.core.event.channel.server;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.server.ServerChannelDeleteEvent;

/**
 * The implementation of {@link ServerChannelDeleteEvent}.
 */
public class ServerChannelDeleteEventImpl extends ServerChannelEventImpl implements ServerChannelDeleteEvent {

    /**
     * Creates a new server channel delete event.
     *
     * @param channel The channel of the event.
     */
    public ServerChannelDeleteEventImpl(ServerChannel channel) {
        super(channel);
    }

}
