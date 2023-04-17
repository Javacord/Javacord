package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerVoiceChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server voice channels.
 */
public class ServerVoiceChannelUpdater extends RegularServerChannelUpdater<ServerVoiceChannelUpdater> {

    /**
     * The server voice channel delegate used by this instance.
     */
    private final ServerVoiceChannelUpdaterDelegate delegate;

    /**
     * Creates a new server voice channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerVoiceChannelUpdater(ServerVoiceChannel channel) {
        super(DelegateFactory.createServerVoiceChannelUpdaterDelegate(channel));
        delegate = (ServerVoiceChannelUpdaterDelegate) super.regularServerChannelUpdaterDelegate;
    }

    /**
     * Queues the bitrate to be updated.
     *
     * @param bitrate The new bitrate of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setBitrate(int bitrate) {
        delegate.setBitrate(bitrate);
        return this;
    }

    /**
     * Queues the user limit to be updated.
     *
     * @param userLimit The new user limit of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setUserLimit(int userLimit) {
        delegate.setUserLimit(userLimit);
        return this;
    }

    /**
     * Queues the user limit to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater removeUserLimit() {
        delegate.removeUserLimit();
        return this;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater removeCategory() {
        delegate.removeCategory();
        return this;
    }

    /**
     * Queues the nsfw flag to be updated.
     *
     * @param nsfw Whether the channel should be marked as 'not safe for work'
     *             or not.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setNsfw(Boolean nsfw) {
        delegate.setNsfw(nsfw);
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }
}
