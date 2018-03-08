package org.javacord.event.server.impl;

import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeNameEvent;

/**
 * The implementation of {@link ServerChangeNameEvent}.
 */
public class ImplServerChangeNameEvent extends ImplServerEvent implements ServerChangeNameEvent {

    /**
     * The new name of the server.
     */
    private final String newName;

    /**
     * The old name of the server.
     */
    private final String oldName;

    /**
     * Creates a new server change name event.
     *
     * @param server The server of the event.
     * @param newName The new name of the server.
     * @param oldName The old name of the server.
     */
    public ImplServerChangeNameEvent(Server server, String newName, String oldName) {
        super(server);
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public String getOldName() {
        return oldName;
    }

    @Override
    public String getNewName() {
        return newName;
    }

}
