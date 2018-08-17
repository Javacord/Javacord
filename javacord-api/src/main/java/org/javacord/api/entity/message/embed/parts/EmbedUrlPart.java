package org.javacord.api.entity.message.embed.parts;

import java.net.URL;

/**
 * This class represents an embed part that contains a URL.
 */
interface EmbedUrlPart {
    /**
     * Gets the URL of an embed part.
     *
     * @return The URL.
     */
    URL getUrl();
}
