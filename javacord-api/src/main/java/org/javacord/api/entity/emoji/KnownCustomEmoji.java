package org.javacord.api.entity.emoji;

import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiAttachableListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeNameListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeWhitelistedRolesListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiDeleteListener;
import org.javacord.api.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a known custom emoji.
 */
public interface KnownCustomEmoji extends CustomEmoji, UpdatableFromCache<KnownCustomEmoji> {

    /**
     * Gets the server of the emoji.
     *
     * @return The server of the emoji.
     */
    Server getServer();

    /**
     * Deletes the emoji.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the emoji.
     *
     * @param reason The reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> delete(String reason);

    /**
     * Creates an updater for this emoji.
     *
     * @return An updater for this emoji.
     */
    default CustomEmojiUpdater createUpdater() {
        return new CustomEmojiUpdater(this);
    }

    /**
     * Gets a list with all whitelisted roles.
     *
     * @return A list with all whitelisted roles.
     */
    Optional<Collection<Role>> getWhitelistedRoles();

    /**
     * Checks if this emoji must be wrapped in colons.
     *
     * @return Whether this emoji must be wrapped in colons or not.
     */
    boolean requiresColons();

    /**
     * Checks if this emoji is managed.
     *
     * @return Whether this emoji is managed or not.
     */
    boolean isManaged();

    /**
     * Updates the name of the emoji.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param name The new name of the emoji.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return createUpdater().setName(name).update();
    }

    /**
     * Updates the whitelist of the emoji.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param roles The new whitelist.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateWhitelist(Collection<Role> roles) {
        return createUpdater().setWhitelist(roles).update();
    }

    /**
     * Updates the whitelist of the emoji.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param roles The new whitelist.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateWhitelist(Role... roles) {
        return createUpdater().setWhitelist(roles).update();
    }

    /**
     * Removes the whitelist of the emoji.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeWhitelist() {
        return createUpdater().removeWhitelist().update();
    }

    /**
     * Adds a listener, which listens to this custom emoji being updated.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<KnownCustomEmojiChangeNameListener> addKnownCustomEmojiChangeNameListener(
            KnownCustomEmojiChangeNameListener listener);

    /**
     * Gets a list with all registered custom emoji update listeners.
     *
     * @return A list with all registered custom emoji update listeners.
     */
    List<KnownCustomEmojiChangeNameListener> getKnownCustomEmojiChangeNameListeners();

    /**
     * Adds a listener, which listens to custom emoji whitelisted roles changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<KnownCustomEmojiChangeWhitelistedRolesListener> addKnownCustomEmojiChangeWhitelistedRolesListener(
            KnownCustomEmojiChangeWhitelistedRolesListener listener);

    /**
     * Gets a list with all registered custom emoji change whitelisted roles listeners.
     *
     * @return A list with all registered custom emoji change whitelisted roles listeners.
     */
    List<KnownCustomEmojiChangeWhitelistedRolesListener> getKnownCustomEmojiChangeWhitelistedRolesListeners();

    /**
     * Adds a listener, which listens to this custom emoji being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<KnownCustomEmojiDeleteListener> addKnownCustomEmojiDeleteListener(
            KnownCustomEmojiDeleteListener listener);

    /**
     * Gets a list with all registered custom emoji delete listeners.
     *
     * @return A list with all registered custom emoji delete listeners.
     */
    List<KnownCustomEmojiDeleteListener> getKnownCustomEmojiDeleteListeners();

    /**
     * Adds a listener that implements one or more {@code KnownCustomEmojiAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends KnownCustomEmojiAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addKnownCustomEmojiAttachableListener(T listener);

    /**
     * Removes a listener that implements one or more {@code KnownCustomEmojiAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends KnownCustomEmojiAttachableListener & ObjectAttachableListener> void
            removeKnownCustomEmojiAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code KnownCustomEmojiAttachableListener}s
     * and their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more
     * {@code KnownCustomEmojiAttachableListener}s and their assigned listener classes they listen to.
     */
    <T extends KnownCustomEmojiAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getKnownCustomEmojiAttachableListeners();

    /**
     * Removes a listener from this custom emoji.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends KnownCustomEmojiAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

    @Override
    default Optional<KnownCustomEmoji> getCurrentCachedInstance() {
        return getApi().getCustomEmojiById(getId());
    }

}
