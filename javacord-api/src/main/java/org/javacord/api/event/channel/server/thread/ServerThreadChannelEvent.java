package org.javacord.api.event.channel.server.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.server.ServerChannelEvent;

/**
 * A server thread channel event.
 */
public interface ServerThreadChannelEvent extends ServerChannelEvent {

    @Override
    ServerThreadChannel getChannel();
}
