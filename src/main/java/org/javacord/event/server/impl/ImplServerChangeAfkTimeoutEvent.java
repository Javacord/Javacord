package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeAfkTimeoutEvent;

/**
 * The implementation of {@link ServerChangeAfkTimeoutEvent}.
 */
public class ImplServerChangeAfkTimeoutEvent extends ImplServerEvent implements ServerChangeAfkTimeoutEvent {

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
    public ImplServerChangeAfkTimeoutEvent(Server server, int newAfkTimeout, int oldAfkTimeout) {
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
