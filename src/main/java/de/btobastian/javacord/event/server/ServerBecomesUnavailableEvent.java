package de.btobastian.javacord.event.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.server.Server;

/**
 * A server becomes unavailable event.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#guild-unavailability">Discord docs</a>
 */
public class ServerBecomesUnavailableEvent extends ServerEvent {

    /**
     * Creates a new server becomes unavailable event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     */
    public ServerBecomesUnavailableEvent(DiscordApi api, Server server) {
        super(api, server);
    }

}
