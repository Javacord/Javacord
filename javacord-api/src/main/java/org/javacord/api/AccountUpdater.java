package org.javacord.api;

import org.javacord.api.entity.Icon;
import org.javacord.api.internal.AccountUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the connected account (e.g. username or avatar).
 */
public class AccountUpdater {

    /**
     * The account delegate used by this instance.
     */
    private final AccountUpdaterDelegate delegate;

    /**
     * Creates a new account updater.
     *
     * @param api The discord api instance.
     */
    public AccountUpdater(DiscordApi api) {
        delegate = DelegateFactory.createAccountUpdaterDelegate(api);
    }

    /**
     * Queues the username of the connected account to get updated.
     *
     * @param username The username to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setUsername(String username) {
        delegate.setUsername(username);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(BufferedImage avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(BufferedImage avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(File avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(Icon avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(URL avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(byte[] avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(byte[] avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(InputStream avatar) {
        delegate.setAvatar(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(InputStream avatar, String fileType) {
        delegate.setAvatar(avatar, fileType);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
