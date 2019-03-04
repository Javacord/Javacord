package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedField;
import org.javacord.core.entity.message.embed.EmbedFieldImpl;
import org.javacord.core.entity.message.embed.draft.EmbedDraftFieldImpl;

/**
 * The implementation of {@link SentEmbedField}.
 */
public class SentEmbedFieldImpl extends EmbedFieldImpl implements SentEmbedField {
    private final SentEmbed parent;

    /**
     * Constructor.
     *
     * @param data The json data of the field.
     */
    public SentEmbedFieldImpl(SentEmbed parent, JsonNode data) {
        super(data.path("name").asText(null),
                data.path("value").asText(null),
                data.path("inline").asBoolean(false));

        this.parent = parent;
    }

    @Override
    public EmbedDraftField toEmbedDraftField() {
        return new EmbedDraftFieldImpl(parent, this);
    }

    @Override
    public SentEmbed getEmbed() {
        return parent;
    }
}
