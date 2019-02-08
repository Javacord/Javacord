package org.javacord.api.entity.message.embed.sent;

import java.net.URL;

import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;

public interface SentEmbedThumbnail extends BaseEmbedThumbnail,
        SentEmbedImageMember<EmbedDraftThumbnail, SentEmbedThumbnail> {

    /**
     * Gets the proxy url of the image.
     *
     * @return The proxy url of the image.
     */
    String getProxyUrl();

}
