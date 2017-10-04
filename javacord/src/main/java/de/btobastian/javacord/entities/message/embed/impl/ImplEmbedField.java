package de.btobastian.javacord.entities.message.embed.impl;

import de.btobastian.javacord.entities.message.embed.EmbedField;
import org.json.JSONObject;

/**
 * The implementation of {@link EmbedField}.
 */
public class ImplEmbedField implements EmbedField {

    private String name;
    private String value;
    private boolean inline;

    /**
     * Creates a new embed field.
     *
     * @param data The json data of the field.
     */
    public ImplEmbedField(JSONObject data) {
        name = data.has("name") ? data.getString("name") : null;
        value = data.has("value") ? data.getString("value") : null;
        inline = data.has("inline") && data.getBoolean("inline");
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