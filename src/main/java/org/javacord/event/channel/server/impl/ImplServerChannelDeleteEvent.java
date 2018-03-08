package org.javacord.event.channel.server.impl;

import org.javacord.entity.channel.ServerChannel;
import org.javacord.event.channel.server.ServerChannelDeleteEvent;

/**
 * The implementation of {@link ServerChannelDeleteEvent}.
 */
public class ImplServerChannelDeleteEvent extends ImplServerChannelEvent implements ServerChannelDeleteEvent {

    /**
     * Creates a new server channel delete event.
     *
     * @param channel The channel of the event.
     */
    public ImplServerChannelDeleteEvent(ServerChannel channel) {
        super(channel);
    }

}
