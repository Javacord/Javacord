package org.javacord.api.entity.channel;

import org.javacord.api.entity.server.Server;
import org.javacord.api.util.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server text channels.
 */
public class ServerTextChannelBuilder {

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

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
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
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    public CompletableFuture<ServerTextChannel> create() {
        return delegate.create();
    }

}
