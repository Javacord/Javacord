package org.javacord.entity.message;

import org.javacord.Javacord;
import org.javacord.entity.DiscordEntity;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.entity.DiscordEntity;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a message attachment.
 */
public interface MessageAttachment extends DiscordEntity {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(MessageAttachment.class);

    /**
     * Gets the message of the attachment.
     *
     * @return The message of the attachment.
     */
    Message getMessage();

    /**
     * Gets the file name of the attachment.
     *
     * @return The file name of the attachment.
     */
    String getFileName();

    /**
     * Gets the size of the attachment in bytes.
     *
     * @return The size of the attachment in bytes.
     */
    int getSize();

    /**
     * Gets the url of the attachment.
     *
     * @return The url of the attachment.
     */
    URL getUrl();

    /**
     * Gets the proxy url of the attachment.
     *
     * @return The proxy url of the attachment.
     */
    URL getProxyUrl();

    /**
     * Checks if the attachment is an image.
     *
     * @return Whether the attachment is an image or not.
     */
    default boolean isImage() {
        return getHeight().isPresent();
    }

    /**
     * Gets the height of the attachment, if it's an image.
     *
     * @return The height of the attachment.
     */
    Optional<Integer> getHeight();

    /**
     * Gets the width of the attachment, if it's an image.
     *
     * @return The width of the attachment.
     */
    Optional<Integer> getWidth();

    /**
     * Gets the attachment as byte array.
     *
     * @return The attachment as byte array.
     */
    default CompletableFuture<byte[]> downloadAsByteArray() {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                logger.debug("Trying to download attachment {}", this);
                URL url = getUrl();
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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
                    byte[] attachment = out.toByteArray();
                    logger.debug("Got attachment {} (size: {})", this, attachment.length);
                    future.complete(attachment);
                }
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Downloads the attachment as image.
     *
     * @return The attachment as image. Only present, if the attachment is an image.
     * @throws IllegalStateException If the attachment is not an image.
     */
    default CompletableFuture<BufferedImage> downloadAsImage() {
        if (!isImage()) {
            throw new IllegalStateException("The attachment is not an image!");
        }
        return downloadAsByteArray().thenApply(bytes -> {
            try {
                return ImageIO.read(new ByteArrayInputStream(bytes));
            } catch (IOException e) {
                logger.warn("Error while converting byte array to BufferedImage!", e);
                return null;
            }
        });
    }

}
