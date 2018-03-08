package org.javacord.event.channel.server.impl;

import org.javacord.entity.channel.ServerChannel;
import org.javacord.event.channel.server.ServerChannelCreateEvent;

/**
 * The implementation of {@link ServerChannelCreateEvent}.
 */
public class ImplServerChannelCreateEvent extends ImplServerChannelEvent implements ServerChannelCreateEvent {

    /**
     * Creates a new server channel create event.
     *
     * @param channel The channel of the event.
     */
    public ImplServerChannelCreateEvent(ServerChannel channel) {
        super(channel);
    }

}
