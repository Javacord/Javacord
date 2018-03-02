package de.btobastian.javacord.entity.server;

import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.Region;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to create a server.
 */
public interface ServerBuilder {

    /**
     * Sets the server's name.
     *
     * @param name The name of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setName(String name);

    /**
     * Sets the server's region.
     *
     * @param region The region of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setRegion(Region region);

    /**
     * Sets the server's verification level.
     *
     * @param verificationLevel The verification level of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setVerificationLevel(VerificationLevel verificationLevel);

    /**
     * Sets the server's default message notification level.
     *
     * @param defaultMessageNotificationLevel The default message notification level of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel);

    /**
     * Sets the server's afk timeout in seconds.
     *
     * @param afkTimeout The afk timeout in seconds of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setAfkTimeoutInSeconds(int afkTimeout);

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(BufferedImage icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(BufferedImage icon, String fileType);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(File icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(Icon icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(URL icon);

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(byte[] icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(byte[] icon, String fileType);

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(InputStream icon);

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerBuilder setIcon(InputStream icon, String fileType);

    /**
     * Creates the server.
     *
     * @return The id of the server.
     */
    CompletableFuture<Long> create();

}
