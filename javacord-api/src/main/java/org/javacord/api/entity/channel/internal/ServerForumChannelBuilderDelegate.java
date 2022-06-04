package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.ServerForumChannelBuilder;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerForumChannelBuilder} to create server forum channels.
 * You usually don't want to interact with this object.
 */
public interface ServerForumChannelBuilderDelegate extends RegularServerChannelBuilderDelegate {

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     */
    void setCategory(ChannelCategory category);

    /**
     * Creates the server forum channel.
     *
     * @return The created forum channel.
     */
    CompletableFuture<ServerForumChannel> create();
}
