package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;

public interface EmbedDraftAuthor extends BaseEmbedAuthor, EmbedDraftMember<EmbedDraftAuthor, SentEmbedAuthor> {
    EmbedDraftAuthor setUrl(String url);

    EmbedDraftAuthor setIconUrl(String url);
}
