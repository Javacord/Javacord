package de.btobastian.javacord.entities.channels;

/**
 * This class represents a server voice channel.
 */
public interface ServerVoiceChannel extends ServerChannel, VoiceChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_VOICE_CHANNEL;
    }

}
