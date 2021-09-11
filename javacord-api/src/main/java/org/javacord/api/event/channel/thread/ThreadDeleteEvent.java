package org.javacord.api.event.channel.thread;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.channel.ChannelEvent;

public interface ThreadDeleteEvent extends ChannelEvent {

    @Override
    TextChannel getChannel();
}
