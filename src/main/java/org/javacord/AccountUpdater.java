package org.javacord;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.Icon;
import org.javacord.util.FileContainer;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.javacord.entity.Icon;
import org.javacord.util.FileContainer;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the connected account (e.g. username or avatar).
 */
public class AccountUpdater {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The username to update.
     */
    private String username = null;

    /**
     * The avatar to update.
     */
    private FileContainer avatar = null;

    /**
     * Creates a new account updater.
     *
     * @param api The discord api instance.
     */
    public AccountUpdater(DiscordApi api) {
        this.api = api;
    }

    /**
     * Queues the username of the connected account to get updated.
     *
     * @param username The username to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setUsername(String username) {
        this.username = username;
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(File avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(Icon avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        return this;
    }

    /**
     * Queues the avatar of the connected account to get updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public AccountUpdater setAvatar(URL avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
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
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        boolean patchAccount = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (username != null) {
            body.put("username", username);
            patchAccount = true;
        }
        if (avatar != null) {
            patchAccount = true;
        }
        if (patchAccount) {
            if (avatar != null) {
                return avatar.asByteArray(api).thenAccept(bytes -> {
                    String base64Avatar = "data:image/" + avatar.getFileType() + ";base64," +
                            Base64.getEncoder().encodeToString(bytes);
                    body.put("avatar", base64Avatar);
                }).thenCompose(aVoid -> new RestRequest<Void>(api, RestMethod.PATCH, RestEndpoint.CURRENT_USER)
                        .setRatelimitRetries(0)
                        .setBody(body)
                        .execute(result -> null));
            }
            return new RestRequest<Void>(api, RestMethod.PATCH, RestEndpoint.CURRENT_USER)
                    .setRatelimitRetries(0)
                    .setBody(body)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}
