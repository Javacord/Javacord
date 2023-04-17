package org.javacord.api.event.channel.server.message;

import org.javacord.api.entity.channel.ServerMessageChannel;
import org.javacord.api.event.channel.TextChannelEvent;
import org.javacord.api.event.channel.server.ServerChannelEvent;

/**
 * A server message channel event.
 */
public interface ServerMessageChannelEvent extends ServerChannelEvent, TextChannelEvent {

    @Override
    ServerMessageChannel getChannel();

}
