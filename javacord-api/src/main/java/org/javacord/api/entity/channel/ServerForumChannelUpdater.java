package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerForumChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server forum channels.
 */
public class ServerForumChannelUpdater extends RegularServerChannelUpdater<ServerForumChannelUpdater> {

    /**
     * The server forum channel delegate used by this instance.
     */
    private final ServerForumChannelUpdaterDelegate delegate;

    /**
     * Creates a new server forum channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerForumChannelUpdater(ServerForumChannel channel) {
        super(DelegateFactory.createServerForumChannelUpdaterDelegate(channel));
        delegate = (ServerForumChannelUpdaterDelegate) super.regularServerChannelUpdaterDelegate;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater removeCategory() {
        delegate.removeCategory();
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
