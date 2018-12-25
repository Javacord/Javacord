package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannelUpdater;

/**
 * This class is internally used by the {@link ServerTextChannelUpdater} to update server text channels.
 * You usually don't want to interact with this object.
 */
public interface ServerTextChannelUpdaterDelegate extends ServerChannelUpdaterDelegate {

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     */
    void setTopic(String topic);

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     */
    void setNsfwFlag(boolean nsfw);

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
     * Sets the slowmode delay.
     *
     * @param delay The delay in seconds.
     */
    void setSlowmodeDelayInSeconds(int delay);

}
