package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.sent.SentEmbedField;

public interface EmbedDraftField extends BaseEmbedField, EmbedDraftMember<EmbedDraftField, SentEmbedField> {
    EmbedDraftField setName(String name);

    EmbedDraftField setValue(String value);

    EmbedDraftField setInline(boolean inline);
}
