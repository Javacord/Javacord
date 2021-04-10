package org.javacord.api.entity.permission;

import java.util.Optional;

/**
 * This class represents a Role Tag.
 */
public interface RoleTags {

    /**
     * Gets the bot id of the role if it belongs to a bot.
     *
     * @return The bot id of the role.
     */
    Optional<Long> getBotId();

    /**
     * Gets the integration id of the role if it belongs to an integration.
     *
     * @return The integration id of the role.
     */
    Optional<Long> getIntegrationId();

    /**
     * Whether this role is the premium subscription role.
     *
     * @return If this role is the premium subscription role.
     */
    boolean isPremiumSubscriptionRole();
}
