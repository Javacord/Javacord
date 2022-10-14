package org.javacord.api.entity.channel.forum;

import org.javacord.api.entity.DiscordEntity;

public interface PermissionOverwrite extends DiscordEntity {
    /**
     * Gets the type of the permission overwrite.
     * Either 0 (role) or 1 (member).
     *
     * @return The type of the permission overwrite.
     */
    int getType();

    /**
     * Gets the allowed permissions bit set.
     *
     * @return The allowed permissions bit set.
     */
    int getAllowed();

    /**
     * Gets the denied permissions bit set.
     *
     * @return The denied permissions bit set.
     */
    int getDenied();
}
