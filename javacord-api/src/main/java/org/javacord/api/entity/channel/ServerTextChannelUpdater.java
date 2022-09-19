package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerTextChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server text channels.
 */
public class ServerTextChannelUpdater extends RegularServerChannelUpdater<ServerTextChannelUpdater> {

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
        delegate = (ServerTextChannelUpdaterDelegate) super.regularServerChannelUpdaterDelegate;
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

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setNsfwFlag(boolean nsfw) {
        delegate.setNsfwFlag(nsfw);
        return this;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater removeCategory() {
        delegate.removeCategory();
        return this;
    }

    /**
     * Set the delay for slowmode.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setSlowmodeDelayInSeconds(int delay) {
        delegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }

    /**
     * Unset the slowmode.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater unsetSlowmode() {
        return this.setSlowmodeDelayInSeconds(0);
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
