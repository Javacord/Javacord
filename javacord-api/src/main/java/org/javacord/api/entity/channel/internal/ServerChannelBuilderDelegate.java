package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ServerChannelBuilder;

/**
 * This class is internally used by the {@link ServerChannelBuilder} to create server channels.
 * You usually don't want to interact with this object.
 */
public interface ServerChannelBuilderDelegate {

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

}
