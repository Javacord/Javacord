package org.javacord.entity.channel;

import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.util.DelegateFactory;

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
    public ServerTextChannelUpdater addPermissionOverwrite(User user, Permissions permissions) {
        delegate.addPermissionOverwrite(user, permissions);
        return this;
    }

    @Override
    public ServerTextChannelUpdater addPermissionOverwrite(Role role, Permissions permissions) {
        delegate.addPermissionOverwrite(role, permissions);
        return this;
    }

    @Override
    public ServerTextChannelUpdater removePermissionOverwrite(User user) {
        delegate.removePermissionOverwrite(user);
        return this;
    }

    @Override
    public ServerTextChannelUpdater removePermissionOverwrite(Role role) {
        delegate.removePermissionOverwrite(role);
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
