package de.btobastian.javacord.entities.message.emoji;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplIcon;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiAttachableListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeNameListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiDeleteListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents a custom emoji.
 */
public interface CustomEmoji extends DiscordEntity, Emoji {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(CustomEmoji.class);

    /**
     * Gets the name of the emoji.
     *
     * @return The name of the emoji.
     */
    String getName();

    /**
     * Gets the server of the emoji.
     * We might not know the server of the emoji, if it was sent by a nitro user!
     *
     * @return The server of the emoji.
     */
    Optional<Server> getServer();

    /**
     * Gets the image of the emoji.
     *
     * @return The image of the emoji.
     */
    default Icon getImage() {
        String urlString = "https://cdn.discordapp.com/emojis/" + getId() + (isAnimated() ? ".gif" : ".png");
        try {
            return new ImplIcon(getApi(), new URL(urlString));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    default String getMentionTag() {
        return "<" + (isAnimated() ? "a" : "") + ":" + getName() + ":" + getId() + ">";
    }

    @Override
    default Optional<String> asUnicodeEmoji() {
        return Optional.empty();
    }

    @Override
    default Optional<CustomEmoji> asCustomEmoji() {
        return Optional.of(this);
    }

    /**
     * Adds a listener, which listens to this custom emoji being updated.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CustomEmojiChangeNameListener> addCustomEmojiChangeNameListener(
            CustomEmojiChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(CustomEmoji.class, getId(), CustomEmojiChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji update listeners.
     *
     * @return A list with all registered custom emoji update listeners.
     */
    default List<CustomEmojiChangeNameListener> getCustomEmojiChangeNameListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(CustomEmoji.class, getId(), CustomEmojiChangeNameListener.class);
    }

    /**
     * Adds a listener, which listens to this custom emoji being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CustomEmojiDeleteListener> addCustomEmojiDeleteListener(
            CustomEmojiDeleteListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(CustomEmoji.class, getId(), CustomEmojiDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji delete listeners.
     *
     * @return A list with all registered custom emoji delete listeners.
     */
    default List<CustomEmojiDeleteListener> getCustomEmojiDeleteListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(CustomEmoji.class, getId(), CustomEmojiDeleteListener.class);
    }

    /**
     * Adds a listener that implements one or more {@code CustomEmojiAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends CustomEmojiAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addCustomEmojiAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(CustomEmojiAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(CustomEmoji.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code CustomEmojiAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends CustomEmojiAttachableListener & ObjectAttachableListener> void
    removeCustomEmojiAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(CustomEmojiAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(CustomEmoji.class, getId(),
                                                                                           listenerClass, listener));
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code CustomEmojiAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code CustomEmojiAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    default <T extends CustomEmojiAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getCustomEmojiAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(CustomEmoji.class, getId());
    }

    /**
     * Removes a listener from this custom emoji.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends CustomEmojiAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(CustomEmoji.class, getId(), listenerClass, listener);
    }

}
