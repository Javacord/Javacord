package org.javacord.api.entity.emoji;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.emoji.internal.CustomEmojiBuilderDelegate;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new custom emojis.
 */
public class CustomEmojiBuilder {

    /**
     * The custom emoji delegate used by this instance.
     */
    private final CustomEmojiBuilderDelegate delegate;

    /**
     * Creates a new custom emoji builder.
     *
     * @param server The server of the custom emoji.
     */
    public CustomEmojiBuilder(Server server) {
        delegate = DelegateFactory.createCustomEmojiBuilderDelegate(server);
    }

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the emoji.
     *
     * @param name The name of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(Icon image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(URL image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Sets the image of the emoji.
     *
     * @param image The image file of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(File image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(BufferedImage image) {
        delegate.setImage(image);
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
        delegate.setImage(image, type);
        return this;
    }

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(byte[] image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(byte[] image, String type) {
        delegate.setImage(image, type);
        return this;
    }

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(InputStream image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiBuilder setImage(InputStream image, String type) {
        delegate.setImage(image, type);
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
        delegate.addRoleToWhitelist(role);
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
        delegate.setWhitelist(roles);
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
        delegate.setWhitelist(roles);
        return this;
    }

    /**
     * Creates the custom emoji.
     *
     * @return The created custom emoji.
     */
    public CompletableFuture<KnownCustomEmoji> create() {
        return delegate.create();
    }

}
