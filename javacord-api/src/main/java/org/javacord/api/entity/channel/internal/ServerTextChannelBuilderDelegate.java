package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerTextChannelBuilder} to create server text channels.
 * You usually don't want to interact with this object.
 */
public interface ServerTextChannelBuilderDelegate extends ServerChannelBuilderDelegate {

    /**
     * Sets the topic of the channel.
     *
     * @param topic The topic of the channel.
     */
    void setTopic(String topic);

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

    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    CompletableFuture<ServerTextChannel> create();
}
