package org.javacord.api.entity.server.invite.internal;

import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.server.invite.InviteBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link InviteBuilder} to create invites.
 * You usually don't want to interact with this object.
 */
public interface InviteBuilderDelegate {

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the duration of the invite in seconds before expiry, or 0 for never.
     * The default value is 86400 (24 hours).
     *
     * @param maxAge The duration of the invite in seconds before expiry, or 0 for never.
     */
    void setMaxAgeInSeconds(int maxAge);

    /**
     * Sets the invite to never expire.
     * This is the same as setting the max age to 0.
     *
     * @see #setMaxAgeInSeconds(int)
     */
    void setNeverExpire();

    /**
     * Sets the max number of uses or 0 for unlimited.
     * The default value is 0 (unlimited uses).
     *
     * @param maxUses The max number of uses or 0 for unlimited.
     */
    void setMaxUses(int maxUses);

    /**
     * Sets whether this invite should only grant temporary membership or not.
     * The default value is false.
     *
     * @param temporary Whether this invite should only grant temporary membership or not.
     */
    void setTemporary(boolean temporary);

    /**
     * Sets whether this invite should be unique or not.
     * The default value is false.
     *
     * @param unique Whether this invite should be unique or not.
     */
    void setUnique(boolean unique);

    /**
     * Creates the invite.
     *
     * @return The created invite.
     */
    CompletableFuture<Invite> create();

}
