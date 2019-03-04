package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.BaseEmbedField;

public interface SentEmbedField extends BaseEmbedField {

    @Override
    SentEmbed getEmbed();

}
