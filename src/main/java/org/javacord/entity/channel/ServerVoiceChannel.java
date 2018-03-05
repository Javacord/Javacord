package org.javacord.entity.channel;

import org.javacord.entity.user.User;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelAttachableListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server voice channel.
 */
public interface ServerVoiceChannel extends ServerChannel, VoiceChannel, Categorizable {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_VOICE_CHANNEL;
    }

    /**
     * Gets the bitrate (int bits) of the channel.
     *
     * @return The bitrate of the channel.
     */
    int getBitrate();

    /**
     * Gets the user limit of the channel.
     *
     * @return The user limit.
     */
    Optional<Integer> getUserLimit();

    /**
     * Gets the users that are connected to this server voice channel.
     *
     * @return The users that are connected to this server voice channel.
     */
    Collection<User> getConnectedUsers();

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    ServerVoiceChannelUpdater createUpdater();

    /**
     * Updates the bitrate of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param bitrate The new bitrate of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateBitrate(int bitrate) {
        return createUpdater().setBitrate(bitrate).update();
    }

    /**
     * Updates the user limit of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param userLimit The new user limit of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateUserLimit(int userLimit) {
        return createUpdater().setUserLimit(userLimit).update();
    }

    /**
     * Removes the user limit of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeUserLimit() {
        return createUpdater().removeUserLimit().update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return createUpdater().setCategory(category).update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return createUpdater().removeCategory().update();
    }

    /**
     * Adds a listener, which listens to users joining this server voice channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener);

    /**
     * Gets a list with all registered server voice channel member join listeners.
     *
     * @return A list with all registered server voice channel member join listeners.
     */
    List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners();

    /**
     * Adds a listener, which listens to users leaving this server voice channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener);

    /**
     * Gets a list with all registered server voice channel member leave listeners.
     *
     * @return A list with all registered server voice channel member leave listeners.
     */
    List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners();

    /**
     * Adds a listener, which listens to bitrate changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener);

    /**
     * Gets a list with all registered server voice channel change bitrate listeners.
     *
     * @return A list with all registered server voice channel change bitrate listeners.
     */
    List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners();

    /**
     * Adds a listener, which listens to user limit changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener);

    /**
     * Gets a list with all registered server voice channel change user limit listeners.
     *
     * @return A list with all registered server voice channel change user limit listeners.
     */
    List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners();

    /**
     * Adds a listener that implements one or more {@code ServerVoiceChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ServerVoiceChannelAttachableListener>> addServerVoiceChannelAttachableListener(
            T listener);

    /**
     * Removes a listener that implements one or more {@code ServerVoiceChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> void
    removeServerVoiceChannelAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code ServerVoiceChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more
     * {@code ServerVoiceChannelAttachableListener}s and their assigned listener classes they listen to.
     */
    <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerVoiceChannelAttachableListeners();

    /**
     * Removes a listener from this server voice channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

    @Override
    default Optional<ServerVoiceChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getVoiceChannelById(getId()));
    }

    @Override
    default CompletableFuture<ServerVoiceChannel> getLatestInstance() {
        Optional<ServerVoiceChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<ServerVoiceChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}
