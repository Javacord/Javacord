package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerMessageChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

public class ServerMessageChannelUpdater extends RegularServerChannelUpdater<ServerMessageChannelUpdater> {

    /**
     * The server text channel delegate used by this instance.
     */
    private final ServerMessageChannelUpdaterDelegate delegate;


    protected ServerMessageChannelUpdater(ServerMessageChannel channel) {
        super(DelegateFactory.createServerMessageChannelUpdaterDelegate(channel));
        delegate = (ServerMessageChannelUpdaterDelegate) super.regularServerChannelUpdaterDelegate;
    }

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerMessageChannelUpdater setTopic(String topic) {
        delegate.setTopic(topic);
        return this;
    }

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerMessageChannelUpdater setNsfwFlag(boolean nsfw) {
        delegate.setNsfwFlag(nsfw);
        return this;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerMessageChannelUpdater setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerMessageChannelUpdater removeCategory() {
        delegate.removeCategory();
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }
}
