package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;

public interface TextableRegularServerChannelUpdaterDelegate extends RegularServerChannelUpdaterDelegate {
    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     */
    void setCategory(ChannelCategory category);

    /**
     * Queues the category to be removed.
     */
    void removeCategory();

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     */
    void setNsfw(boolean nsfw);

    /**
     * Sets the slowmode delay.
     *
     * @param delay The delay in seconds.
     */
    void setSlowmodeDelayInSeconds(int delay);
}
