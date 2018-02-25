package de.btobastian.javacord.entities.message.emoji;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiAttachableListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeNameListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeWhitelistedRolesListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiDeleteListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a known custom emoji.
 */
public interface KnownCustomEmoji extends CustomEmoji {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(KnownCustomEmoji.class);

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
    default CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.CUSTOM_EMOJI)
                .setUrlParameters(getServer().getIdAsString(), String.valueOf(getId()))
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    /**
     * Gets the updater for this emoji.
     *
     * @return The updater for this emoji.
     */
    default CustomEmojiUpdater getUpdater() {
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
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param name The new name of the emoji.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return getUpdater().setName(name).update();
    }

    /**
     * Updates the whitelist of the emoji.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param roles The new whitelist.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateWhitelist(Collection<Role> roles) {
        return getUpdater().setWhitelist(roles).update();
    }

    /**
     * Updates the whitelist of the emoji.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param roles The new whitelist.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateWhitelist(Role... roles) {
        return getUpdater().setWhitelist(roles).update();
    }

    /**
     * Removes the whitelist of the emoji.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link CustomEmojiUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeWhitelist() {
        return getUpdater().removeWhitelist().update();
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
                .addObjectListener(KnownCustomEmoji.class, getId(), CustomEmojiChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji update listeners.
     *
     * @return A list with all registered custom emoji update listeners.
     */
    default List<CustomEmojiChangeNameListener> getCustomEmojiChangeNameListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(KnownCustomEmoji.class, getId(), CustomEmojiChangeNameListener.class);
    }

    /**
     * Adds a listener, which listens to custom emoji whitelisted roles changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CustomEmojiChangeWhitelistedRolesListener> addCustomEmojiChangeWhitelistedRolesListener(
            CustomEmojiChangeWhitelistedRolesListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                KnownCustomEmoji.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji change whitelisted roles listeners.
     *
     * @return A list with all registered custom emoji change whitelisted roles listeners.
     */
    default List<CustomEmojiChangeWhitelistedRolesListener> getCustomEmojiChangeWhitelistedRolesListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(KnownCustomEmoji.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class);
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
                .addObjectListener(KnownCustomEmoji.class, getId(), CustomEmojiDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji delete listeners.
     *
     * @return A list with all registered custom emoji delete listeners.
     */
    default List<CustomEmojiDeleteListener> getCustomEmojiDeleteListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(KnownCustomEmoji.class, getId(), CustomEmojiDeleteListener.class);
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
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(KnownCustomEmoji.class, getId(),
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
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(KnownCustomEmoji.class, getId(),
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
        return ((ImplDiscordApi) getApi()).getObjectListeners(KnownCustomEmoji.class, getId());
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
        ((ImplDiscordApi) getApi()).removeObjectListener(KnownCustomEmoji.class, getId(), listenerClass, listener);
    }

}
