package org.javacord.event.channel.server.text;

import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.event.channel.TextChannelEvent;
import org.javacord.event.channel.server.ServerChannelEvent;

/**
 * A server text channel event.
 */
public interface ServerTextChannelEvent extends ServerChannelEvent, TextChannelEvent {

    @Override
    ServerTextChannel getChannel();

}
