package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;

public interface SentEmbedField extends BaseEmbedField {

    @Override
    SentEmbed getEmbed();

}
