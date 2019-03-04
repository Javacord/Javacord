package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedFooter;

/**
 * Draft representation of an embed's footer.
 */
public interface EmbedDraftFooter extends BaseEmbedFooter {

    @Override
    EmbedDraft getEmbed();

    /**
     * Sets the text of the footer.
     *
     * @param text The text to set.
     * @return This instance of the footer.
     */
    EmbedDraftFooter setText(String text);

    /**
     * Sets the icon URL of the footer.
     *
     * @param url The URL to set.
     * @return This instance of the footer.
     */
    EmbedDraftFooter setIconUrl(String url);

}
