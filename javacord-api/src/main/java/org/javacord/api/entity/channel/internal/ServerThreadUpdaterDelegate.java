package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.channel.ServerThreadUpdater;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerThreadUpdater} to update server thread channels.
 * You usually don't want to interact with this object.
 */
public interface ServerThreadUpdaterDelegate {

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the thread.
     */
    void setName(String name);
    
    /**
     * Queues the archived to be updated.
     *
     * @param archived The new archived flag of the thread.
     */
    void setArchivedFlag(boolean archived);

    /**
     * Queues the auto archive duration to be updated.
     *
     * @param autoArchiveDuration The new auto archive duration of the thread.
     */
    void setAutoArchiveDuration(AutoArchiveDuration autoArchiveDuration);
    
    /**
     * Queues the locked to be updated.
     *
     * @param locked The new locked flag of the thread.
     */
    void setLockedFlag(boolean locked);
    
    /**
     * Queues the invitable to be updated.
     *
     * @param invitable The new invitable flag of the thread.
     */
    void setInvitableFlag(boolean invitable);

    /**
     * Queues slowmode delay to be updated.
     *
     * @param delay The delay in seconds.
     */
    void setSlowmodeDelayInSeconds(int delay);
    
    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);
    
    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();
}
