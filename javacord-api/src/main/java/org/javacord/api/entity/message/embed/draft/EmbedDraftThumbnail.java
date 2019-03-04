package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;

/**
 * Draft representation of an embed's thumbnail.
 */
public interface EmbedDraftThumbnail extends BaseEmbedThumbnail {

    @Override
    EmbedDraft getEmbed();

    /**
     * Sets the URL of the thumbnail image.
     *
     * @param url The URL to set.
     * @return This instance of the thumbnail.
     */
    EmbedDraftThumbnail setUrl(String url);

}
