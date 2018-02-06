package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listeners.group.channel.GroupChannelChangeNameListener;
import de.btobastian.javacord.listeners.group.channel.GroupChannelDeleteListener;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.Collection;
import java.util.List;
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
     * Removes a listener from this group channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T> void removeListener(Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(GroupChannel.class, getId(), listenerClass, listener);
    }

}
