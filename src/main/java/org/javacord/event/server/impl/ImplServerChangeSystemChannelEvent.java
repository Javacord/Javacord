package org.javacord.event.server.impl;

import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeSystemChannelEvent;

import java.util.Optional;

/**
 * The implementation of {@link ServerChangeSystemChannelEvent}.
 */
public class ImplServerChangeSystemChannelEvent extends ImplServerEvent implements ServerChangeSystemChannelEvent {

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
     * @param server The server of the event.
     * @param newSystemChannel The new system channel of the server.
     * @param oldSystemChannel The old system channel of the server.
     */
    public ImplServerChangeSystemChannelEvent(
            Server server, ServerTextChannel newSystemChannel, ServerTextChannel oldSystemChannel) {
        super(server);
        this.newSystemChannel = newSystemChannel;
        this.oldSystemChannel = oldSystemChannel;
    }

    @Override
    public Optional<ServerTextChannel> getOldSystemChannel() {
        return Optional.ofNullable(oldSystemChannel);
    }

    @Override
    public Optional<ServerTextChannel> getNewSystemChannel() {
        return Optional.ofNullable(newSystemChannel);
    }

}
