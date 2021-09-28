package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.PositionableServerChannel;

public interface PositionableServerChannelUpdaterDelegate extends ServerChannelUpdaterDelegate {

    /**
     * Queues the raw position to be updated.
     *
     * @param rawPosition The new position of the channel.
     *                    If you want to update the position based on other channels, make sure to use
     *                    {@link PositionableServerChannel#getRawPosition()} instead of
     *                    {@link PositionableServerChannel#getPosition()}!
     */
    void setRawPosition(int rawPosition);
}
