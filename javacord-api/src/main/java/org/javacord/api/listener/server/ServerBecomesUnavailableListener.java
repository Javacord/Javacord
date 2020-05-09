package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerBecomesUnavailableEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to servers becoming unavailable.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discord.com/developers/docs/topics/gateway#guild-unavailability">Discord docs</a>
 */
@FunctionalInterface
public interface ServerBecomesUnavailableListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server became available.
     *
     * @param event The event.
     */
    void onServerBecomesUnavailable(ServerBecomesUnavailableEvent event);
}
