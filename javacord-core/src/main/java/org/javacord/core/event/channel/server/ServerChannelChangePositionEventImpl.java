package org.javacord.core.event.channel.server;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.server.ServerChannelChangePositionEvent;

import java.util.Optional;

/**
 * The implementation of {@link ServerChannelChangePositionEvent}.
 */
public class ServerChannelChangePositionEventImpl extends ServerChannelEventImpl
        implements ServerChannelChangePositionEvent {

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
    public ServerChannelChangePositionEventImpl(ServerChannel channel, int newPosition, int oldPosition,
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

    @Override
    public int getNewPosition() {
        return newPosition;
    }

    @Override
    public int getOldPosition() {
        return oldPosition;
    }

    @Override
    public int getNewRawPosition() {
        return newRawPosition;
    }

    @Override
    public int getOldRawPosition() {
        return oldRawPosition;
    }

    @Override
    public Optional<ChannelCategory> getNewCategory() {
        return Optional.ofNullable(newCategory);
    }

    @Override
    public Optional<ChannelCategory> getOldCategory() {
        return Optional.ofNullable(oldCategory);
    }

}
