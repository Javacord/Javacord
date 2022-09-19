package org.javacord.api.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.channel.VoiceChannelEvent;
import org.javacord.api.event.channel.server.ServerChannelEvent;

/**
 * A server voice channel event.
 */
public interface ServerVoiceChannelEvent extends ServerChannelEvent, VoiceChannelEvent {

    @Override
    ServerVoiceChannel getChannel();

}
