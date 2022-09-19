package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerForumChannelUpdater;

/**
 * This class is internally used by the {@link ServerForumChannelUpdater} to update server forum channels.
 * You usually don't want to interact with this object.
 */
public interface ServerForumChannelUpdaterDelegate extends RegularServerChannelUpdaterDelegate {

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
