package org.javacord.api.entity.channel;

import java.util.concurrent.CompletableFuture;

public interface PositionableServerChannel extends ServerChannel, Comparable<PositionableServerChannel> {

    /**
     * Gets the raw position of the channel.
     *
     * <p>This is the positions sent from Discord and might not be unique and have gaps.
     * Also, every channel type (text, voice and category) has its own position counter.
     *
     * @return The raw position of the channel.
     */
    int getRawPosition();

    /**
     * Gets the real position of the channel.
     *
     * <p>Returns <code>-1</code> if the channel is deleted.
     *
     * @return The real position of the channel.
     */
    default int getPosition() {
        return getServer().getChannels().indexOf(this);
    }

    /**
     * Updates the raw position of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link PositionableServerChannel#getRawPosition()} instead of
     *                    {@link PositionableServerChannel#getPosition()}!
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRawPosition(final int rawPosition) {
        return createUpdater().setRawPosition(rawPosition).update();
    }

    /**
     * {@inheritDoc}
     *
     * <p><b><i>Implementation note:</i></b> Only channels from the same server can be compared
     *
     * @throws IllegalArgumentException If the channels are on different servers.
     */
    @Override
    default int compareTo(final PositionableServerChannel channel) {
        if (!getServer().equals(channel.getServer())) {
            throw new IllegalArgumentException("Only channels from the same server can be compared for order");
        }
        return getPosition() - channel.getPosition();
    }

    /**
     * Create an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default PositionableServerChannelUpdater createUpdater() {
        return new PositionableServerChannelUpdater(this);
    }
}
