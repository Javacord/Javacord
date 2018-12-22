package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.ServerTextChannelUpdaterDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server text channels.
 */
public class ServerTextChannelUpdater extends ServerChannelUpdater {

    /**
     * The server text channel delegate used by this instance.
     */
    private final ServerTextChannelUpdaterDelegate delegate;

    /**
     * Creates a new server text channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerTextChannelUpdater(ServerTextChannel channel) {
        delegate = DelegateFactory.createServerTextChannelUpdaterDelegate(channel);
    }

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setTopic(String topic) {
        delegate.setTopic(topic);
        return this;
    }

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setNsfwFlag(boolean nsfw) {
        delegate.setNsfwFlag(nsfw);
        return this;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater removeCategory() {
        delegate.removeCategory();
        return this;
    }

    /**
     * Set the delay for slowmode.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setSlowmodeDelayInSeconds(int delay) {
        delegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }

    /**
     * Unset the slowmode.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater unsetSlowmode() {
        return this.setSlowmodeDelayInSeconds(0);
    }

    @Override
    public ServerTextChannelUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    @Override
    public ServerTextChannelUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    @Override
    public ServerTextChannelUpdater setRawPosition(int rawPosition) {
        delegate.setRawPosition(rawPosition);
        return this;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> ServerTextChannelUpdater addPermissionOverwrite(
            T permissionable, Permissions permissions) {
        delegate.addPermissionOverwrite(permissionable, permissions);
        return this;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> ServerTextChannelUpdater removePermissionOverwrite(
            T permissionable) {
        delegate.removePermissionOverwrite(permissionable);
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
