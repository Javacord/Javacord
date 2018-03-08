package org.javacord.event.channel;

import org.javacord.entity.channel.VoiceChannel;

/**
 * A voice channel event.
 */
public interface VoiceChannelEvent extends ChannelEvent {

    @Override
    VoiceChannel getChannel();

}
