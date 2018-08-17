package org.javacord.api.entity.message.embed.parts.sent;

import org.javacord.api.entity.message.embed.parts.EmbedFooter;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftFooter;

import java.net.URL;
import java.util.Optional;

/**
 * This interface represents an embed footer.
 */
public interface SentEmbedFooter extends EmbedFooter {

    /**
     * Gets the footer text.
     *
     * @return The text of the footer.
     */
    Optional<String> getText();

    /**
     * Gets the url of the footer icon.
     *
     * @return The url of the footer icon.
     */
    Optional<URL> getIconUrl();

    /**
     * Gets the proxy url of the footer icon.
     *
     * @return The proxy url of the footer icon.
     */
    Optional<URL> getProxyIconUrl();

    /**
     * Creates a draft instance according to this sent version.
     *
     * @return The new draft instance.
     */
    EmbedDraftFooter toDraft();
}