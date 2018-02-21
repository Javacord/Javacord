package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listeners.ChannelAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.VoiceChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelChangeBitrateListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberJoinListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberLeaveListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Gets the updater for this channel.
     *
     * @return The updater for this channel.
     */
    default ServerVoiceChannelUpdater getUpdater() {
        return new ServerVoiceChannelUpdater(this);
    }

    /**
     * Updates the bitrate of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param bitrate The new bitrate of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateBitrate(int bitrate) {
        return getUpdater().setBitrate(bitrate).update();
    }

    /**
     * Updates the user limit of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param userLimit The new user limit of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateUserLimit(int userLimit) {
        return getUpdater().setUserLimit(userLimit).update();
    }

    /**
     * Removes the user limit of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeUserLimit() {
        return getUpdater().removeUserLimit().update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return getUpdater().setCategory(category).update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return getUpdater().removeCategory().update();
    }

    /**
     * Adds a listener, which listens to users joining this server voice channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberJoinListener.class, listener);
    }

    /**
     * Gets a list with all registered server voice channel member join listeners.
     *
     * @return A list with all registered server voice channel member join listeners.
     */
    default List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberJoinListener.class);
    }

    /**
     * Adds a listener, which listens to users leaving this server voice channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    /**
     * Gets a list with all registered server voice channel member leave listeners.
     *
     * @return A list with all registered server voice channel member leave listeners.
     */
    default List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberLeaveListener.class);
    }

    /**
     * Adds a listener, which listens to bitrate changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeBitrateListener.class, listener);
    }

    /**
     * Gets a list with all registered server voice channel change bitrate listeners.
     *
     * @return A list with all registered server voice channel change bitrate listeners.
     */
    default List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeBitrateListener.class);
    }

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
    @SuppressWarnings("unchecked")
    default <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ServerVoiceChannelAttachableListener>>
    addServerVoiceChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerVoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(
                                ServerVoiceChannel.class, getId(), listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code ServerVoiceChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> void
    removeServerVoiceChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerVoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((ImplDiscordApi) getApi()).removeObjectListener(ServerVoiceChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code ServerVoiceChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more
     * {@code ServerVoiceChannelAttachableListener}s and their assigned listener classes they listen to.
     */
    @SuppressWarnings("unchecked")
    default <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerVoiceChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverVoiceChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(ServerVoiceChannel.class, getId());
        getVoiceChannelAttachableListeners().forEach((listener, listenerClasses) -> serverVoiceChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getServerChannelAttachableListeners().forEach((listener, listenerClasses) -> serverVoiceChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> serverVoiceChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return serverVoiceChannelListeners;
    }

    /**
     * Removes a listener from this server voice channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(ServerVoiceChannel.class, getId(), listenerClass, listener);
    }

}
