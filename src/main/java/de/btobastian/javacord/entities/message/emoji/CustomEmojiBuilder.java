package de.btobastian.javacord.entities.message.emoji;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new custom emojis.
 */
public class CustomEmojiBuilder {

    /**
     * The server of the emoji.
     */
    private ImplServer server;

    /**
     * The name of the emoji.
     */
    private String name = null;

    /**
     * The image of the emoji.
     */
    private BufferedImage image = null;

    /**
     * The type of the image.
     */
    private String imageType = "jpg";

    /**
     * The whitelist of the emoji.
     */
    private Collection<Role> whitelist = null;

    /**
     * Creates a new custom emoji builder.
     *
     * @param server The server of the channel.
     */
    public CustomEmojiBuilder(Server server) {
        this.server = (ImplServer) server;
    }

    /**
     * Sets the name of the emoji.
     *
     * @param name The name of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a jpg.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(BufferedImage image) {
        this.image = image;
        return this;
    }

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(BufferedImage image, String type) {
        this.image = image;
        imageType = type;
        return this;
    }

    /**
     * Adds a role to the whitelist.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param role The role to add.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder addRoleToWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = new HashSet<>();
        }
        whitelist.add(role);
        return this;
    }

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setWhitelist(Collection<Role> roles) {
        whitelist = roles == null ? null : new HashSet<>(roles);
        return this;
    }

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setWhitelist(Role... roles) {
        return setWhitelist(roles == null ? null : Arrays.asList(roles));
    }

    /**
     * Creates the custom emoji.
     *
     * @return The created custom emoji.
     */
    public CompletableFuture<KnownCustomEmoji> create() {
        if (name == null) {
            throw new IllegalStateException("The name is no optional parameter!");
        }
        if (image == null) {
            throw new IllegalStateException("The image is no optional parameter!");
        }
        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("name", name);
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, imageType, os);
            String base64Icon = "data:image/" + imageType + ";base64," + Base64.getEncoder().encodeToString(os.toByteArray());
            body.put("image", base64Icon);
        } catch (IOException e) {
            CompletableFuture<KnownCustomEmoji> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
        if (whitelist != null) {
            ArrayNode jsonRoles = body.putArray("roles");
            whitelist.stream().map(Role::getIdAsString).forEach(jsonRoles::add);
        }
        return new RestRequest<KnownCustomEmoji>(server.getApi(), RestMethod.POST, RestEndpoint.CUSTOM_EMOJI)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .execute(result -> ((ImplDiscordApi) server.getApi())
                        .getOrCreateKnownCustomEmoji(server, result.getJsonBody()));
    }

}
