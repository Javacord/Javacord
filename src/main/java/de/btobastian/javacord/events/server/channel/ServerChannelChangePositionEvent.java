package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ServerChannel;

import java.util.Optional;

/**
 * A server channel change position event.
 */
public class ServerChannelChangePositionEvent extends ServerChannelEvent {

    /**
     * The new position of the channel.
     */
    private final int newPosition;

    /**
     * The old position of the channel.
     */
    private final int oldPosition;

    /**
     * The new raw position of the channel.
     */
    private final int newRawPosition;

    /**
     * The old raw position of the channel.
     */
    private final int oldRawPosition;

    /**
     * The new category of the channel.
     */
    private final ChannelCategory newCategory;

    /**
     * The old category of the channel.
     */
    private final ChannelCategory oldCategory;

    /**
     * Creates a new server channel change position event.
     *
     * @param channel The channel of the event.
     * @param newPosition The new (logical!) position of the channel.
     * @param oldPosition The old (logical!) position of the channel.
     * @param newRawPosition The new raw position of the channel.
     * @param oldRawPosition The old raw position of the channel.
     * @param newCategory The new category of the channel.
     * @param oldCategory The old category of the channel.
     */
    public ServerChannelChangePositionEvent(ServerChannel channel, int newPosition, int oldPosition,
                                            int newRawPosition, int oldRawPosition,
                                            ChannelCategory newCategory, ChannelCategory oldCategory) {
        super(channel);
        this.newPosition = newPosition;
        this.oldPosition = oldPosition;
        this.newRawPosition = newRawPosition;
        this.oldRawPosition = oldRawPosition;
        this.newCategory = newCategory;
        this.oldCategory = oldCategory;
    }

    /**
     * Gets the new position of the channel.
     *
     * @return The new position of the channel.
     */
    public int getNewPosition() {
        return newPosition;
    }

    /**
     * Gets the old position of the channel.
     *
     * @return The old position of the channel.
     */
    public int getOldPosition() {
        return oldPosition;
    }

    /**
     * Gets the new raw position of the channel.
     *
     * @return The new raw position of the channel.
     */
    public int getNewRawPosition() {
        return newRawPosition;
    }

    /**
     * Gets the old raw position of the channel.
     *
     * @return The old raw position of the channel.
     */
    public int getOldRawPosition() {
        return oldRawPosition;
    }

    /**
     * Gets the new category of the channel.
     *
     * @return The new category of the channel.
     */
    public Optional<ChannelCategory> getNewCategory() {
        return Optional.ofNullable(newCategory);
    }

    /**
     * Gets the old category of the channel.
     *
     * @return The old category of the channel.
     */
    public Optional<ChannelCategory> getOldCategory() {
        return Optional.ofNullable(oldCategory);
    }
}
