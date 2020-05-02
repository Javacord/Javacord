package org.javacord.api.entity;

import java.net.URL;

public interface VanityUrlCode {

    /**
     * Gets the vanity code.
     *
     * @return The vanity code.
     */
    String getCode();

    /**
     * Gets the vanity url.
     *
     * @return The URL of the vanity code.
     */
    URL getUrl();

}
