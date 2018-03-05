package org.javacord.entity.server;

import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.user.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the settings of a server.
 */
public interface ServerUpdater {
    
    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setAuditLogReason(String reason);
    
    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setName(String name);

    /**
     * Queues the region to be updated.
     *
     * @param region The new region of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setRegion(Region region);

    /**
     * Queues the explicit content filter level to be updated.
     *
     * @param explicitContentFilterLevel The new explicit content filter level of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel);

    /**
     * Queues the verification level to be updated.
     *
     * @param verificationLevel The new verification level of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setVerificationLevel(VerificationLevel verificationLevel);

    /**
     * Queues the default message notification level to be updated.
     *
     * @param defaultMessageNotificationLevel The new default message notification level of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel);

    /**
     * Queues the afk channel to be updated.
     *
     * @param afkChannel The new afk channel of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setAfkChannel(ServerVoiceChannel afkChannel);

    /**
     * Queues the afk channel to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater removeAfkChannel();

    /**
     * Queues the afk timeout in seconds to be updated.
     *
     * @param afkTimeout The new afk timeout in seconds of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setAfkTimeoutInSeconds(int afkTimeout);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(BufferedImage icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(BufferedImage icon, String fileType);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(File icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(Icon icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(URL icon);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(byte[] icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(byte[] icon, String fileType);

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(InputStream icon);

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setIcon(InputStream icon, String fileType);

    /**
     * Queues the icon to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater removeIcon();

    /**
     * Queues the owner to be updated.
     * You must be the owner of this server in order to transfer it!
     *
     * @param owner The new owner of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setOwner(User owner);

    /**
     * Queues the splash to be updated.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(BufferedImage splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(BufferedImage splash, String fileType);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(File splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(Icon splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(URL splash);

    /**
     * Queues the splash to be updated.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(byte[] splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(byte[] splash, String fileType);

    /**
     * Queues the splash to be updated.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(InputStream splash);

    /**
     * Queues the splash to be updated.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSplash(InputStream splash, String fileType);

    /**
     * Queues the splash to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater removeSplash();

    /**
     * Queues the system channel to be updated.
     *
     * @param systemChannel The new system channel of the server.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setSystemChannel(ServerTextChannel systemChannel);

    /**
     * Queues the system channel to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater removeSystemChannel();

    /**
     * Queues a user's nickname to be updated.
     *
     * @param user The user whose nickname should be updated.
     * @param nickname The new nickname of the user.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater setNickname(User user, String nickname);

    /**
     * Sets the new order for the server's roles.
     *
     * @param roles An ordered list with the new role positions.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater reorderRoles(List<Role> roles);

    /**
     * Queues a role to be assigned to the user.
     *
     * @param user The user to whom the role should be assigned.
     * @param role The role to be assigned.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater addRoleToUser(User user, Role role);

    /**
     * Queues a collection of roles to be assigned to the user.
     *
     * @param user The user to whom the roles should be assigned.
     * @param roles The collection of roles to be assigned.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater addAllRolesToUser(User user, Collection<Role> roles);

    /**
     * Queues a role to be removed from the user.
     *
     * @param user The user who should lose the role.
     * @param role The role to be removed.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater removeRoleFromUser(User user, Role role);

    /**
     * Queues a collection of roles to be removed from the user.
     *
     * @param user The user who should lose the roles.
     * @param roles The collection of roles to be removed.
     * @return The current instance in order to chain call methods.
     */
    ServerUpdater removeAllRolesFromUser(User user, Collection<Role> roles);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
