package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.interaction.InteractionComponentData;

public class InteractionComponentDataImpl implements InteractionComponentData {
    private final String customId;

    private final ComponentType type;

    /**
     * Create a new component data object.
     *
     * @param data JSON data from Discord.
     */
    public InteractionComponentDataImpl(JsonNode data) {
        this.customId = data.get("custom_id").asText();
        this.type = ComponentType.fromId(data.get("component_type").asInt());
    }

    @Override
    public String getCustomId() {
        return customId;
    }

    @Override
    public ComponentType getType() {
        return type;
    }
}
