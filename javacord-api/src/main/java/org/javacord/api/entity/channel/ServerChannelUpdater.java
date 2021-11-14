package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server channels.
 */
public class ServerChannelUpdater<T extends ServerChannelUpdater<T>> {

    /**
     * The server channel delegate used by this instance.
     */
    private final ServerChannelUpdaterDelegate serverChannelUpdaterDelegate;

    /**
     * Creates a new server channel updater.
     */
    protected ServerChannelUpdater(ServerChannelUpdaterDelegate serverChannelUpdaterDelegate) {
        this.serverChannelUpdaterDelegate = serverChannelUpdaterDelegate;
    }

    /**
     * Creates a new server channel updater.
     * @param serverChannel The server channel to update.
     */
    public ServerChannelUpdater(ServerChannel serverChannel) {
        serverChannelUpdaterDelegate = DelegateFactory.createServerChannelUpdaterDelegate(serverChannel);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public T setAuditLogReason(String reason) {
        serverChannelUpdaterDelegate.setAuditLogReason(reason);
        return (T) this;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public T setName(String name) {
        serverChannelUpdaterDelegate.setName(name);
        return (T) this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return serverChannelUpdaterDelegate.update();
    }

}
