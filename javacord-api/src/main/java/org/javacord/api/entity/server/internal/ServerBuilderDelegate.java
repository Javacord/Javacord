package org.javacord.api.entity.server.internal;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.ServerBuilder;
import org.javacord.api.entity.server.VerificationLevel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerBuilder} to create servers.
 * You usually don't want to interact with this object.
 */
public interface ServerBuilderDelegate {

    /**
     * Sets the server's name.
     *
     * @param name The name of the server.
     */
    void setName(String name);

    /**
     * Sets the server's region.
     *
     * @param region The region of the server.
     */
    void setRegion(Region region);

    /**
     * Sets the server's explicit content filter level.
     *
     * @param explicitContentFilterLevel The explicit content filter level of the server.
     */
    void setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel);

    /**
     * Sets the server's verification level.
     *
     * @param verificationLevel The verification level of the server.
     */
    void setVerificationLevel(VerificationLevel verificationLevel);

    /**
     * Sets the server's default message notification level.
     *
     * @param defaultMessageNotificationLevel The default message notification level of the server.
     */
    void setDefaultMessageNotificationLevel(DefaultMessageNotificationLevel defaultMessageNotificationLevel);

    /**
     * Sets the server's afk timeout in seconds.
     *
     * @param afkTimeout The afk timeout in seconds of the server.
     */
    void setAfkTimeoutInSeconds(int afkTimeout);

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     */
    void setIcon(BufferedImage icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(BufferedImage icon, String fileType);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     */
    void setIcon(File icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     */
    void setIcon(Icon icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     */
    void setIcon(URL icon);

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     */
    void setIcon(byte[] icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(byte[] icon, String fileType);

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     */
    void setIcon(InputStream icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(InputStream icon, String fileType);

    /**
     * Creates the server.
     *
     * @return The id of the server.
     */
    CompletableFuture<Long> create();

}
