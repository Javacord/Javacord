package org.javacord.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * An object that can be serialized to a JsonNode.
 */
public interface JsonNodeable {

    /**
     * Injects this object into the provided ObjectNode.
     *
     * @param frame The frame to apply this object to.
     * @return The name of the field created with this method.
     */
    String applyToNode(final ObjectNode frame);

    /**
     * Returns this object as a plain JsonONode.
     *
     * @return This object as a plain JsonNode.
     */
    default JsonNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        return node.get(applyToNode(node));
    }

}
