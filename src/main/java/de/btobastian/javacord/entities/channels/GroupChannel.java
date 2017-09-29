package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.IconHolder;
import de.btobastian.javacord.entities.User;

import java.util.Collection;
import java.util.Optional;

/**
 * This class represents a group channel. Group channels are not supported by bot accounts!
 */
public interface GroupChannel extends TextChannel, VoiceChannel, IconHolder {

    @Override
    default ChannelType getType() {
        return ChannelType.GROUP_CHANNEL;
    }

    /**
     * Gets the members of the group channel.
     *
     * @return The members of the group channel.
     */
    Collection<User> getMembers();

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    Optional<String> getName();

}
