package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.TextableRegularServerChannelBuilder;

/**
 * This class is internally used by the {@link TextableRegularServerChannelBuilder} to create regular server channels.
 * You usually don't want to interact with this object.
 */
public interface TextableRegularServerChannelBuilderDelegate extends RegularServerChannelBuilderDelegate {

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     */
    void setCategory(ChannelCategory category);

    /**
     * Sets the slowmode delay of the channel.
     *
     * @param delay The delay in seconds.
     */
    void setSlowmodeDelayInSeconds(int delay);
}
