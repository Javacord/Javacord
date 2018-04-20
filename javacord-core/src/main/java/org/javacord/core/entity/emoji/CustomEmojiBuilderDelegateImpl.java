package org.javacord.core.entity.emoji;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.emoji.internal.CustomEmojiBuilderDelegate;
import org.javacord.api.entity.permission.Role;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link CustomEmojiBuilderDelegate}.
 */
public class CustomEmojiBuilderDelegateImpl implements CustomEmojiBuilderDelegate {

    /**
     * The server of the emoji.
     */
    private final ServerImpl server;

    /**
     * The reason for the creation.
     */
    private String reason = null;

    /**
     * The name of the emoji.
     */
    private String name = null;

    /**
     * The image of the emoji.
     */
    private FileContainer image = null;

    /**
     * The whitelist of the emoji.
     */
    private Collection<Role> whitelist = null;

    /**
     * Creates a new custom emoji builder delegate.
     *
     * @param server The server of the channel.
     */
    public CustomEmojiBuilderDelegateImpl(ServerImpl server) {
        this.server = server;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setImage(Icon image) {
        this.image = (image == null) ? null : new FileContainer(image);
    }

    @Override
    public void setImage(URL image) {
        this.image = (image == null) ? null : new FileContainer(image);
    }

    @Override
    public void setImage(File image) {
        this.image = (image == null) ? null : new FileContainer(image);
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
    }

    @Override
    public void setImage(BufferedImage image, String type) {
        this.image = (image == null) ? null : new FileContainer(image, type);
    }

    @Override
    public void setImage(byte[] image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
    }

    @Override
    public void setImage(byte[] image, String type) {
        this.image = (image == null) ? null : new FileContainer(image, type);
    }

    @Override
    public void setImage(InputStream image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
    }

    @Override
    public void setImage(InputStream image, String type) {
        this.image = (image == null) ? null : new FileContainer(image, type);
    }

    @Override
    public void addRoleToWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = new HashSet<>();
        }
        whitelist.add(role);
    }

    @Override
    public void setWhitelist(Collection<Role> roles) {
        whitelist = roles == null ? null : new HashSet<>(roles);
    }

    @Override
    public void setWhitelist(Role... roles) {
        setWhitelist(roles == null ? null : Arrays.asList(roles));
    }

    @Override
    public CompletableFuture<KnownCustomEmoji> create() {
        if (name == null) {
            throw new IllegalStateException("The name is no optional parameter!");
        }
        if (image == null) {
            throw new IllegalStateException("The image is no optional parameter!");
        }
        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("name", name);

        if (whitelist != null) {
            ArrayNode jsonRoles = body.putArray("roles");
            whitelist.stream().map(Role::getIdAsString).forEach(jsonRoles::add);
        }

        return image.asByteArray(server.getApi()).thenAccept(bytes -> {
            String base64Icon = "data:image/" + image.getFileType() + ";base64,"
                    + Base64.getEncoder().encodeToString(bytes);
            body.put("image", base64Icon);
        }).thenCompose(aVoid ->
                new RestRequest<KnownCustomEmoji>(server.getApi(), RestMethod.POST, RestEndpoint.CUSTOM_EMOJI)
                        .setUrlParameters(server.getIdAsString())
                        .setBody(body)
                        .setAuditLogReason(reason)
                        .execute(result -> ((DiscordApiImpl) server.getApi())
                                .getOrCreateKnownCustomEmoji(server, result.getJsonBody()))
        );
    }

}
