package org.javacord.api.entity.message.embed.parts.sent;

import org.javacord.api.entity.message.embed.parts.EmbedThumbnail;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftThumbnail;

import java.net.URL;

/**
 * This interface represents an embed thumbnail.
 */
public interface SentEmbedThumbnail extends EmbedThumbnail {

    /**
     * Gets the proxy url of the thumbnail.
     *
     * @return The proxy url of the thumbnail.
     */
    URL getProxyUrl();

    /**
     * Gets the height of the thumbnail.
     *
     * @return The height of the thumbnail.
     */
    int getHeight();

    /**
     * Gets the width of the thumbnail.
     *
     * @return The width of the thumbnail.
     */
    int getWidth();

    /**
     * Creates a draft instance according to this sent version.
     *
     * @return The new draft instance.
     */
    EmbedDraftThumbnail toDraft();
}