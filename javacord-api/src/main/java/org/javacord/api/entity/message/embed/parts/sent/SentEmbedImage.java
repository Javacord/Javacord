package org.javacord.api.entity.message.embed.parts.sent;

import org.javacord.api.entity.message.embed.parts.EmbedImage;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftImage;

import java.net.URL;

/**
 * This interface represents an embed image.
 */
public interface SentEmbedImage extends EmbedImage {

    /**
     * Gets the proxy url of the image.
     *
     * @return The proxy url of the image.
     */
    URL getProxyUrl();

    /**
     * Gets the height of the image.
     *
     * @return The height of the image.
     */
    int getHeight();

    /**
     * Gets the width of the image.
     *
     * @return The width of the image.
     */
    int getWidth();

    /**
     * Creates a draft instance according to this sent version.
     *
     * @return The new draft instance.
     */
    EmbedDraftImage toDraft();
}