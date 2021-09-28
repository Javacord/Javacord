package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.PositionableServerChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class PositionableServerChannelUpdater extends ServerChannelUpdater {

    /**
     * The server channel delegate used by this instance.
     */
    private final PositionableServerChannelUpdaterDelegate delegate;

    /**
     * Creates a new server channel updater without delegate.
     */
    protected PositionableServerChannelUpdater() {
        delegate = null;
    }

    /**
     * Creates a new server channel updater.
     *
     * @param channel The channel to update.
     */
    public PositionableServerChannelUpdater(PositionableServerChannel channel) {
        delegate = DelegateFactory.createPositionableServerChannelUpdaterDelegate(channel);
    }

    /**
     * Queues the raw position to be updated.
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link PositionableServerChannel#getRawPosition()} instead of
     *                    {@link PositionableServerChannel#getPosition()}!
     * @return The current instance in order to chain call methods.
     */
    public PositionableServerChannelUpdater setRawPosition(int rawPosition) {
        delegate.setRawPosition(rawPosition);
        return this;
    }
}
