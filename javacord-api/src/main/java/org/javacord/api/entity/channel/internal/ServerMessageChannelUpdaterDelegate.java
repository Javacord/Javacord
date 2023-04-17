package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerMessageChannelUpdater;

/**
 * This class is internally used by the {@link ServerMessageChannelUpdater} to update server message channels.
 * You usually don't want to interact with this object.
 */
public interface ServerMessageChannelUpdaterDelegate extends RegularServerChannelUpdaterDelegate {
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

}
