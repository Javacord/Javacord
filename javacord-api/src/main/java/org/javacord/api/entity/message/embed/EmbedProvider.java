package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.Nameable;

import java.net.URL;

/**
 * This interface represents an embed provider.
 */
public interface EmbedProvider extends Nameable {

    /**
     * Gets the url of the provider.
     *
     * @return The url of the provider.
     */
    URL getUrl();

}
