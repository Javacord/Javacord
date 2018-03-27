package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.GroupChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update group channels.
 */
public class GroupChannelUpdater {

    /**
     * The group channel delegate used by this instance.
     */
    private final GroupChannelUpdaterDelegate delegate;

    /**
     * Creates a new group channel updater.
     *
     * @param channel The channel to update.
     */
    public GroupChannelUpdater(GroupChannel channel) {
        delegate = DelegateFactory.createGroupChannelUpdaterDelegate(channel);
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public GroupChannelUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
