package org.javacord.api.entity.server.invite;

import org.javacord.api.entity.user.User;

import java.time.Instant;

/**
 * This class represents an invite with additional information like expire date, creator, etc.
 */
public interface RichInvite extends Invite {

    /**
     * Gets the user who created the invite.
     *
     * @return The user who created the invite.
     */
    User getInviter();

    /**
     * Gets the number of times this invite has been used.
     *
     * @return The number of times this invite has been used.
     */
    int getUses();

    /**
     * Gets the max number of times this invite can be used.
     *
     * @return The max number of times this invite can be used.
     */
    int getMaxUses();

    /**
     * Gets the duration (in seconds) after which the invite expires.
     *
     * @return The duration (in seconds) after which the invite expires.
     */
    int getMaxAgeInSeconds();

    /**
     * Checks if the invite only grants temporary membership.
     *
     * @return Whether the invite only grants temporary membership or not.
     */
    boolean isTemporary();

    /**
     * Gets the creation date of the invite.
     *
     * @return The creation date of the invite.
     */
    Instant getCreationTimestamp();

    /**
     * Checks if the invite is revoked.
     *
     * @return Whether the invite is revoked or not.
     */
    boolean isRevoked();
}
