package org.javacord.api.entity.server.invite;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.invite.internal.InviteBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * A class to create {@link Invite invite} objects.
 */
public class InviteBuilder {

    /**
     * The invite delegate used by this instance.
     */
    private final InviteBuilderDelegate delegate;

    /**
     * Creates a new invite builder.
     *
     * @param channel The channel for the invite.
     */
    public InviteBuilder(ServerChannel channel) {
        delegate = DelegateFactory.createInviteBuilderDelegate(channel);
    }

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the duration of the invite in seconds before expiry, or 0 for never.
     * The default value is 86400 (24 hours).
     *
     * @param maxAge The duration of the invite in seconds before expiry, or 0 for never.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setMaxAgeInSeconds(int maxAge) {
        delegate.setMaxAgeInSeconds(maxAge);
        return this;
    }

    /**
     * Sets the invite to never expire.
     * This is the same as setting the max age to 0.
     *
     * @return The current instance in order to chain call methods.
     * @see #setMaxAgeInSeconds(int)
     */
    public InviteBuilder setNeverExpire() {
        delegate.setNeverExpire();
        return this;
    }

    /**
     * Sets the max number of uses or 0 for unlimited.
     * The default value is 0 (unlimited uses).
     *
     * @param maxUses The max number of uses or 0 for unlimited.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setMaxUses(int maxUses) {
        delegate.setMaxUses(maxUses);
        return this;
    }

    /**
     * Sets whether this invite should only grant temporary membership or not.
     * The default value is false.
     *
     * @param temporary Whether this invite should only grant temporary membership or not.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setTemporary(boolean temporary) {
        delegate.setTemporary(temporary);
        return this;
    }

    /**
     * Sets whether this invite should be unique or not.
     * The default value is false.
     *
     * @param unique Whether this invite should be unique or not.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setUnique(boolean unique) {
        delegate.setUnique(unique);
        return this;
    }

    /**
     * Creates the invite.
     *
     * @return The created invite.
     */
    public CompletableFuture<Invite> create() {
        return delegate.create();
    }

}
