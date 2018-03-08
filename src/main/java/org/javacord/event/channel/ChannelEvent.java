package org.javacord.event.channel;

import org.javacord.entity.channel.Channel;
import org.javacord.event.Event;

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
