package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.component.Component;
import org.javacord.api.entity.message.component.ComponentType;

public abstract class ComponentImpl implements Component {
    private final ComponentType type;

    /**
     * Creates a new component.
     *
     * @param data The json data of the component.
     */
    public ComponentImpl(JsonNode data) {
        this.type = ComponentType.fromId(data.get("type").asInt());
    }

    protected ComponentImpl(ComponentType type) {
        this.type = type;
    }

    @Override
    public ComponentType getType() {
        return type;
    }

    abstract ObjectNode toJsonNode();
}
