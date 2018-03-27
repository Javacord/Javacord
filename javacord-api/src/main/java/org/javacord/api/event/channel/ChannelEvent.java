package org.javacord.api.event.channel;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.event.Event;

/**
 * A channel event.
 */
public interface ChannelEvent extends Event {

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    Channel getChannel();

}
