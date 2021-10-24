package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.internal.ServerThreadUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server thread channels.
 */
public class ServerThreadUpdater {

    /**
     * The server thread channel delegate used by this instance.
     */
    private final ServerThreadUpdaterDelegate delegate;

    /**
     * Creates a new server thread updater.
     *
     * @param thread The thread to update.
     */
    public ServerThreadUpdater(ServerThreadChannel thread) {
        delegate = DelegateFactory.createServerThreadUpdaterDelegate(thread);
    }
    
    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }
    
    /**
     * Queues the archived to be updated.
     *
     * @param archived The new archived flag of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadUpdater setArchivedFlag(boolean archived) {
        delegate.setArchivedFlag(archived);
        return this;
    }

    /**
     * Queues the auto archive duration to be updated.
     *
     * @param autoArchiveDuration The new auto archive duration of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadUpdater setAutoArchiveDuration(AutoArchiveDuration autoArchiveDuration) {
        delegate.setAutoArchiveDuration(autoArchiveDuration);
        return this;
    }
    
    /**
     * Queues the locked to be updated.
     *
     * @param locked The new locked flag of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadUpdater setLockedFlag(boolean locked) {
        delegate.setLockedFlag(locked);
        return this;
    }
    
    /**
     * Queues the invitable to be updated.
     *
     * @param invitable The new invitable flag of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadUpdater setInvitableFlag(boolean invitable) {
        delegate.setInvitableFlag(invitable);
        return this;
    }

    /**
     * Queues slowmode delay to be updated.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadUpdater setSlowmodeDelayInSeconds(int delay) {
        delegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }
    
    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }
}
