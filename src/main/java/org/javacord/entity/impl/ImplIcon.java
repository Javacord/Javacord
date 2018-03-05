package org.javacord.entity.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.Icon;
import org.javacord.util.FileContainer;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

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

    @Override
    public CompletableFuture<byte[]> asByteArray() {
        return new FileContainer(getUrl()).asByteArray((this).getApi());
    }

    @Override
    public CompletableFuture<InputStream> asInputStream() {
        return new FileContainer(getUrl()).asInputStream((this).getApi());
    }

    @Override
    public CompletableFuture<BufferedImage> asBufferedImage() {
        return new FileContainer(getUrl()).asBufferedImage((this).getApi());
    }
}
