package org.javacord.api.event.channel.server;

import org.javacord.api.entity.channel.ChannelCategory;

import java.util.Optional;

/**
 * A server channel change position event.
 */
public interface ServerChannelChangePositionEvent extends ServerChannelEvent {

    /**
     * Gets the new position of the channel.
     *
     * @return The new position of the channel.
     */
    int getNewPosition();

    /**
     * Gets the old position of the channel.
     *
     * @return The old position of the channel.
     */
    int getOldPosition();

    /**
     * Gets the new raw position of the channel.
     *
     * @return The new raw position of the channel.
     */
    int getNewRawPosition();

    /**
     * Gets the old raw position of the channel.
     *
     * @return The old raw position of the channel.
     */
    int getOldRawPosition();

    /**
     * Gets the new category of the channel.
     *
     * @return The new category of the channel.
     */
    Optional<ChannelCategory> getNewCategory();

    /**
     * Gets the old category of the channel.
     *
     * @return The old category of the channel.
     */
    Optional<ChannelCategory> getOldCategory();

}
