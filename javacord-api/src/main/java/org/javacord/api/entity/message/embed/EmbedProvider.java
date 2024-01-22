package org.javacord.api.entity.message.embed;

import java.net.URL;
import java.util.Optional;

/**
 * This interface represents an embed provider.
 */
public interface EmbedProvider {

    /**
     * Gets the name of the provider.
     *
     * @return The name of the provider.
     */
    Optional<String> getName();

    /**
     * Gets the url of the provider.
     *
     * @return The url of the provider.
     */
    Optional<URL> getUrl();

}
