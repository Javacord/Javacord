package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.impl.ImplServer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a channel category.
 */
public interface ChannelCategory extends ServerChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.CHANNEL_CATEGORY;
    }

    /**
     * Gets a sorted (by position) list of all channels in the category.
     *
     * @return The channels in the category.
     */
    default List<ServerChannel> getChannels() {
        List<ServerChannel> channels = new ArrayList<>();
        ((ImplServer) getServer()).getUnorderedChannels().stream()
                .filter(channel -> channel.asServerTextChannel().isPresent())
                .map(channel -> channel.asServerTextChannel().get())
                .filter(channel -> channel.getCategory().orElse(null) == this)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEach(channels::add);
        ((ImplServer) getServer()).getUnorderedChannels().stream()
                .filter(channel -> channel.asServerVoiceChannel().isPresent())
                .map(channel -> channel.asServerVoiceChannel().get())
                .filter(channel -> channel.getCategory().orElse(null) == this)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEach(channels::add);
        return channels;
    }

    /**
     * Checks is the category is "not safe for work".
     *
     * @return Whether the category is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Adds a channel to this category.
     *
     * @param channel The channel to add.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> addChannel(ServerTextChannel channel) {
        return channel.updateCategory(this);
    }

    /**
     * Adds a channel to this category.
     *
     * @param channel The channel to add.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> addChannel(ServerVoiceChannel channel) {
        return channel.updateCategory(this);
    }

    /**
     * Removes a channel from this category.
     *
     * @param channel The channel to remove.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeChannel(ServerTextChannel channel) {
        if (channel.getCategory().orElse(null) == this) {
            return channel.removeCategory();
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Removes a channel from this category.
     *
     * @param channel The channel to remove.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeChannel(ServerVoiceChannel channel) {
        if (channel.getCategory().orElse(null) == this) {
            return channel.removeCategory();
        }
        return CompletableFuture.completedFuture(null);
    }

}
