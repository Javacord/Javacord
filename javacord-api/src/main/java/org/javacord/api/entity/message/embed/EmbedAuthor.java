package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.Nameable;

import java.net.URL;
import java.util.Optional;

/**
 * This interface represents an embed author.
 */
public interface EmbedAuthor extends Nameable {

    /**
     * Gets the name of the author.
     *
     * @return The name of the author.
     */
    String getName();

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

}
