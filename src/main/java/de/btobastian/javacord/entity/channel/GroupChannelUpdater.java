package de.btobastian.javacord.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update group channels.
 */
public interface GroupChannelUpdater {

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     * @return The current instance in order to chain call methods.
     */
    GroupChannelUpdater setName(String name);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
