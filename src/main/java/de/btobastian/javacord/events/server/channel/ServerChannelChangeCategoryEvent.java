package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ServerChannel;

import java.util.Optional;

/**
 * A server channel change category event.
 */
public class ServerChannelChangeCategoryEvent extends ServerChannelEvent {

    /**
     * The new category of the channel.
     */
    private final ChannelCategory newCategory;

    /**
     * The old category of the channel.
     */
    private final ChannelCategory oldCategory;

    /**
     * Creates a new server channel change name event.
     *
     * @param channel The channel of the event.
     * @param newCategory The new category of the channel.
     * @param oldCategory The old category of the channel.
     */
    public ServerChannelChangeCategoryEvent(
            ServerChannel channel, ChannelCategory newCategory, ChannelCategory oldCategory) {
        super(channel);
        this.newCategory = newCategory;
        this.oldCategory = oldCategory;
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
