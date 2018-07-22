package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.ServerVoiceChannelUpdaterDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server voice channels.
 */
public class ServerVoiceChannelUpdater extends ServerChannelUpdater {

    /**
     * The server voice channel delegate used by this instance.
     */
    private final ServerVoiceChannelUpdaterDelegate delegate;

    /**
     * Creates a new server voice channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerVoiceChannelUpdater(ServerVoiceChannel channel) {
        delegate = DelegateFactory.createServerVoiceChannelUpdaterDelegate(channel);
    }

    /**
     * Queues the bitrate to be updated.
     *
     * @param bitrate The new bitrate of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setBitrate(int bitrate) {
        delegate.setBitrate(bitrate);
        return this;
    }

    /**
     * Queues the user limit to be updated.
     *
     * @param userLimit The new user limit of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setUserLimit(int userLimit) {
        delegate.setUserLimit(userLimit);
        return this;
    }

    /**
     * Queues the user limit to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater removeUserLimit() {
        delegate.removeUserLimit();
        return this;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater removeCategory() {
        delegate.removeCategory();
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater setRawPosition(int rawPosition) {
        delegate.setRawPosition(rawPosition);
        return this;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> ServerVoiceChannelUpdater addPermissionOverwrite(
            T permissionable, Permissions permissions) {
        delegate.addPermissionOverwrite(permissionable, permissions);
        return this;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> ServerVoiceChannelUpdater removePermissionOverwrite(
            T permissionable) {
        delegate.removePermissionOverwrite(permissionable);
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
