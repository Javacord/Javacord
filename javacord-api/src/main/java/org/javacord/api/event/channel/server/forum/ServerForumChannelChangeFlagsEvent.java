package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.ChannelFlag;

import java.util.EnumSet;

/**
 * An event signaling that a server forum channel's flags have changed.
 */
public interface ServerForumChannelChangeFlagsEvent extends ServerForumChannelEvent {

    /**
     * Gets the old flags of the channel.
     *
     * @return The old flags of the channel.
     */
    EnumSet<ChannelFlag> getOldFlags();

    /**
     * Gets the new flags of the channel.
     *
     * @return The new flags of the channel.
     */
    EnumSet<ChannelFlag> getNewFlags();

}
