package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.ServerChannelEvent;

/**
 * A server forum channel event.
 */
public interface ServerForumChannelEvent extends ServerChannelEvent {
    @Override
    ServerForumChannel getChannel();
}
