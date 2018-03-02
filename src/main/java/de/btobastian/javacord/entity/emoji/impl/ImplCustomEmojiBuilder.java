package de.btobastian.javacord.entity.emoji.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.emoji.CustomEmojiBuilder;
import de.btobastian.javacord.entity.emoji.KnownCustomEmoji;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.entity.server.Server;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.util.FileContainer;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

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
 * The implementation of {@link CustomEmojiBuilder}.
 */
public class ImplCustomEmojiBuilder implements CustomEmojiBuilder {

    /**
     * The server of the emoji.
     */
    private ImplServer server;

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
     * Creates a new custom emoji builder.
     *
     * @param server The server of the channel.
     */
    public ImplCustomEmojiBuilder(Server server) {
        this.server = (ImplServer) server;
    }

    @Override
    public ImplCustomEmojiBuilder setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(Icon image) {
        this.image = (image == null) ? null : new FileContainer(image);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(URL image) {
        this.image = (image == null) ? null : new FileContainer(image);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(File image) {
        this.image = (image == null) ? null : new FileContainer(image);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(BufferedImage image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(BufferedImage image, String type) {
        this.image = (image == null) ? null : new FileContainer(image, type);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(byte[] image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(byte[] image, String type) {
        this.image = (image == null) ? null : new FileContainer(image, type);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(InputStream image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setImage(InputStream image, String type) {
        this.image = (image == null) ? null : new FileContainer(image, type);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder addRoleToWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = new HashSet<>();
        }
        whitelist.add(role);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setWhitelist(Collection<Role> roles) {
        whitelist = roles == null ? null : new HashSet<>(roles);
        return this;
    }

    @Override
    public ImplCustomEmojiBuilder setWhitelist(Role... roles) {
        return setWhitelist(roles == null ? null : Arrays.asList(roles));
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
            String base64Icon = "data:image/" + image.getFileType() + ";base64," +
                    Base64.getEncoder().encodeToString(bytes);
            body.put("image", base64Icon);
        }).thenCompose(aVoid ->
                new RestRequest<KnownCustomEmoji>(server.getApi(), RestMethod.POST, RestEndpoint.CUSTOM_EMOJI)
                        .setUrlParameters(server.getIdAsString())
                        .setBody(body)
                        .setAuditLogReason(reason)
                        .execute(result -> ((ImplDiscordApi) server.getApi())
                                .getOrCreateKnownCustomEmoji(server, result.getJsonBody()))
        );
    }

}
