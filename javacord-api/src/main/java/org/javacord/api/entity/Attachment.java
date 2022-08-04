package org.javacord.api.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an attachment.
 */
public interface Attachment extends DiscordEntity {

    /**
     * Gets the file name of the attachment.
     *
     * @return The file name of the attachment.
     */
    String getFileName();

    /**
     * Gets the description of the attachment.
     *
     * @return The description of the attachment.
     */
    String getDescription();

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
     * Gets whether this attachment is ephemeral.
     * Ephemeral attachments will automatically be removed after a set period of time and
     * attachments on messages are guaranteed to be available as long as the message itself exists.
     *
     * @return True if the attachment is ephemeral.
     */
    Optional<Boolean> isEphemeral();

    /**
     * Downloads the attachment as an input stream.
     *
     * @return The attachment as an input stream.
     * @throws IOException If an IO error occurs.
     * @deprecated Use {@link #asInputStream()} instead.
     */
    @Deprecated
    default InputStream downloadAsInputStream() throws IOException {
        return asInputStream();
    }

    /**
     * Downloads the attachment as a byte array.
     *
     * @return The attachment as a byte array.
     * @deprecated Use {@link #asByteArray()} instead.
     */
    @Deprecated
    default CompletableFuture<byte[]> downloadAsByteArray() {
        return asByteArray();
    }

    /**
     * Downloads the attachment as an image.
     *
     * @return The attachment as an image. Only present, if the attachment is an image.
     * @throws IllegalStateException If the attachment is not an image.
     * @deprecated Use {@link #asImage()} instead.
     */
    @Deprecated
    default CompletableFuture<BufferedImage> downloadAsImage() {
        return asImage();
    }

    /**
     * Downloads the attachment as an input stream.
     *
     * @return The attachment as an input stream.
     * @throws IOException If an IO error occurs.
     */
    InputStream asInputStream() throws IOException;

    /**
     * Downloads the attachment as a byte array.
     *
     * @return The attachment as a byte array.
     */
    CompletableFuture<byte[]> asByteArray();

    /**
     * Downloads the attachment as an image.
     *
     * @return The attachment as an image. Only present, if the attachment is an image.
     * @throws IllegalStateException If the attachment is not an image.
     */
    CompletableFuture<BufferedImage> asImage();

    /**
     * Checks whether the attachment is marked as a spoiler.
     *
     * <p>Discord encodes the information on whether a file is considered a spoiler in the file name. Any file whose
     * filename starts with {@code SPOILER_} is considered a spoiler.
     *
     * @return The spoiler status.
     */
    default boolean isSpoiler() {
        return getFileName().startsWith("SPOILER_");
    }

}
