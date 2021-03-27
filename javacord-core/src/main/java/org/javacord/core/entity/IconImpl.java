package org.javacord.core.entity;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.core.util.FileContainer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link Icon}.
 */
public class IconImpl implements Icon {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The url of the icon.
     */
    private final URL url;

    /**
     * Creates a new icon object.
     *
     * @param api The discord api instance.
     * @param url The url of the icon.
     */
    public IconImpl(DiscordApi api, URL url) {
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
    public InputStream asInputStream() throws IOException {
        return new FileContainer(getUrl()).asInputStream(getApi());
    }

    @Override
    public CompletableFuture<BufferedImage> asBufferedImage() {
        return new FileContainer(getUrl()).asBufferedImage((this).getApi());
    }
}
