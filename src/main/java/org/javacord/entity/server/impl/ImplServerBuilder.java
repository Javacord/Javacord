package org.javacord.entity.server.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ServerBuilder;
import org.javacord.entity.server.VerificationLevel;
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
 * The implementation of {@link ServerBuilder}.
 */
public class ImplServerBuilder implements ServerBuilder {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

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
     * Creates a new server builder.
     *
     * @param api The discord api instance.
     */
    public ImplServerBuilder(ImplDiscordApi api) {
        this.api = api;
    }

    @Override
    public ServerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ServerBuilder setRegion(Region region) {
        this.region = region;
        return this;
    }

    @Override
    public ServerBuilder setVerificationLevel(VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
        return this;
    }

    @Override
    public ServerBuilder setDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
        return this;
    }

    @Override
    public ServerBuilder setAfkTimeoutInSeconds(int afkTimeout) {
        this.afkTimeout = afkTimeout;
        return this;
    }

    @Override
    public ServerBuilder setIcon(BufferedImage icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        return this;
    }

    @Override
    public ServerBuilder setIcon(BufferedImage icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        return this;
    }

    @Override
    public ServerBuilder setIcon(File icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        return this;
    }

    @Override
    public ServerBuilder setIcon(Icon icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        return this;
    }

    @Override
    public ServerBuilder setIcon(URL icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        return this;
    }

    @Override
    public ServerBuilder setIcon(byte[] icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        return this;
    }

    @Override
    public ServerBuilder setIcon(byte[] icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        return this;
    }

    @Override
    public ServerBuilder setIcon(InputStream icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        return this;
    }

    @Override
    public ServerBuilder setIcon(InputStream icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        return this;
    }

    @Override
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
