package org.javacord.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerTextChannelBuilder} to create server text channels.
 * You usually don't want to interact with this object.
 */
public interface ServerTextChannelBuilderDelegate {

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
     * Sets the topic of the channel.
     *
     * @param topic The topic of the channel.
     */
    void setTopic(String topic);

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     */
    void setCategory(ChannelCategory category);

    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    CompletableFuture<ServerTextChannel> create();

}
