package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeAfkTimeoutEvent;

/**
 * The implementation of {@link ServerChangeAfkTimeoutEvent}.
 */
public class ServerChangeAfkTimeoutEventImpl extends ServerEventImpl implements ServerChangeAfkTimeoutEvent {

    /**
     * The new afk timeout of the server.
     */
    private final int newAfkTimeout;

    /**
     * The old afk timeout of the server.
     */
    private final int oldAfkTimeout;

    /**
     * Creates a new server change afk timeout event.
     *
     * @param server The server of the event.
     * @param newAfkTimeout The new afk timeout of the server.
     * @param oldAfkTimeout The old afk timeout of the server.
     */
    public ServerChangeAfkTimeoutEventImpl(Server server, int newAfkTimeout, int oldAfkTimeout) {
        super(server);
        this.newAfkTimeout = newAfkTimeout;
        this.oldAfkTimeout = oldAfkTimeout;
    }

    @Override
    public int getOldAfkTimeoutInSeconds() {
        return oldAfkTimeout;
    }

    @Override
    public int getNewAfkTimeoutInSeconds() {
        return newAfkTimeout;
    }

}
