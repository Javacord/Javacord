package org.javacord.api.entity.channel;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.group.GroupChannelAttachableListener;
import org.javacord.api.listener.channel.group.GroupChannelChangeNameListener;
import org.javacord.api.listener.channel.group.GroupChannelDeleteListener;
import org.javacord.api.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default GroupChannelUpdater createUpdater() {
        return new GroupChannelUpdater(this);
    }

    /**
     * Updates the name of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link GroupChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param name The new name of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return createUpdater().setName(name).update();
    }

    /**
     * Adds a listener, which listens to this group channel name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener);

    /**
     * Gets a list with all registered group channel change name listeners.
     *
     * @return A list with all registered group channel change name listeners.
     */
    List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners();

    /**
     * Adds a listener, which listens to this channel being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(GroupChannelDeleteListener listener);

    /**
     * Gets a list with all registered group channel delete listeners.
     *
     * @return A list with all registered group channel delete listeners.
     */
    List<GroupChannelDeleteListener> getGroupChannelDeleteListeners();

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
    <T extends GroupChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends GroupChannelAttachableListener>> addGroupChannelAttachableListener(T listener);

    /**
     * Removes a listener that implements one or more {@code GroupChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends GroupChannelAttachableListener & ObjectAttachableListener> void removeGroupChannelAttachableListener(
            T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code GroupChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code GroupChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    <T extends GroupChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getGroupChannelAttachableListeners();

    /**
     * Removes a listener from this group channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends GroupChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

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
