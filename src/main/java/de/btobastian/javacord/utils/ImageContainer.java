package de.btobastian.javacord.utils;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.utils.io.FileUtils;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * A helper class which contains a image which can be in different formats.
 */
public class ImageContainer {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ImageContainer.class);

    /**
     * The image as buffered image
     */
    private final BufferedImage imageAsBufferedImage;

    /**
     * The image as file.
     */
    private final File imageAsFile;

    /**
     * The image as icon.
     */
    private final Icon imageAsIcon;

    /**
     * The image as url.
     */
    private final URL imageAsUrl;

    /**
     * The image as byte array.
     */
    private final byte[] imageAsByteArray;

    /**
     * The image as input stream.
     */
    private final InputStream imageAsInputStream;

    /**
     * The type of the image, e.g. "png" or "gif".
     */
    private final String imageType;

    /**
     * Creates a new image container with a buffered image.
     *
     * @param image The image as a buffered image.
     * @param type The type of the image, e.g. "png" or "gif".
     */
    public ImageContainer(BufferedImage image, String type) {
        imageAsBufferedImage = image;
        imageAsFile = null;
        imageAsIcon = null;
        imageAsUrl = null;
        imageAsByteArray = null;
        imageAsInputStream = null;
        imageType = type;
    }

    /**
     * Creates a new image container with a file.
     *
     * @param image The image as a file.
     */
    public ImageContainer(File image) {
        imageAsBufferedImage = null;
        imageAsFile = image;
        imageAsIcon = null;
        imageAsUrl = null;
        imageAsByteArray = null;
        imageAsInputStream = null;
        imageType = FileUtils.getExtension(image);
    }

    /**
     * Creates a new image container with an icon.
     *
     * @param image The image a an icon.
     */
    public ImageContainer(Icon image) {
        imageAsBufferedImage = null;
        imageAsFile = null;
        imageAsIcon = image;
        imageAsUrl = null;
        imageAsByteArray = null;
        imageAsInputStream = null;
        imageType = FileUtils.getExtension(image.getUrl().getFile());
    }

    /**
     * Creates a new image container with an url.
     *
     * @param image The image as an url.
     */
    public ImageContainer(URL image) {
        imageAsBufferedImage = null;
        imageAsFile = null;
        imageAsIcon = null;
        imageAsUrl = image;
        imageAsByteArray = null;
        imageAsInputStream = null;
        imageType = FileUtils.getExtension(image.getFile());
    }

    /**
     * Creates a new image container with an url.
     *
     * @param image The image as a byte array.
     * @param type The type of the image, e.g. "png" or "gif".
     */
    public ImageContainer(byte[] image, String type) {
        imageAsBufferedImage = null;
        imageAsFile = null;
        imageAsIcon = null;
        imageAsUrl = null;
        imageAsByteArray = null;
        imageAsInputStream = null;
        imageType = type;
    }

    /**
     * Creates a new image container with an input stream.
     *
     * @param image The image as an input stream.
     * @param type The type of the image, e.g. "png" or "gif".
     */
    public ImageContainer(InputStream image, String type) {
        imageAsBufferedImage = null;
        imageAsFile = null;
        imageAsIcon = null;
        imageAsUrl = null;
        imageAsByteArray = null;
        imageAsInputStream = image;
        imageType = type;
    }

    /**
     * Gets the type of the image, e.g. "png" or "gif".
     *
     * @return The type of the image.
     */
    public String getImageType() {
        return imageType;
    }

    /**
     * Gets the byte array for the image.
     *
     * @param api The discord api instance.
     * @return The byte array stream for the image.
     */
    public CompletableFuture<byte[]> asByteArray(DiscordApi api) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        try {
            if (imageAsByteArray != null) {
                future.complete(imageAsByteArray);
            }
            if (imageAsBufferedImage != null ||
                    imageAsFile != null ||
                    imageAsIcon != null ||
                    imageAsUrl != null ||
                    imageAsInputStream != null) {
                asInputStream(api).thenApply(stream -> {
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
                }).whenComplete((bytes, throwable) -> {
                    if (throwable != null) {
                        future.completeExceptionally(throwable);
                    } else {
                        future.complete(bytes);
                    }
                });
            }
        } catch (Throwable t) {
            future.completeExceptionally(t);
        }
        return future;
    }

    /**
     * Gets the input stream for the image.
     *
     * @param api The discord api instance.
     * @return The input stream for the image.
     */
    public CompletableFuture<InputStream> asInputStream(DiscordApi api) {
        CompletableFuture<InputStream> future = new CompletableFuture<>();
        try {
            if (imageAsBufferedImage != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(imageAsBufferedImage, imageType, os);
                future.complete(new ByteArrayInputStream(os.toByteArray()));
            }
            if (imageAsFile != null) {
                future.complete(new FileInputStream(imageAsFile));
            }
            if (imageAsIcon != null || imageAsUrl != null) {
                URL url = imageAsUrl == null ? imageAsIcon.getUrl() : imageAsUrl;
                api.getThreadPool().getExecutorService().submit(() -> {
                    try {
                        logger.debug("Trying to download image from {}", url);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);
                        future.complete(conn.getInputStream());
                        logger.debug("Downloaded image from {} (content length: {})", url, conn.getContentLength());
                    } catch (Throwable t) {
                        future.completeExceptionally(t);
                    }
                });
            }
            if (imageAsByteArray != null) {
                future.complete(new ByteArrayInputStream(imageAsByteArray));
            }
            if (imageAsInputStream != null) {
                future.complete(imageAsInputStream);
            }
        } catch (Throwable t) {
            future.completeExceptionally(t);
        }
        return future;
    }

    /**
     * Gets the image as {@link BufferedImage}.
     *
     * @param api The discord api instance.
     * @return The image as BufferedImage.
     */
    public CompletableFuture<BufferedImage> asBufferedImage(DiscordApi api) {
        return asByteArray(api)
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
