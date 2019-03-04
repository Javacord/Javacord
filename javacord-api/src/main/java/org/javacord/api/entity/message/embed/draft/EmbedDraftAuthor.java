package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedAuthor;

/**
 * Draft representation of an embed's author.
 */
public interface EmbedDraftAuthor extends BaseEmbedAuthor {

    @Override
    EmbedDraft getEmbed();

    /**
     * Sets the name of the author.
     *
     * @param name The name to set.
     * @return This instance of the author.
     */
    EmbedDraftAuthor setName(String name);

    /**
     * Sets the URL of the author.
     *
     * @param url The URL to set.
     * @return This instance of the author.
     */
    EmbedDraftAuthor setUrl(String url);

    /**
     * Sets the icon URL of the author.
     *
     * @param url The icon URL to set.
     * @return This instance of the author.
     */
    EmbedDraftAuthor setIconUrl(String url);

}
