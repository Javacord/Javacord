package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.GroupChannelUpdater;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link GroupChannelUpdater} to update group channels.
 * You usually don't want to interact with this object.
 */
public interface GroupChannelUpdaterDelegate {

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     */
    void setName(String name);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
