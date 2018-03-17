package org.javacord.api.event.channel;

import org.javacord.api.entity.channel.VoiceChannel;

/**
 * A voice channel event.
 */
public interface VoiceChannelEvent extends ChannelEvent {

    @Override
    VoiceChannel getChannel();

}
