package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerVoiceChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server voice channels.
 */
public class ServerVoiceChannelUpdater extends TextableRegularServerChannelUpdater<ServerVoiceChannelUpdater> {

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
        delegate = (ServerVoiceChannelUpdaterDelegate) super.textableRegularServerChannelUpdaterDelegate;
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

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }
}
