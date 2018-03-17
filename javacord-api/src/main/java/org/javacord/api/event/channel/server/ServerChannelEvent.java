package org.javacord.api.event.channel.server;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.ChannelEvent;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server channel event.
 */
public interface ServerChannelEvent extends ServerEvent, ChannelEvent {

    @Override
    ServerChannel getChannel();

}
