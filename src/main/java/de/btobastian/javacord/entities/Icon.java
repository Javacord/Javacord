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
     * Gets the icon as byte array.
     *
     * @return The icon as byte array.
     */
    default CompletableFuture<byte[]> asByteArray() {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        ((ImplIcon) this).getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                logger.debug("Trying to get icon for entity {}", this);
                HttpsURLConnection conn = (HttpsURLConnection) getUrl().openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);
                try (
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        ByteArrayOutputStream out = new ByteArrayOutputStream()
                ) {
                    byte[] buf = new byte[1024];
                    int n;
                    while (-1 != (n = in.read(buf))) {
                        out.write(buf, 0, n);
                    }
                    byte[] icon = out.toByteArray();
                    logger.debug("Got icon for entity {} (size: {})", this, icon.length);
                    future.complete(icon);
                }
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
                        logger.error("Failed to convert byte array to buffered image", e);
                        return null;
                    }
                });
    }

}
