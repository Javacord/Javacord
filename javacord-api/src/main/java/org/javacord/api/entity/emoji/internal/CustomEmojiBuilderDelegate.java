package org.javacord.api.entity.emoji.internal;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.emoji.CustomEmojiBuilder;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.permission.Role;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link CustomEmojiBuilder} to create custom emojis.
 * You usually don't want to interact with this object.
 */
public interface CustomEmojiBuilderDelegate {

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the name of the emoji.
     *
     * @param name The name of the emoji.
     */
    void setName(String name);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     */
    void setImage(Icon image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     */
    void setImage(URL image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image file of the emoji.
     */
    void setImage(File image);

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     */
    void setImage(BufferedImage image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     */
    void setImage(BufferedImage image, String type);

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     */
    void setImage(byte[] image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     */
    void setImage(byte[] image, String type);

    /**
     * Sets the image of the emoji.
     * This method assumes that the provided image is a png.
     *
     * @param image The image of the emoji.
     */
    void setImage(InputStream image);

    /**
     * Sets the image of the emoji.
     *
     * @param image The image of the emoji.
     * @param type The type of the image, e.g. "png", "jpg" or "gif".
     */
    void setImage(InputStream image, String type);

    /**
     * Adds a role to the whitelist.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param role The role to add.
     */
    void addRoleToWhitelist(Role role);

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     */
    void setWhitelist(Collection<Role> roles);

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     */
    void setWhitelist(Role... roles);

    /**
     * Creates the custom emoji.
     *
     * @return The created custom emoji.
     */
    CompletableFuture<KnownCustomEmoji> create();

}
