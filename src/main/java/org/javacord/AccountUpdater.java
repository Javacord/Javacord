package org.javacord;

import org.javacord.entity.Icon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This interface can be used to update the connected account (e.g. username or avatar).
 */
public interface AccountUpdater {

    /**
     * Queues the username of the connected account to get updated.
     *
     * @param username The username to set.
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setUsername(String username);

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(BufferedImage avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(BufferedImage avatar, String fileType);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(File avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(Icon avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(URL avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(byte[] avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(byte[] avatar, String fileType);

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(InputStream avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    AccountUpdater setAvatar(InputStream avatar, String fileType);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
