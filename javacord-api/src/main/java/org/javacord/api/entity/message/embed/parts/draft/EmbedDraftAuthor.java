package org.javacord.api.entity.message.embed.parts.draft;

import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.message.embed.parts.EmbedAuthor;

import java.net.URL;
import java.util.Optional;

/**
 * This class represents an embed author for an EmbedDraft.
 */
public interface EmbedDraftAuthor extends Nameable, EmbedAuthor {

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
}
