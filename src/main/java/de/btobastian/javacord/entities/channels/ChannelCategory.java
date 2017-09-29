package de.btobastian.javacord.entities.channels;

import java.util.List;

/**
 * This class represents a channel category.
 */
public interface ChannelCategory extends ServerChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.CHANNEL_CATEGORY;
    }

    /**
     * Gets a sorted (by position) list of all channels in the category.
     *
     * @return The channels in the category.
     */
    List<ServerChannel> getChannels();

}
