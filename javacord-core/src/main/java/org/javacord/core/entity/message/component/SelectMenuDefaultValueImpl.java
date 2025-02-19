package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.component.SelectMenuDefaultValue;
import org.javacord.api.entity.message.component.SelectMenuDefaultValueType;

public class SelectMenuDefaultValueImpl implements SelectMenuDefaultValue {

    private final long id;

    private final SelectMenuDefaultValueType type;

    /**
     * Creates a new select menu default value.
     *
     * @param data The json data of the select menu option.
     */
    public SelectMenuDefaultValueImpl(JsonNode data) {
        id = data.get("id").asLong();
        type = SelectMenuDefaultValueType.fromString(data.get("type").asText());
    }

    /**
     * Creates a new select menu default value.
     *
     * @param id    The id of a user, role, or channel.
     * @param type  Type of value that id represents. Either user, role, or channel.
     */
    public SelectMenuDefaultValueImpl(long id, SelectMenuDefaultValueType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public SelectMenuDefaultValueType getType() {
        return type;
    }

    /**
     * Gets the select menu default value as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The select menu default value as a ObjectNode.
     */
    public ObjectNode toJson() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();

        object.put("id", id);
        object.put("type", type.getJsonName());

        return object;
    }
}
