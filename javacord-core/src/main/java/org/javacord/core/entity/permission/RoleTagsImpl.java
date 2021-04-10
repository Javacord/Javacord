package org.javacord.core.entity.permission;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.permission.RoleTags;

import java.util.Optional;

public class RoleTagsImpl implements RoleTags {

    /**
     * The id of the bot this role belongs to.
     */
    private final Long botId;

    /**
     * The id of the integration this role belongs to.
     */
    private final Long integrationId;

    /**
     * Whether the role is the premium subscription role.
     */
    private final boolean isPremiumSubscriptionRole;

    /**
     * Creates a new role tags object.
     *
     * @param data The json data of the role tags.
     */
    public RoleTagsImpl(JsonNode data) {
        this.botId = data.hasNonNull("bot_id") ? data.get("bot_id").asLong(0) : null;
        this.integrationId = data.hasNonNull("integration_id") ? data.get("integration_id").asLong(0) : null;
        this.isPremiumSubscriptionRole = data.has("premium_subscriber");
    }

    @Override
    public Optional<Long> getBotId() {
        return Optional.ofNullable(botId);
    }

    @Override
    public Optional<Long> getIntegrationId() {
        return Optional.ofNullable(integrationId);
    }

    @Override
    public boolean isPremiumSubscriptionRole() {
        return isPremiumSubscriptionRole;
    }
}
