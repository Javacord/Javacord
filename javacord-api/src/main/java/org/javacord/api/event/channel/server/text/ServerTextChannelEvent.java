package org.javacord.api.event.channel.server.text;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.channel.TextChannelEvent;
import org.javacord.api.event.channel.server.ServerChannelEvent;

/**
 * A server text channel event.
 */
public interface ServerTextChannelEvent extends ServerChannelEvent, TextChannelEvent {

    @Override
    ServerTextChannel getChannel();
}
