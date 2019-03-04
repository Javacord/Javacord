package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedImage;

/**
 * Draft representation of an embed's image.
 */
public interface EmbedDraftImage extends BaseEmbedImage {

    @Override
    EmbedDraft getEmbed();

    /**
     * Sets the URL of the image.
     *
     * @param url The URL to set.
     * @return This instance of the image.
     */
    EmbedDraftImage setUrl(String url);

}
