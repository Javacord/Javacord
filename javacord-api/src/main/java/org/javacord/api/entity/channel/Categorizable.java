package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ChannelSpecialization;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an entity which can have a channel category.
 */
public interface Categorizable extends ChannelSpecialization {

    /**
     * Gets the category of the channel.
     *
     * @return The category of the channel.
     */
    Optional<ChannelCategory> getCategory();

    /**
     * Updates the category of the channel.
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> updateCategory(ChannelCategory category);

    /**
     * Removes the category of the channel.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> removeCategory();

}
