package org.javacord.event.channel;

import org.javacord.entity.channel.GroupChannel;
import org.javacord.entity.channel.PrivateChannel;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.TextChannel;

import java.util.Optional;

/**
 * A text channel event.
 */
public interface TextChannelEvent extends ChannelEvent {

    @Override
    TextChannel getChannel();

    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().asServerTextChannel();
    }

    default Optional<PrivateChannel> getPrivateChannel() {
        return getChannel().asPrivateChannel();
    }

    default Optional<GroupChannel> getGroupChannel() {
        return getChannel().asGroupChannel();
    }

}
