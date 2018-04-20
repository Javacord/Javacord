package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.entity.server.internal.ServerBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerBuilderDelegate}.
 */
public class ServerBuilderDelegateImpl implements ServerBuilderDelegate {

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The name.
     */
    private String name = null;

    /**
     * The region.
     */
    private Region region = null;

    /**
     * The explicit content filter level.
     */
    private ExplicitContentFilterLevel explicitContentFilterLevel = null;

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
     * Creates a new server builder delegate.
     *
     * @param api The discord api instance.
     */
    public ServerBuilderDelegateImpl(DiscordApiImpl api) {
        this.api = api;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public void setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel) {
        this.explicitContentFilterLevel = explicitContentFilterLevel;
    }

    @Override
    public void setVerificationLevel(VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    @Override
    public void setDefaultMessageNotificationLevel(DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
    }

    @Override
    public void setAfkTimeoutInSeconds(int afkTimeout) {
        this.afkTimeout = afkTimeout;
    }

    @Override
    public void setIcon(BufferedImage icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
    }

    @Override
    public void setIcon(BufferedImage icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
    }

    @Override
    public void setIcon(File icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
    }

    @Override
    public void setIcon(URL icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
    }

    @Override
    public void setIcon(byte[] icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
    }

    @Override
    public void setIcon(byte[] icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
    }

    @Override
    public void setIcon(InputStream icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
    }

    @Override
    public void setIcon(InputStream icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
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
        if (explicitContentFilterLevel != null) {
            body.put("explicit_content_filter", explicitContentFilterLevel.getId());
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
                String base64Icon = "data:image/" + icon.getFileType() + ";base64,"
                        + Base64.getEncoder().encodeToString(bytes);
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
