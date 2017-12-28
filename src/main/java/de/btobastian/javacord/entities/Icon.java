package de.btobastian.javacord.entities;

import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.impl.ImplIcon;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * This class represents a discord icon, for example a server icon or a user avatar.
 */
public interface Icon {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(Icon.class);

    /**
     * Gets the url of the icon.
     *
     * @return The url of the icon.
     */
    URL getUrl();

    /**
     * Checks if the icon is animated.
     *
     * @return Whether the icon is animated or not.
     */
    default boolean isAnimated() {
        return getUrl().getFile().endsWith(".gif");
    }

    /**
     * Gets the icon as byte array.
     *
     * @return The icon as byte array.
     */
    default CompletableFuture<byte[]> asByteArray() {
        return asInputStream().thenApply(stream -> {
            try (
                    InputStream in = new BufferedInputStream(stream);
                    ByteArrayOutputStream out = new ByteArrayOutputStream()
            ) {
                byte[] buf = new byte[1024];
                int n;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                return out.toByteArray();
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Gets the input stream for the icon.
     * This can be used for {@link de.btobastian.javacord.entities.message.Messageable#sendMessage(InputStream, String)}
     *
     * @return The input stream for the icon.
     */
    default CompletableFuture<InputStream> asInputStream() {
        CompletableFuture<InputStream> future = new CompletableFuture<>();
        ((ImplIcon) this).getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                logger.debug("Trying to download icon from {}", getUrl());
                HttpsURLConnection conn = (HttpsURLConnection) getUrl().openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);
                future.complete(conn.getInputStream());
                logger.debug("Downloaded icon from {} (content length: {})", getUrl(), conn.getContentLength());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Gets the icon as {@link BufferedImage}.
     *
     * @return The icon as BufferedImage.
     */
    default CompletableFuture<BufferedImage> asBufferedImage() {
        return asByteArray()
                .thenApply(ByteArrayInputStream::new)
                .thenApply(stream -> {
                    try {
                        return ImageIO.read(stream);
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

}
