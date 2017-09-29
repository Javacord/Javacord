package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.Mentionable;

/**
 * This class represents a server text channel.
 */
public interface ServerTextChannel extends ServerChannel, TextChannel, Mentionable {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_TEXT_CHANNEL;
    }

}
