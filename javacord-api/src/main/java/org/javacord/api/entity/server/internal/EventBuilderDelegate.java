package org.javacord.api.entity.server.internal;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.server.PrivacyLevel;
import org.javacord.api.entity.server.ScheduledEventType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public interface EventBuilderDelegate {
    void setChannel(long channelId);

    void setChannel(Channel channel);

    void setName(String name);

    void setPrivacyLevel(PrivacyLevel privacyLevel);

    void setStartTime(Instant startTime);

    void setEndTime(Instant endTime);

    void setDescription(String description);

    void setType(ScheduledEventType eventType);

    void setLocation(String location);

    /**
     * Sets the server's image.
     * This method assumes the file type is "png"!
     *
     * @param image The image of the server.
     */
    void setImage(BufferedImage image);

    /**
     * Sets the server's image.
     *
     * @param image The image of the server.
     * @param fileType The type of the image, e.g. "png" or "jpg".
     */
    void setImage(BufferedImage image, String fileType);

    /**
     * Sets the server's image.
     *
     * @param image The image of the server.
     */
    void setImage(File image);

    /**
     * Sets the server's image.
     *
     * @param image The image of the server.
     */
    void setImage(URL image);

    /**
     * Sets the server's image.
     * This method assumes the file type is "png"!
     *
     * @param image The image of the server.
     */
    void setImage(byte[] image);

    /**
     * Sets the server's image.
     *
     * @param image The image of the server.
     * @param fileType The type of the image, e.g. "png" or "jpg".
     */
    void setImage(byte[] image, String fileType);

    /**
     * Sets the server's image.
     * This method assumes the file type is "png"!
     *
     * @param image The image of the server.
     */
    void setImage(InputStream image);

    /**
     * Sets the server's image.
     *
     * @param image The image of the server.
     * @param fileType The type of the image, e.g. "png" or "jpg".
     */
    void setImage(InputStream image, String fileType);

    CompletableFuture<Long> create();
}
