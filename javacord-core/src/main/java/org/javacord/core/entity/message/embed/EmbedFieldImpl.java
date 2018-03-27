package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.EmbedField;

/**
 * The implementation of {@link EmbedField}.
 */
public class EmbedFieldImpl implements EmbedField {

    private String name;
    private String value;
    private boolean inline;

    /**
     * Creates a new embed field.
     *
     * @param data The json data of the field.
     */
    public EmbedFieldImpl(JsonNode data) {
        name = data.has("name") ? data.get("name").asText() : null;
        value = data.has("value") ? data.get("value").asText() : null;
        inline = data.has("inline") && data.get("inline").asBoolean();
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
}