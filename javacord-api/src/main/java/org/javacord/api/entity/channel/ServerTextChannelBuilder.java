package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.ServerTextChannelBuilderDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server text channels.
 */
public class ServerTextChannelBuilder extends ServerChannelBuilder {

    /**
     * The server text channel delegate used by this instance.
     */
    private final ServerTextChannelBuilderDelegate delegate;

    /**
     * Creates a new server text channel builder.
     *
     * @param server The server of the server text channel.
     */
    public ServerTextChannelBuilder(Server server) {
        delegate = DelegateFactory.createServerTextChannelBuilderDelegate(server);
    }

    @Override
    public ServerTextChannelBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    @Override
    public ServerTextChannelBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the topic of the channel.
     *
     * @param topic The topic of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelBuilder setTopic(String topic) {
        delegate.setTopic(topic);
        return this;
    }

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelBuilder setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Sets the slowmode of the channel.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelBuilder setSlowmodeDelayInSeconds(int delay) {
        delegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> ServerTextChannelBuilder addPermissionOverwrite(
            T permissionable, Permissions permissions) {
        delegate.addPermissionOverwrite(permissionable, permissions);
        return this;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> ServerTextChannelBuilder removePermissionOverwrite(
            T permissionable) {
        delegate.removePermissionOverwrite(permissionable);
        return this;
    }

    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    public CompletableFuture<ServerTextChannel> create() {
        return delegate.create();
    }

}
