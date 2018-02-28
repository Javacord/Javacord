package de.btobastian.javacord.entity.channel;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.listener.ChannelAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.VoiceChannelAttachableListener;
import de.btobastian.javacord.listener.channel.group.GroupChannelAttachableListener;
import de.btobastian.javacord.listener.channel.group.GroupChannelChangeNameListener;
import de.btobastian.javacord.listener.channel.group.GroupChannelDeleteListener;
import de.btobastian.javacord.util.ClassHelper;
import de.btobastian.javacord.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a group channel. Group channels are not supported by bot accounts!
 */
public interface GroupChannel extends TextChannel, VoiceChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.GROUP_CHANNEL;
    }

    /**
     * Gets the members of the group channel.
     *
     * @return The members of the group channel.
     */
    Collection<User> getMembers();

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    Optional<String> getName();

    /**
     * Gets the icon of the group channel.
     *
     * @return The icon of the group channel.
     */
    Optional<Icon> getIcon();

    /**
     * Checks if the user is a member of this group channel.
     *
     * @param user The user to check.
     * @return Whether the user is a member of this group channel or not.
     */
    default boolean isMember(User user) {
        return user.isYourself() || getMembers().contains(user);
    }

    /**
     * Gets the updater for this channel.
     *
     * @return The updater for this channel.
     */
    default GroupChannelUpdater getUpdater() {
        return new GroupChannelUpdater(this);
    }

    /**
     * Updates the name of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link GroupChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param name The new name of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return getUpdater().setName(name).update();
    }

    /**
     * Adds a listener, which listens to this group channel name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                GroupChannel.class, getId(), GroupChannelChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered group channel change name listeners.
     *
     * @return A list with all registered group channel change name listeners.
     */
    default List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                GroupChannel.class, getId(), GroupChannelChangeNameListener.class);
    }

    /**
     * Adds a listener, which listens to this channel being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(
            GroupChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                GroupChannel.class, getId(), GroupChannelDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered group channel delete listeners.
     *
     * @return A list with all registered group channel delete listeners.
     */
    default List<GroupChannelDeleteListener> getGroupChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                GroupChannel.class, getId(), GroupChannelDeleteListener.class);
    }

    /**
     * Adds a listener that implements one or more {@code GroupChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends GroupChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends GroupChannelAttachableListener>> addGroupChannelAttachableListener(
            T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GroupChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(GroupChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code GroupChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends GroupChannelAttachableListener & ObjectAttachableListener> void
    removeGroupChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GroupChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((ImplDiscordApi) getApi()).removeObjectListener(GroupChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code GroupChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code GroupChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    @SuppressWarnings("unchecked")
    default <T extends GroupChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getGroupChannelAttachableListeners() {
        Map<T, List<Class<T>>> groupChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(GroupChannel.class, getId());
        getTextChannelAttachableListeners().forEach((listener, listenerClasses) -> groupChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getVoiceChannelAttachableListeners().forEach((listener, listenerClasses) -> groupChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> groupChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return groupChannelListeners;
    }

    /**
     * Removes a listener from this group channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends GroupChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(GroupChannel.class, getId(), listenerClass, listener);
    }

    @Override
    default Optional<GroupChannel> getCurrentCachedInstance() {
        return getApi().getGroupChannelById(getId());
    }

    @Override
    default CompletableFuture<GroupChannel> getLatestInstance() {
        Optional<GroupChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<GroupChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}
