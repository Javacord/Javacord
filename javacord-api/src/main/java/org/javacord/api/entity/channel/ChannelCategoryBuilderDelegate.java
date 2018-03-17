package org.javacord.api.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ChannelCategoryBuilder} to create channel categories.
 * You usually don't want to interact with this object.
 */
public interface ChannelCategoryBuilderDelegate {

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     */
    void setName(String name);

    /**
     * Creates the channel category.
     *
     * @return The created channel category.
     */
    CompletableFuture<ChannelCategory> create();

}
