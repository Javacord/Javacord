package org.javacord.event.channel.server.voice;

import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.event.channel.VoiceChannelEvent;
import org.javacord.event.server.ServerEvent;

/**
 * A server voice channel event.
 */
public interface ServerVoiceChannelEvent extends ServerEvent, VoiceChannelEvent {

    @Override
    ServerVoiceChannel getChannel();

}
