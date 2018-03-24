package org.javacord.api.entity.emoji;

import org.javacord.api.entity.emoji.internal.CustomEmojiUpdaterDelegate;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update known custom emojis.
 */
public class CustomEmojiUpdater {

    /**
     * The custom emoji delegate used by this instance.
     */
    private final CustomEmojiUpdaterDelegate delegate;

    /**
     * Creates a new custom emoji updater.
     *
     * @param emoji The custom emoji to update.
     */
    public CustomEmojiUpdater(KnownCustomEmoji emoji) {
        delegate = DelegateFactory.createCustomEmojiUpdaterDelegate(emoji);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Queues a role to be added to the whitelist.
     *
     * @param role The role to add.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater addRoleToWhitelist(Role role) {
        delegate.addRoleToWhitelist(role);
        return this;
    }

    /**
     * Queues a role to be removed from the whitelist.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param role The role to remove.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater removeRoleFromWhitelist(Role role) {
        delegate.removeRoleFromWhitelist(role);
        return this;
    }

    /**
     * Queues the whitelist to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater removeWhitelist() {
        delegate.removeWhitelist();
        return this;
    }

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setWhitelist(Collection<Role> roles) {
        delegate.setWhitelist(roles);
        return this;
    }

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setWhitelist(Role... roles) {
        delegate.setWhitelist(roles);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
