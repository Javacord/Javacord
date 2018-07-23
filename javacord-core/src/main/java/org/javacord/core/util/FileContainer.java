package org.javacord.core.util;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.Icon;
import org.javacord.core.util.io.FileUtils;
import org.javacord.core.util.logging.LoggerUtil;

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
 * A helper class which contains a file which can be in different formats.
 */
public class FileContainer {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(FileContainer.class);

    /**
     * The file as buffered image.
     */
    private final BufferedImage fileAsBufferedImage;

    /**
     * The file as file.
     */
    private final File fileAsFile;

    /**
     * The file as icon.
     */
    private final Icon fileAsIcon;

    /**
     * The file as url.
     */
    private final URL fileAsUrl;

    /**
     * The file as byte array.
     */
    private final byte[] fileAsByteArray;

    /**
     * The file as input stream.
     */
    private final InputStream fileAsInputStream;

    /**
     * The type ("png", "txt", ...) or name ("image.png", "readme.txt", ...) of the file.
     */
    private String fileTypeOrName;

    /**
     * Creates a new file container with a buffered image.
     *
     * @param file The file as a buffered image.
     * @param type The type ("png", "txt", ...) or name ("image.png", "readme.txt", ...) of the file.
     */
    public FileContainer(BufferedImage file, String type) {
        fileAsBufferedImage = file;
        fileAsFile = null;
        fileAsIcon = null;
        fileAsUrl = null;
        fileAsByteArray = null;
        fileAsInputStream = null;
        fileTypeOrName = type;
    }

    /**
     * Creates a new file container with a file.
     *
     * @param file The file as a file.
     */
    public FileContainer(File file) {
        fileAsBufferedImage = null;
        fileAsFile = file;
        fileAsIcon = null;
        fileAsUrl = null;
        fileAsByteArray = null;
        fileAsInputStream = null;
        fileTypeOrName = file.getName();
    }

    /**
     * Creates a new file container with an icon.
     *
     * @param file The file a an icon.
     */
    public FileContainer(Icon file) {
        fileAsBufferedImage = null;
        fileAsFile = null;
        fileAsIcon = file;
        fileAsUrl = null;
        fileAsByteArray = null;
        fileAsInputStream = null;
        fileTypeOrName = file.getUrl().getFile();
    }

    /**
     * Creates a new file container with an url.
     *
     * @param file The file as an url.
     */
    public FileContainer(URL file) {
        fileAsBufferedImage = null;
        fileAsFile = null;
        fileAsIcon = null;
        fileAsUrl = file;
        fileAsByteArray = null;
        fileAsInputStream = null;
        fileTypeOrName = file.getFile();
    }

    /**
     * Creates a new file container with an url.
     *
     * @param file The file as a byte array.
     * @param type The type ("png", "txt", ...) or name ("image.png", "readme.txt", ...) of the file.
     */
    public FileContainer(byte[] file, String type) {
        fileAsBufferedImage = null;
        fileAsFile = null;
        fileAsIcon = null;
        fileAsUrl = null;
        fileAsByteArray = file;
        fileAsInputStream = null;
        fileTypeOrName = type;
    }

    /**
     * Creates a new file container with an input stream.
     *
     * @param file The file as an input stream.
     * @param type The type ("png", "txt", ...) or name ("image.png", "readme.txt", ...) of the file.
     */
    public FileContainer(InputStream file, String type) {
        fileAsBufferedImage = null;
        fileAsFile = null;
        fileAsIcon = null;
        fileAsUrl = null;
        fileAsByteArray = null;
        fileAsInputStream = file;
        fileTypeOrName = type;
    }

    /**
     * Sets the type ("png", "txt", ...) or name ("image.png", "readme.txt", ...) of the file.
     *
     * @param type The type or name of the file.
     */
    public void setFileTypeOrName(String type) {
        fileTypeOrName = type;
    }

    /**
     * Gets the type ("png", "txt", ...) of the file.
     *
     * @return The type of the file.
     */
    public String getFileType() {
        if (fileTypeOrName != null && fileTypeOrName.contains(".")) {
            return FileUtils.getExtension(fileTypeOrName);
        } else {
            return fileTypeOrName;
        }
    }

    /**
     * Gets the type ("png", "txt", ...) or name ("image.png", "readme.txt", ...) of the file.
     *
     * @return The type or name of the file.
     */
    public String getFileTypeOrName() {
        return fileTypeOrName;
    }

    /**
     * Gets the byte array for the file.
     *
     * @param api The discord api instance.
     * @return The byte array stream for the file.
     */
    public CompletableFuture<byte[]> asByteArray(DiscordApi api) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        try {
            if (fileAsByteArray != null) {
                future.complete(fileAsByteArray);
                return future;
            }
            if (fileAsBufferedImage != null
                    || fileAsFile != null
                    || fileAsIcon != null
                    || fileAsUrl != null
                    || fileAsInputStream != null) {
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
                return future;
            }
            future.completeExceptionally(new IllegalStateException("No file variant is set"));
        } catch (Throwable t) {
            future.completeExceptionally(t);
        }
        return future;
    }

    /**
     * Gets the input stream for the file.
     *
     * @param api The discord api instance.
     * @return The input stream for the file.
     */
    public CompletableFuture<InputStream> asInputStream(DiscordApi api) {
        CompletableFuture<InputStream> future = new CompletableFuture<>();
        try {
            if (fileAsBufferedImage != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(fileAsBufferedImage, fileTypeOrName, os);
                future.complete(new ByteArrayInputStream(os.toByteArray()));
                return future;
            }
            if (fileAsFile != null) {
                future.complete(new FileInputStream(fileAsFile));
                return future;
            }
            if (fileAsIcon != null || fileAsUrl != null) {
                URL url = fileAsUrl == null ? fileAsIcon.getUrl() : fileAsUrl;
                api.getThreadPool().getExecutorService().submit(() -> {
                    try {
                        logger.debug("Trying to download file from {}", url);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);
                        future.complete(conn.getInputStream());
                        logger.debug("Downloaded file from {} (content length: {})", url, conn.getContentLength());
                    } catch (Throwable t) {
                        future.completeExceptionally(t);
                    }
                });
                return future;
            }
            if (fileAsByteArray != null) {
                future.complete(new ByteArrayInputStream(fileAsByteArray));
                return future;
            }
            if (fileAsInputStream != null) {
                future.complete(fileAsInputStream);
                return future;
            }
            future.completeExceptionally(new IllegalStateException("No file variant is set"));
        } catch (Throwable t) {
            future.completeExceptionally(t);
        }
        return future;
    }

    /**
     * Gets the file as {@link BufferedImage}.
     *
     * @param api The discord api instance.
     * @return The file as BufferedImage.
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
