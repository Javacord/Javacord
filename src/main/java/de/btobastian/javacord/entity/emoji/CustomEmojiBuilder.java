package de.btobastian.javacord.entity.emoji;

import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.permission.Role;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new custom emojis.
 */
public interface CustomEmojiBuilder {

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setAuditLogReason(String reason);

    /**
     * Sets the name of the emoji.
     *
     * @param name The name of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setName(String name);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(Icon image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(URL image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image file of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(File image);

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(BufferedImage image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(BufferedImage image, String type);

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(byte[] image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(byte[] image, String type);

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(InputStream image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setImage(InputStream image, String type);

    /**
     * Adds a role to the whitelist.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param role The role to add.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder addRoleToWhitelist(Role role);

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setWhitelist(Collection<Role> roles);

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiBuilder setWhitelist(Role... roles);

    /**
     * Creates the custom emoji.
     *
     * @return The created custom emoji.
     */
    CompletableFuture<KnownCustomEmoji> create();

}
