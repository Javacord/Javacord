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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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
        setFileTypeOrName(type);
    }

    /**
     * Creates a new file container with a file.
     *
     * @param file The file as a file.
     */
    public FileContainer(File file) {
        this(file, false);
    }

    /**
     * Creates a new file container with a file.
     *
     * @param file The file as a file.
     * @param isSpoiler Whether the file is to be marked as spoiler.
     */
    public FileContainer(File file, boolean isSpoiler) {
        fileAsBufferedImage = null;
        fileAsFile = file;
        fileAsIcon = null;
        fileAsUrl = null;
        fileAsByteArray = null;
        fileAsInputStream = null;
        fileTypeOrName = (isSpoiler ? "SPOILER_" : "") + file.getName();
    }

    /**
     * Creates a new file container with an icon.
     *
     * @param file The file as an icon.
     */
    public FileContainer(Icon file) {
        this(file, false);
    }

    /**
     * Creates a new file container with an icon.
     *
     * @param file The file as an icon.
     * @param isSpoiler Whether the icon is marked as a spoiler.
     */
    public FileContainer(Icon file, boolean isSpoiler) {
        fileAsBufferedImage = null;
        fileAsFile = null;
        fileAsIcon = file;
        fileAsUrl = null;
        fileAsByteArray = null;
        fileAsInputStream = null;
        fileTypeOrName = (isSpoiler ? "SPOILER_" : "") + file.getUrl().getFile();
    }

    /**
     * Creates a new file container with an url.
     *
     * @param file The file as an url.
     */
    public FileContainer(URL file) {
        this(file, false);
    }

    /**
     * Creates a new file container with an url.
     *
     * @param file The file as an url.
     * @param isSpoiler Whether the file is to be marked as spoiler.
     */
    public FileContainer(URL file, boolean isSpoiler) {
        fileAsBufferedImage = null;
        fileAsFile = null;
        fileAsIcon = null;
        fileAsUrl = file;
        fileAsByteArray = null;
        fileAsInputStream = null;
        fileTypeOrName = (isSpoiler ? "SPOILER_" : "") + new File(file.getFile()).getName();
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
        if ((fileAsBufferedImage != null) && !ImageIO.getImageWritersByFormatName(getFileType()).hasNext()) {
            throw new IllegalArgumentException(String.format("No image writer found for format \"%s\"", getFileType()));
        }
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
                api.getThreadPool().getExecutorService().submit(() -> {
                    try (
                            InputStream in = new BufferedInputStream(asInputStream(api));
                            ByteArrayOutputStream out = new ByteArrayOutputStream()
                    ) {
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = in.read(buf))) {
                            out.write(buf, 0, n);
                        }
                        future.complete(out.toByteArray());
                    } catch (Throwable t) {
                        future.completeExceptionally(t);
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
     * @throws IOException If an IO error occurs.
     */
    public InputStream asInputStream(DiscordApi api) throws IOException {
        if (fileAsBufferedImage != null) {
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos);
            api.getThreadPool().getExecutorService().submit(() -> {
                try {
                    ImageIO.write(fileAsBufferedImage, getFileType(), pos);
                    pos.close();
                } catch (Throwable t) {
                    logger.error("Failed to process buffered image file!", t);
                }
            });
            return pis;
        }
        if (fileAsFile != null) {
            return new FileInputStream(fileAsFile);
        }
        if (fileAsIcon != null || fileAsUrl != null) {
            URL url = fileAsUrl == null ? fileAsIcon.getUrl() : fileAsUrl;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);
            return conn.getInputStream();
        }
        if (fileAsByteArray != null) {
            return new ByteArrayInputStream(fileAsByteArray);
        }
        if (fileAsInputStream != null) {
            return fileAsInputStream;
        }
        throw new IllegalStateException("No file variant is set");
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
