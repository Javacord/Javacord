package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ServerChannelUpdater;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerChannelUpdater} to update server channels.
 * You usually don't want to interact with this object.
 */
public interface ServerChannelUpdaterDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     */
    void setName(String name);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
