package org.javacord.api.entity.message.embed.parts.sent;

import org.javacord.api.entity.Nameable;

import java.net.URL;

/**
 * This interface represents an embed provider.
 */
public interface SentEmbedProvider extends Nameable {
    /**
     * Gets the URL of an the provider.
     *
     * @return The URL of the provider.
     */
    URL getUrl();
}
