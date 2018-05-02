package org.javacord.api.entity.server.internal;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.entity.user.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerUpdater} to update servers.
 * You usually don't want to interact with this object.
 */
public interface ServerUpdaterDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the server.
     */
    void setName(String name);

    /**
     * Queues the region to be updated.
     *
     * @param region The new region of the server.
     */
    void setRegion(Region region);

    /**
     * Queues the explicit content filter level to be updated.
     *
     * @param explicitContentFilterLevel The new explicit content filter level of the server.
     */
    void setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel);

    /**
     * Queues the verification level to be updated.
     *
     * @param verificationLevel The new verification level of the server.
     */
    void setVerificationLevel(VerificationLevel verificationLevel);

    /**
     * Queues the default message notification level to be updated.
     *
     * @param defaultMessageNotificationLevel The new default message notification level of the server.
     */
    void setDefaultMessageNotificationLevel(DefaultMessageNotificationLevel defaultMessageNotificationLevel);

    /**
     * Queues the afk channel to be updated.
     *
     * @param afkChannel The new afk channel of the server.
     */
    void setAfkChannel(ServerVoiceChannel afkChannel);

    /**
     * Queues the afk channel to be removed.
     */
    void removeAfkChannel();

    /**
     * Queues the afk timeout in seconds to be updated.
     *
     * @param afkTimeout The new afk timeout in seconds of the server.
     */
    void setAfkTimeoutInSeconds(int afkTimeout);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     */
    void setIcon(BufferedImage icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(BufferedImage icon, String fileType);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     */
    void setIcon(File icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     */
    void setIcon(Icon icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     */
    void setIcon(URL icon);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     */
    void setIcon(byte[] icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(byte[] icon, String fileType);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     */
    void setIcon(InputStream icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     */
    void setIcon(InputStream icon, String fileType);

    /**
     * Queues the icon to be removed.
     */
    void removeIcon();

    /**
     * Queues the owner to be updated.
     * You must be the owner of this server in order to transfer it!
     *
     * @param owner The new owner of the server.
     */
    void setOwner(User owner);

    /**
     * Queues the splash to be updated.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     */
    void setSplash(BufferedImage splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     */
    void setSplash(BufferedImage splash, String fileType);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     */
    void setSplash(File splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     */
    void setSplash(Icon splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     */
    void setSplash(URL splash);

    /**
     * Queues the splash to be updated.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     */
    void setSplash(byte[] splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     */
    void setSplash(byte[] splash, String fileType);

    /**
     * Queues the splash to be updated.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     */
    void setSplash(InputStream splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     */
    void setSplash(InputStream splash, String fileType);

    /**
     * Queues the splash to be removed.
     */
    void removeSplash();

    /**
     * Queues the system channel to be updated.
     *
     * @param systemChannel The new system channel of the server.
     */
    void setSystemChannel(ServerTextChannel systemChannel);

    /**
     * Queues the system channel to be removed.
     */
    void removeSystemChannel();

    /**
     * Queues a user's nickname to be updated.
     *
     * @param user The user whose nickname should be updated.
     * @param nickname The new nickname of the user.
     */
    void setNickname(User user, String nickname);

    /**
     * Sets the new order for the server's roles.
     *
     * @param roles An ordered list with the new role positions.
     */
    void reorderRoles(List<Role> roles);

    /**
     * Queues a role to be assigned to the user.
     *
     * @param user The user to whom the role should be assigned.
     * @param role The role to be assigned.
     */
    void addRoleToUser(User user, Role role);

    /**
     * Queues a collection of roles to be assigned to the user.
     *
     * @param user The user to whom the roles should be assigned.
     * @param roles The collection of roles to be assigned.
     */
    void addRolesToUser(User user, Collection<Role> roles);

    /**
     * Queues a role to be removed from the user.
     *
     * @param user The user who should lose the role.
     * @param role The role to be removed.
     */
    void removeRoleFromUser(User user, Role role);

    /**
     * Queues a collection of roles to be removed from the user.
     *
     * @param user The user who should lose the roles.
     * @param roles The collection of roles to be removed.
     */
    void removeRolesFromUser(User user, Collection<Role> roles);

    /**
     * Queues all roles to be removed from the user.
     *
     * @param user The user who should lose the roles.
     */
    void removeAllRolesFromUser(User user);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
