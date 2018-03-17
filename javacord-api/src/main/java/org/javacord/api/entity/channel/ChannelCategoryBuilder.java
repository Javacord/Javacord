package org.javacord.api.entity.channel;

import org.javacord.api.entity.server.Server;
import org.javacord.api.util.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new channel categories.
 */
public class ChannelCategoryBuilder {

    /**
     * The channel category delegate used by this instance.
     */
    private final ChannelCategoryBuilderDelegate delegate;

    /**
     * Creates a new channel category builder.
     *
     * @param server The server of the channel category.
     */
    public ChannelCategoryBuilder(Server server) {
        delegate = DelegateFactory.createChannelCategoryBuilderDelegate(server);
    }

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ChannelCategoryBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ChannelCategoryBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Creates the channel category.
     *
     * @return The created channel category.
     */
    public CompletableFuture<ChannelCategory> create() {
        return delegate.create();
    }

}
