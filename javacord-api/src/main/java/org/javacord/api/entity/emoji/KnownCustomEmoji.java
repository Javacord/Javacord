package org.javacord.api.entity.emoji;

import org.javacord.api.entity.Deletable;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiAttachableListenerManager;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a known custom emoji.
 */
public interface KnownCustomEmoji extends CustomEmoji, Deletable, UpdatableFromCache<KnownCustomEmoji>,
                                          KnownCustomEmojiAttachableListenerManager {

    /**
     * Gets the server of the emoji.
     *
     * @return The server of the emoji.
     */
    Server getServer();

    /**
     * Creates an updater for this emoji.
     *
     * @return An updater for this emoji.
     */
    default CustomEmojiUpdater createUpdater() {
        return new CustomEmojiUpdater(this);
    }

    /**
     * Gets all whitelisted roles.
     *
     * @return All whitelisted roles.
     */
    Optional<Set<Role>> getWhitelistedRoles();

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
     * Gets the creator of the emoji.
     *
     * @return The user who created the emoji.
     */
    CompletableFuture<Optional<User>> getCreator();

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

    @Override
    default Optional<KnownCustomEmoji> getCurrentCachedInstance() {
        return getApi().getCustomEmojiById(getId());
    }

}
