package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.sent.SentEmbedImage;

public interface EmbedDraftImage extends BaseEmbedImage, EmbedDraftMember<EmbedDraftImage, SentEmbedImage> {
    EmbedDraftImage setUrl(String url);
}
