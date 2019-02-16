package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedField;
import org.javacord.core.entity.message.embed.EmbedFieldImpl;
import org.javacord.core.entity.message.embed.draft.EmbedDraftImpl;

/**
 * The implementation of {@link SentEmbedField}.
 */
public class SentEmbedFieldImpl extends EmbedFieldImpl implements SentEmbedField {

    private final SentEmbed parent;

    /**
     * Creates a new embed field.
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
    public Optional<SentEmbed> getEmbed() {
        return Optional.ofNullable(parent);
    }

    @Override
    public EmbedDraftField toDraftMember() {
        return EmbedDraftImpl.createFrom(parent.toEmbedDraft(), this);
    }

    @Override
    public Optional<SentEmbedField> asSentEmbedMember() {
        return Optional.of(this);
    }
}
