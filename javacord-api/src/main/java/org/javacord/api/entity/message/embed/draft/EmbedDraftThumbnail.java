package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;

public interface EmbedDraftThumbnail extends BaseEmbedThumbnail,
        EmbedDraftMember<EmbedDraftThumbnail, SentEmbedThumbnail> {

    EmbedDraftThumbnail setUrl(String url);

}
