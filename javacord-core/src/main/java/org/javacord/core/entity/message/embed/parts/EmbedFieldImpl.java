package org.javacord.core.entity.message.embed.parts;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.parts.EditableEmbedField;
import org.javacord.api.entity.message.embed.parts.EmbedField;

/**
 * The implementation for embed fields.
 */
public class EmbedFieldImpl implements EmbedField {

    String name;
    String value;
    boolean inline;

    /**
     * Creates a new instance from a given set of information.
     *
     * @param name   The name of the field.
     * @param value  The value of the field.
     * @param inline Whether the field should be inline.
     */
    public EmbedFieldImpl(
            String name,
            String value,
            boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    /**
     * Creates a new instance from a JsonNode.
     * This is usually only used when converting from a message node.
     *
     * @param data The JsonNode of this field.
     */
    public EmbedFieldImpl(JsonNode data) {
        this.name = data.has("name") ? data.get("name").asText() : null;
        this.value = data.has("value") ? data.get("value").asText() : null;
        this.inline = data.has("inline") && data.get("inline").asBoolean();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInline() {
        return inline;
    }

    @Override
    public EditableEmbedField asEditableEmbedField() {
        return new EditableEmbedFieldImpl(this);
    }
}
