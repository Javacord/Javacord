package org.javacord.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server text channels.
 */
public interface ServerTextChannelBuilder {

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    ServerTextChannelBuilder setAuditLogReason(String reason);

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerTextChannelBuilder setName(String name);

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerTextChannelBuilder setCategory(ChannelCategory category);

    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    CompletableFuture<ServerTextChannel> create();

}
