package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelCategoryBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ChannelCategoryBuilder} to create channel categories.
 * You usually don't want to interact with this object.
 */
public interface ChannelCategoryBuilderDelegate extends ServerChannelBuilderDelegate {

    /**
     * Creates the channel category.
     *
     * @return The created channel category.
     */
    CompletableFuture<ChannelCategory> create();

}
