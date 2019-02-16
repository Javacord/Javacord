package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;

public interface SentEmbedImage extends BaseEmbedImage, SentEmbedImageMember<EmbedDraftImage, SentEmbedImage> {

    /**
     * Gets the proxy url of the image.
     *
     * @return The proxy url of the image.
     */
    String getProxyUrl();

}
