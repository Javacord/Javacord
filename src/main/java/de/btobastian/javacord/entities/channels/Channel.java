package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.listeners.ChannelAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class represents a channel.
 */
public interface Channel extends DiscordEntity {

    /**
     * Gets the type of the channel.
     *
     * @return The type of the channel.
     */
    ChannelType getType();

    /**
     * Gets the channel as group channel.
     *
     * @return The channel as group channel.
     */
    default Optional<GroupChannel> asGroupChannel() {
        if (this instanceof GroupChannel) {
            return Optional.of((GroupChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as private channel.
     *
     * @return The channel as private channel.
     */
    default Optional<PrivateChannel> asPrivateChannel() {
        if (this instanceof PrivateChannel) {
            return Optional.of((PrivateChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as server channel.
     *
     * @return The channel as server channel.
     */
    default Optional<ServerChannel> asServerChannel() {
        if (this instanceof ServerChannel) {
            return Optional.of((ServerChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as channel category.
     *
     * @return The channel as channel category.
     */
    default Optional<ChannelCategory> asChannelCategory() {
        if (this instanceof ChannelCategory) {
            return Optional.of((ChannelCategory) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as categorizable.
     *
     * @return The channel as categorizable.
     */
    default Optional<Categorizable> asCategorizable() {
        if (this instanceof Categorizable) {
            return Optional.of((Categorizable) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as server text channel.
     *
     * @return The channel as server text channel.
     */
    default Optional<ServerTextChannel> asServerTextChannel() {
        if (this instanceof ServerTextChannel) {
            return Optional.of((ServerTextChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as server voice channel.
     *
     * @return The channel as server voice channel.
     */
    default Optional<ServerVoiceChannel> asServerVoiceChannel() {
        if (this instanceof ServerVoiceChannel) {
            return Optional.of((ServerVoiceChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as text channel.
     *
     * @return The channel as text channel.
     */
    default Optional<TextChannel> asTextChannel() {
        if (this instanceof TextChannel) {
            return Optional.of((TextChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as voice channel.
     *
     * @return The channel as voice channel.
     */
    default Optional<VoiceChannel> asVoiceChannel() {
        if (this instanceof VoiceChannel) {
            return Optional.of((VoiceChannel) this);
        }
        return Optional.empty();
    }


    /**
     * Checks if the given user can see this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can see this channel or not.
     */
    default boolean canSee(User user) {
        Optional<PrivateChannel> privateChannel = asPrivateChannel();
        if (privateChannel.isPresent()) {
            return user.isYourself() || privateChannel.get().getRecipient() == user;
        }
        Optional<GroupChannel> groupChannel = asGroupChannel();
        if (groupChannel.isPresent()) {
            return user.isYourself() || groupChannel.get().getMembers().contains(user);
        }
        Optional<ServerChannel> severChannel = asServerChannel();
        return !severChannel.isPresent()
                || severChannel.get().hasAnyPermission(user,
                                                       PermissionType.ADMINISTRATOR,
                                                       PermissionType.READ_MESSAGES);
    }

    /**
     * Checks if the user of the connected account can see this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can see this channel or not.
     */
    default boolean canYouSee() {
        return canSee(getApi().getYourself());
    }

    /**
     * Adds a listener that implements one or more {@code ChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends ChannelAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(Channel.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code ChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends ChannelAttachableListener & ObjectAttachableListener> void removeChannelAttachableListener(
            T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(Channel.class, getId(),
                                                                                           listenerClass, listener));
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code ChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code ChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    default <T extends ChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getChannelAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Channel.class, getId());
    }

    /**
     * Removes a listener from this channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends ChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(Channel.class, getId(), listenerClass, listener);
    }

}
