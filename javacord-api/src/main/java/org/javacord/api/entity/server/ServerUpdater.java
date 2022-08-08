package org.javacord.api.entity.server;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.internal.ServerUpdaterDelegate;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.internal.DelegateFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the settings of a server.
 */
public class ServerUpdater {

    /**
     * The server delegate used by this instance.
     */
    private final ServerUpdaterDelegate delegate;

    /**
     * Creates a new server updater.
     *
     * @param server The server to update.
     */
    public ServerUpdater(Server server) {
        delegate = DelegateFactory.createServerUpdaterDelegate(server);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Queues the region to be updated.
     *
     * @param region The new region of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setRegion(Region region) {
        delegate.setRegion(region);
        return this;
    }

    /**
     * Queues the explicit content filter level to be updated.
     *
     * @param explicitContentFilterLevel The new explicit content filter level of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel) {
        delegate.setExplicitContentFilterLevel(explicitContentFilterLevel);
        return this;
    }

    /**
     * Queues the verification level to be updated.
     *
     * @param verificationLevel The new verification level of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setVerificationLevel(VerificationLevel verificationLevel) {
        delegate.setVerificationLevel(verificationLevel);
        return this;
    }

    /**
     * Queues the default message notification level to be updated.
     *
     * @param defaultMessageNotificationLevel The new default message notification level of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        delegate.setDefaultMessageNotificationLevel(defaultMessageNotificationLevel);
        return this;
    }

    /**
     * Queues the afk channel to be updated.
     *
     * @param afkChannel The new afk channel of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setAfkChannel(ServerVoiceChannel afkChannel) {
        delegate.setAfkChannel(afkChannel);
        return this;
    }

    /**
     * Queues the afk channel to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater removeAfkChannel() {
        delegate.removeAfkChannel();
        return this;
    }

    /**
     * Queues the afk timeout in seconds to be updated.
     *
     * @param afkTimeout The new afk timeout in seconds of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setAfkTimeoutInSeconds(int afkTimeout) {
        delegate.setAfkTimeoutInSeconds(afkTimeout);
        return this;
    }

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(BufferedImage icon) {
        delegate.setIcon(icon);
        return this;
    }

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(BufferedImage icon, String fileType) {
        delegate.setIcon(icon, fileType);
        return this;
    }

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(File icon) {
        delegate.setIcon(icon);
        return this;
    }

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(Icon icon) {
        delegate.setIcon(icon);
        return this;
    }

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(URL icon) {
        delegate.setIcon(icon);
        return this;
    }

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(byte[] icon) {
        delegate.setIcon(icon);
        return this;
    }

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(byte[] icon, String fileType) {
        delegate.setIcon(icon, fileType);
        return this;
    }

    /**
     * Queues the icon to be updated.
     * This method assumes the file type is "png"!
     *
     * @param icon The new icon of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(InputStream icon) {
        delegate.setIcon(icon);
        return this;
    }

    /**
     * Queues the icon to be updated.
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setIcon(InputStream icon, String fileType) {
        delegate.setIcon(icon, fileType);
        return this;
    }

    /**
     * Queues the icon to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater removeIcon() {
        delegate.removeIcon();
        return this;
    }

    /**
     * Queues the owner to be updated.
     * You must be the owner of this server in order to transfer it!
     *
     * @param owner The new owner of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setOwner(User owner) {
        delegate.setOwner(owner);
        return this;
    }

    /**
     * Queues the splash to be updated.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(BufferedImage splash) {
        delegate.setSplash(splash);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(BufferedImage splash, String fileType) {
        delegate.setSplash(splash, fileType);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(File splash) {
        delegate.setSplash(splash);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(Icon splash) {
        delegate.setSplash(splash);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(URL splash) {
        delegate.setSplash(splash);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(byte[] splash) {
        delegate.setSplash(splash);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(byte[] splash, String fileType) {
        delegate.setSplash(splash, fileType);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     * This method assumes the file type is "png"!
     *
     * @param splash The new splash of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(InputStream splash) {
        delegate.setSplash(splash);
        return this;
    }

    /**
     * Queues the splash to be updated. Requires {@link ServerFeature#INVITE_SPLASH}.
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSplash(InputStream splash, String fileType) {
        delegate.setSplash(splash, fileType);
        return this;
    }

    /**
     * Queues the splash to be removed. Requires {@link ServerFeature#INVITE_SPLASH}.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater removeSplash() {
        delegate.removeSplash();
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     * This method assumes the file type is "png"!
     *
     * @param banner The new banner of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(BufferedImage banner) {
        delegate.setBanner(banner);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     *
     * @param banner The new banner of the server.
     * @param fileType The type of the banner, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(BufferedImage banner, String fileType) {
        delegate.setBanner(banner, fileType);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     *
     * @param banner The new banner of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(File banner) {
        delegate.setBanner(banner);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     *
     * @param banner The new banner of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(Icon banner) {
        delegate.setBanner(banner);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     *
     * @param banner The new banner of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(URL banner) {
        delegate.setBanner(banner);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     * This method assumes the file type is "png"!
     *
     * @param banner The new banner of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(byte[] banner) {
        delegate.setBanner(banner);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     *
     * @param banner The new banner of the server.
     * @param fileType The type of the banner, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(byte[] banner, String fileType) {
        delegate.setBanner(banner, fileType);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     * This method assumes the file type is "png"!
     *
     * @param banner The new banner of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(InputStream banner) {
        delegate.setBanner(banner);
        return this;
    }

    /**
     * Queues the banner to be updated. Requires {@link ServerFeature#BANNER}.
     *
     * @param banner The new banner of the server.
     * @param fileType The type of the banner, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setBanner(InputStream banner, String fileType) {
        delegate.setBanner(banner, fileType);
        return this;
    }

    /**
     * Queues the banner to be removed. Requires {@link ServerFeature#BANNER}.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater removeBanner() {
        delegate.removeBanner();
        return this;
    }

    /**
     * Queues the rules channel to be updated. Server has to be "PUBLIC".
     *
     * @param rulesChannel The new rules channel of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setRulesChannel(ServerTextChannel rulesChannel) {
        delegate.setRulesChannel(rulesChannel);
        return this;
    }

    /**
     * Queues the rules channel to be removed. Server has to be "PUBLIC".
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater removeRulesChannel() {
        delegate.removeRulesChannel();
        return this;
    }

    /**
     * Queues the moderators-only channel to be updated. Server has to be "PUBLIC".
     *
     * @param moderatorsOnlyChannel The new moderators-only channel of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setModeratorsOnlyChannel(ServerTextChannel moderatorsOnlyChannel) {
        delegate.setModeratorsOnlyChannel(moderatorsOnlyChannel);
        return this;
    }

    /**
     * Queues the moderators-only channel to be removed. Server has to be "PUBLIC".
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater removeModeratorsOnlyChannel() {
        delegate.removeModeratorsOnlyChannel();
        return this;
    }

    /**
     * Queues the preferred locale of a public guild to be updated.
     *
     * @param locale The preferred locale of the public server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setPreferredLocale(Locale locale) {
        delegate.setPreferredLocale(locale);
        return this;
    }

    /**
     * Queues the system channel to be updated.
     *
     * @param systemChannel The new system channel of the server.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater setSystemChannel(ServerTextChannel systemChannel) {
        delegate.setSystemChannel(systemChannel);
        return this;
    }

    /**
     * Queues the system channel to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater removeSystemChannel() {
        delegate.removeSystemChannel();
        return this;
    }

    /**
     * Sets the new order for the server's roles.
     *
     * @param roles An ordered list with the new role positions.
     * @return The current instance in order to chain call methods.
     */
    public ServerUpdater reorderRoles(List<Role> roles) {
        delegate.reorderRoles(roles);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
