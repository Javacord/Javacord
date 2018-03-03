package org.javacord.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new channel categories.
 */
public interface ChannelCategoryBuilder {

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    ChannelCategoryBuilder setAuditLogReason(String reason);

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    ChannelCategoryBuilder setName(String name);

    /**
     * Creates the channel category.
     *
     * @return The created channel category.
     */
    CompletableFuture<ChannelCategory> create();

}
