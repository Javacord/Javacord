package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.User;

/**
 * This class represents a private channel.
 * Every conversation between two users takes place in a private channel.
 */
public interface PrivateChannel extends TextChannel, VoiceChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.PRIVATE_CHANNEL;
    }

    /**
     * Gets the recipient of the private channel.
     * A private channel always consists of yourself and one other user.
     *
     * @return The recipient of the private channel.
     */
    User getRecipient();

}
