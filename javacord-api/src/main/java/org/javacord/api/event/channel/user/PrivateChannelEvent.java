package org.javacord.api.event.channel.user;

import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.event.channel.ChannelEvent;
import org.javacord.api.event.user.UserEvent;

/**
 * A private channel event.
 */
public interface PrivateChannelEvent extends ChannelEvent, UserEvent {

    @Override
    PrivateChannel getChannel();

}
