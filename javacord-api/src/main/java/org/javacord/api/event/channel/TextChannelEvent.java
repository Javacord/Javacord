package org.javacord.api.event.channel;

import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;

import java.util.Optional;

/**
 * A text channel event.
 */
public interface TextChannelEvent extends ChannelEvent {

    @Override
    TextChannel getChannel();

    /**
     * Gets the channel of the event as a server text channel.
     *
     * @return The channel of the event as a server text channel.
     */
    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().asServerTextChannel();
    }

    /**
     * Gets the channel of the event as a server text channel.
     *
     * @return The channel of the event as a server text channel.
     */
    default Optional<ServerVoiceChannel> getServerVoiceChannel() {
        return getChannel().asServerVoiceChannel();
    }

    /**
     * Gets the channel of the event as a server thread channel.
     *
     * @return The channel of the event as a server thread channel.
     */
    default Optional<ServerThreadChannel> getServerThreadChannel() {
        return getChannel().asServerThreadChannel();
    }

    /**
     * Gets the channel of the event as a private channel.
     *
     * @return The channel of the event as a private channel.
     */
    default Optional<PrivateChannel> getPrivateChannel() {
        return getChannel().asPrivateChannel();
    }

}
