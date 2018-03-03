package org.javacord.entity.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.Icon;
import org.javacord.entity.Icon;

import java.net.URL;

/**
 * The implementation of {@link Icon}.
 */
public class ImplIcon implements Icon {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The url of the icon.
     */
    private URL url;

    /**
     * Creates a new icon object.
     *
     * @param api The discord api instance.
     * @param url The url of the icon.
     */
    public ImplIcon(DiscordApi api, URL url) {
        this.api = api;
        this.url = url;
    }

    /**
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
