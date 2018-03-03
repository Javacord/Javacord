package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;

import java.util.Optional;

/**
 * A server change system channel event.
 */
public class ServerChangeSystemChannelEvent extends ServerEvent {

    /**
     * The new system channel of the server.
     */
    private final ServerTextChannel newSystemChannel;

    /**
     * The old system channel of the server.
     */
    private final ServerTextChannel oldSystemChannel;

    /**
     * Creates a new server change system channel event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newSystemChannel The new system channel of the server.
     * @param oldSystemChannel The old system channel of the server.
     */
    public ServerChangeSystemChannelEvent(
            DiscordApi api, Server server, ServerTextChannel newSystemChannel, ServerTextChannel oldSystemChannel) {
        super(api, server);
        this.newSystemChannel = newSystemChannel;
        this.oldSystemChannel = oldSystemChannel;
    }

    /**
     * Gets the old system channel of the server.
     *
     * @return The old system channel of the server.
     */
    public Optional<ServerTextChannel> getOldSystemChannel() {
        return Optional.ofNullable(oldSystemChannel);
    }

    /**
     * Gets the new system channel of the server.
     *
     * @return The new system channel of the server.
     */
    public Optional<ServerTextChannel> getNewSystemChannel() {
        return Optional.ofNullable(newSystemChannel);
    }

}
