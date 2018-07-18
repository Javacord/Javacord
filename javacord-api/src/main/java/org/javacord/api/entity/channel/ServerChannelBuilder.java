package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerChannelBuilderDelegate;

/**
 * This class is used to create new server channels.
 */
public class ServerChannelBuilder {

    /**
     * The server channel delegate used by this instance.
     */
    private final ServerChannelBuilderDelegate delegate;

    /**
     * Creates a new server channel builder without delegate.
     */
    protected ServerChannelBuilder() {
        delegate = null;
    }

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelBuilder setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

}
