package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.component.Component;
import org.javacord.api.entity.message.component.ComponentType;

public class ComponentImpl implements Component {
    private final ComponentType type;

    public ComponentImpl(JsonNode data) {
        this.type = data.has("type") ? ComponentType.devalue(data.get("type").asInt()) : null;
    }

    public ComponentImpl(ComponentType type) {
        this.type = type;
    }

    @Override
    public ComponentType getType() {
        return type;
    }
}
