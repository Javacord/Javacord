package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.listeners.ChannelAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeCategoryListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNsfwFlagListener;
import de.btobastian.javacord.listeners.server.channel.ServerTextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerTextChannelChangeTopicListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a server text channel.
 */
public interface ServerTextChannel extends ServerChannel, TextChannel, Mentionable, Categorizable {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_TEXT_CHANNEL;
    }

    /**
     * Checks is the channel is "not safe for work".
     *
     * @return Whether the channel is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Gets the topic of the channel.
     *
     * @return The topic of the channel.
     */
    String getTopic();

    /**
     * Gets the updater for this channel.
     *
     * @return The updater for this channel.
     */
    default ServerTextChannelUpdater getUpdater() {
        return new ServerTextChannelUpdater(this);
    }

    /**
     * Updates the topic of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param topic The new topic of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateTopic(String topic) {
        return getUpdater().setTopic(topic).update();
    }

    /**
     * Updates the nsfw flag of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNsfwFlag(boolean nsfw) {
        return getUpdater().setNsfwFlag(nsfw).update();
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
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return getUpdater().removeCategory().update();
    }

    /**
     * Adds a listener, which listens to topic changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class, listener);
    }

    /**
     * Gets a list with all registered server text channel change topic listeners.
     *
     * @return A list with all registered server text channel change topic listeners.
     */
    default List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class);
    }

    /**
     * Adds a listener, which listens to server channel nsfw flag changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerChannelChangeNsfwFlagListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change nsfw flag listeners.
     *
     * @return A list with all registered server channel change nsfw flag listeners.
     */
    default List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerTextChannel.class, getId(), ServerChannelChangeNsfwFlagListener.class);
    }

    /**
     * Adds a listener, which listens to category changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelChangeCategoryListener> addServerChannelChangeCategoryListener(
            ServerChannelChangeCategoryListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerChannelChangeCategoryListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change category listeners.
     *
     * @return A list with all registered server channel change category listeners.
     */
    default List<ServerChannelChangeCategoryListener> getServerChannelChangeCategoryListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerTextChannel.class, getId(), ServerChannelChangeCategoryListener.class);
    }

    /**
     * Adds a listener that implements one or more {@code ServerTextChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends ServerTextChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ServerTextChannelAttachableListener>>
    addServerTextChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerTextChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(ServerTextChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code ServerTextChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> void
    removeServerTextChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerTextChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((ImplDiscordApi) getApi()).removeObjectListener(ServerTextChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code ServerTextChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more
     * {@code ServerTextChannelAttachableListener}s and their assigned listener classes they listen to.
     */
    @SuppressWarnings("unchecked")
    default <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerTextChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverTextChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(ServerTextChannel.class, getId());
        getTextChannelAttachableListeners().forEach((listener, listenerClasses) -> serverTextChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getServerChannelAttachableListeners().forEach((listener, listenerClasses) -> serverTextChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> serverTextChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return serverTextChannelListeners;
    }

    /**
     * Removes a listener from this server text channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(ServerTextChannel.class, getId(), listenerClass, listener);
    }

}
