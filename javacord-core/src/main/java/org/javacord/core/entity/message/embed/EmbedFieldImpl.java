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

    /**
     * Creates a new embed field.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether or not this field should display inline.
     */
    public EmbedFieldImpl(String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }


    /**
     * Sets the name of the field.
     *
     * @param name The name of the field.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value of the field.
     *
     * @param value The value of the field.
     */
    void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets whether or not this field should display inline.
     *
     * @param inline Whether or not this field should display inline.
     */
    void setInline(boolean inline) {
        this.inline = inline;
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
