package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.User;
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
     * Gets a sorted (by position) list with all channels in this category the given user can see.
     *
     * @param user The user to check.
     * @return The visible channels in the category.
     */
    default List<ServerChannel> getVisibleChannels(User user) {
        List<ServerChannel> channels = getChannels();
        channels.removeIf(channel -> !channel.canSee(user));
        return channels;
    }

    /**
     * Checks if the given user can see all channels in this category.
     *
     * @param user The user to check.
     * @return Whether the given user can see all channels in this category or not.
     */
    default boolean canSeeAll(User user) {
        for (ServerChannel channel : getChannels()) {
            if (!channel.canSee(user)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the user of the connected account can see all channels in this category.
     *
     * @return Whether the user of the connected account can see all channels in this category or not.
     */
    default boolean canYouSeeAll() {
        return canSeeAll(getApi().getYourself());
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
