package org.javacord.event.channel.server;

import org.javacord.entity.channel.ServerChannel;
import org.javacord.event.channel.ChannelEvent;
import org.javacord.event.server.ServerEvent;

/**
 * A server channel event.
 */
public interface ServerChannelEvent extends ServerEvent, ChannelEvent {

    @Override
    ServerChannel getChannel();

}
