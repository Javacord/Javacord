package de.btobastian.javacord.entity.channel;

import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.channel.server.ChannelCategoryAttachableListener;
import de.btobastian.javacord.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import de.btobastian.javacord.util.event.ListenerManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a channel category.
 */
public interface ChannelCategory extends ServerChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.CHANNEL_CATEGORY;
    }

    List<ServerChannel> getChannels();

    /**
     * Gets a sorted (by position) list with all channels in this category the given user can see.
     *
     * @param user The user to check.
     * @return The visible channels in the category.
     */
    default List<ServerChannel> getVisibleChannels(User user) {
        List<ServerChannel> channels = new ArrayList<>(getChannels());
        channels.removeIf(channel -> !channel.canSee(user));
        return Collections.unmodifiableList(channels);
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

    /**
     * Adds a listener, which listens to server channel nsfw flag changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener);

    /**
     * Gets a list with all registered server channel change nsfw flag listeners.
     *
     * @return A list with all registered server channel change nsfw flag listeners.
     */
    List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners();

    /**
     * Adds a listener that implements one or more {@code ChannelCategoryAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends ChannelCategoryAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ChannelCategoryAttachableListener>> addChannelCategoryAttachableListener(
            T listener);

    /**
     * Removes a listener that implements one or more {@code ChannelCategoryAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ChannelCategoryAttachableListener & ObjectAttachableListener> void
    removeChannelCategoryAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code ChannelCategoryAttachableListener}s
     * and their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more
     * {@code ChannelCategoryAttachableListener}s and their assigned listener classes they listen to.
     */
    <T extends ChannelCategoryAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getChannelCategoryAttachableListeners();

    /**
     * Removes a listener from this server text channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ChannelCategoryAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

    @Override
    default Optional<ChannelCategory> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getChannelCategoryById(getId()));
    }

    @Override
    default CompletableFuture<ChannelCategory> getLatestInstance() {
        Optional<ChannelCategory> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<ChannelCategory> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}
