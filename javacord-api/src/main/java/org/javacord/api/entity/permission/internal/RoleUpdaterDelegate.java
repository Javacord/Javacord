package org.javacord.api.entity.permission.internal;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.RoleUpdater;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link RoleUpdater} to update roles.
 * You usually don't want to interact with this object.
 */
public interface RoleUpdaterDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the role.
     */
    void setName(String name);

    /**
     * Queues the permissions to be updated.
     *
     * @param permissions The new permissions of the role.
     */
    void setPermissions(Permissions permissions);

    /**
     * Queues the color to be updated.
     *
     * @param color The new color of the role.
     */
    void setColor(Color color);

    /**
     * Queues the display separately flag (sometimes called "hoist") to be updated.
     *
     * @param displaySeparately The new display separately flag of the role.
     */
    void setDisplaySeparatelyFlag(boolean displaySeparately);

    /**
     * Queues the mentionable flag to be updated.
     *
     * @param mentionable The new mentionable flag of the role.
     */
    void setMentionableFlag(boolean mentionable);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the role.
     */
    void setIcon(BufferedImage icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the role.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(BufferedImage icon, String fileType);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the role.
     */
    void setIcon(File icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the role.
     */
    void setIcon(Icon icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the role.
     */
    void setIcon(URL icon);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the role.
     */
    void setIcon(byte[] icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the role.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(byte[] icon, String fileType);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the role.
     */
    void setIcon(InputStream icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the role.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(InputStream icon, String fileType);

    /**
     * Queues the icon to be removed.
     */
    void removeIcon();

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
