package de.btobastian.javacord.entities.channels;

import java.util.List;

/**
 * This class represents a channel category.
 */
public interface ChannelCategory extends ServerChannel {

    /**
     * Gets a sorted (by position) list of all channels in the category.
     *
     * @return The channels in the category.
     */
    List<ServerChannel> getChannels();

}
