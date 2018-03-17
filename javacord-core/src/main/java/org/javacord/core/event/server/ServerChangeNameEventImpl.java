package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeNameEvent;

/**
 * The implementation of {@link ServerChangeNameEvent}.
 */
public class ServerChangeNameEventImpl extends ServerEventImpl implements ServerChangeNameEvent {

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
    public ServerChangeNameEventImpl(Server server, String newName, String oldName) {
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
