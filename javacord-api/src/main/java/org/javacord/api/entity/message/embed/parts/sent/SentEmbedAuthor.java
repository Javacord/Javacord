package org.javacord.api.entity.message.embed.parts.sent;

import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.message.embed.parts.EmbedAuthor;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftAuthor;

import java.net.URL;
import java.util.Optional;

/**
 * This interface represents an embed author.
 */
public interface SentEmbedAuthor extends Nameable, EmbedAuthor {

    /**
     * Gets the url of the author.
     *
     * @return The url of the author.
     */
    Optional<URL> getUrl();

    /**
     * Gets the url of the author icon.
     *
     * @return The url of the author icon.
     */
    Optional<URL> getIconUrl();

    /**
     * Gets the proxy url of the author icon.
     *
     * @return The proxy url of the author icon.
     */
    Optional<URL> getProxyIconUrl();

    /**
     * Creates a draft instance according to this sent version.
     *
     * @return The new draft instance.
     */
    EmbedDraftAuthor toDraft();
}
