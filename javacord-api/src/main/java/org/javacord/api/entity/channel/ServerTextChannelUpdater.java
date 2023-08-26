package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerTextChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server text channels.
 */
public class ServerTextChannelUpdater extends TextableRegularServerChannelUpdater<ServerTextChannelUpdater> {

    /**
     * The server text channel delegate used by this instance.
     */
    private final ServerTextChannelUpdaterDelegate delegate;

    /**
     * Creates a new server text channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerTextChannelUpdater(ServerTextChannel channel) {
        super(DelegateFactory.createServerTextChannelUpdaterDelegate(channel));
        delegate = (ServerTextChannelUpdaterDelegate) super.textableRegularServerChannelUpdaterDelegate;
    }

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setTopic(String topic) {
        delegate.setTopic(topic);
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
