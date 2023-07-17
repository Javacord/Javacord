package org.javacord.api.event.channel.server.textable;

import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.event.channel.server.ServerChannelEvent;

public interface TextableRegularServerChannelEvent extends ServerChannelEvent {

    @Override
    TextableRegularServerChannel getChannel();
}
