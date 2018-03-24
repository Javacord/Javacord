package org.javacord.api.internal;

import org.javacord.api.AccountUpdater;
import org.javacord.api.entity.Icon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link AccountUpdater} to update the connected account.
 * You usually don't want to interact with this object.
 */
public interface AccountUpdaterDelegate {

    /**
     * Queues the username of the connected account to get updated.
     *
     * @param username The username to set.
     */
    void setUsername(String username);

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(BufferedImage avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(BufferedImage avatar, String fileType);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(File avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(Icon avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(URL avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(byte[] avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(byte[] avatar, String fileType);

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     */
    void setAvatar(InputStream avatar);

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     */
    void setAvatar(InputStream avatar, String fileType);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
