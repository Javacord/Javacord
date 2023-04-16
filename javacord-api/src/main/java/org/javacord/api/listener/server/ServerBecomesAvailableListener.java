package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerBecomesAvailableEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to servers becoming available.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discord.com/developers/docs/topics/gateway#guild-availability">Discord docs</a>
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerBecomesAvailableListener extends GloballyAttachableListener {

    /**
     * This method is called every time a server became unavailable.
     *
     * @param event The event.
     */
    void onServerBecomesAvailable(ServerBecomesAvailableEvent event);
}
