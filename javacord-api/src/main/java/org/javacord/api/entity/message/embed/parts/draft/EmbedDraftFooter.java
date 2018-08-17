package org.javacord.api.entity.message.embed.parts.draft;

import org.javacord.api.entity.message.embed.parts.EmbedFooter;

import java.net.URL;
import java.util.Optional;

/**
 * This class represents the footer for an embed draft.
 */
public interface EmbedDraftFooter extends EmbedFooter {

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
}