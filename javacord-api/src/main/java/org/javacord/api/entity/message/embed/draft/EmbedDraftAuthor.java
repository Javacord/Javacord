package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;

import java.net.URL;

public interface EmbedDraftAuthor extends BaseEmbedAuthor, EmbedDraftMember<EmbedDraftAuthor, SentEmbedAuthor> {
    EmbedDraftAuthor setUrl(URL url);

    EmbedDraftAuthor setIconUrl(URL url);
}
