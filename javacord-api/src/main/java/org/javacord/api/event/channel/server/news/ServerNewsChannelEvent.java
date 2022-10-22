package org.javacord.api.event.channel.server.news;

import org.javacord.api.entity.channel.ServerNewsChannel;
import org.javacord.api.event.channel.TextChannelEvent;
import org.javacord.api.event.channel.server.ServerChannelEvent;

public interface ServerNewsChannelEvent extends ServerChannelEvent, TextChannelEvent {
    @Override
    ServerNewsChannel getChannel();
}
