package de.btobastian.javacord.entity.server;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.Region;
import de.btobastian.javacord.util.FileContainer;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to create a server.
 */
public class ServerBuilder {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The name.
     */
    private String name = null;

    /**
     * The region.
     */
    private Region region = null;

    /**
     * The verification level.
     */
    private VerificationLevel verificationLevel = null;

    /**
     * The default message notification level.
     */
    private DefaultMessageNotificationLevel defaultMessageNotificationLevel = null;

    /**
     * The afk timeout.
     */
    private Integer afkTimeout = null;

    /**
     * The icon.
     */
    private FileContainer icon = null;

    /**
     * Creates a new server creator.
     *
     * @param api The discord api instance.
     */
    public ServerBuilder(DiscordApi api) {
        this.api = api;
    }

    /**
     * Sets the server's name.
     *
     * @param name The name of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the server's region.
     *
     * @param region The region of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setRegion(Region region) {
        this.region = region;
        return this;
    }

    /**
     * Sets the server's verification level.
     *
     * @param verificationLevel The verification level of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setVerificationLevel(VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
        return this;
    }

    /**
     * Sets the server's default message notification level.
     *
     * @param defaultMessageNotificationLevel The default message notification level of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
        return this;
    }

    /**
     * Sets the server's afk timeout in seconds.
     *
     * @param afkTimeout The afk timeout in seconds of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setAfkTimeoutInSeconds(int afkTimeout) {
        this.afkTimeout = afkTimeout;
        return this;
    }

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(BufferedImage icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        return this;
    }

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(BufferedImage icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        return this;
    }

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(File icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        return this;
    }

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(Icon icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        return this;
    }

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(URL icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        return this;
    }

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(byte[] icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        return this;
    }

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(byte[] icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        return this;
    }

    /**
     * Sets the server's icon.
     * This method assumes the file type is "png"!
     *
     * @param icon The icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(InputStream icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        return this;
    }

    /**
     * Sets the server's icon.
     *
     * @param icon The icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerBuilder setIcon(InputStream icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        return this;
    }

    /**
     * Creates the server.
     *
     * @return The id of the server.
     */
    public CompletableFuture<Long> create() {
        // Server settings
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
        }
        if (region != null) {
            body.put("region", region.getKey());
        }
        if (verificationLevel != null) {
            body.put("verification_level", verificationLevel.getId());
        }
        if (defaultMessageNotificationLevel != null) {
            body.put("default_message_notifications", defaultMessageNotificationLevel.getId());
        }
        if (afkTimeout != null) {
            body.put("afk_timeout", afkTimeout.intValue());
        }
        if (icon != null) {
            return icon.asByteArray(api).thenAccept(bytes -> {
                String base64Icon = "data:image/" + icon.getFileType() + ";base64," +
                        Base64.getEncoder().encodeToString(bytes);
                body.put("icon", base64Icon);
            }).thenCompose(aVoid -> new RestRequest<Long>(api, RestMethod.POST, RestEndpoint.SERVER)
                    .setBody(body)
                    .execute(result -> result.getJsonBody().get("id").asLong()));
        }
        return new RestRequest<Long>(api, RestMethod.POST, RestEndpoint.SERVER)
                .setBody(body)
                .execute(result -> result.getJsonBody().get("id").asLong());
    }

}
