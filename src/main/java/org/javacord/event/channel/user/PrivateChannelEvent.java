package org.javacord.event.channel.user;

import org.javacord.entity.channel.PrivateChannel;
import org.javacord.event.channel.ChannelEvent;
import org.javacord.event.user.UserEvent;

/**
 * A private channel event.
 */
public interface PrivateChannelEvent extends ChannelEvent, UserEvent {

    @Override
    PrivateChannel getChannel();

}
