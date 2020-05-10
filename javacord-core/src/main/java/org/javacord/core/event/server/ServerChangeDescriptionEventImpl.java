package org.javacord.core.event.server;

import org.javacord.api.event.server.ServerChangeDescriptionEvent;
import org.javacord.core.entity.server.ServerImpl;

import java.util.Optional;

/**
 * The implementation of {@link ServerChangeDescriptionEvent}.
 */
public class ServerChangeDescriptionEventImpl extends ServerEventImpl implements ServerChangeDescriptionEvent {

    /**
     * The old description of the server.
     */
    private final String oldDescription;

    /**
     * The new description of the server.
     */
    private final String newDescription;

    /**
     * Creates a new description change event.
     *
     * @param server The server of the event.
     * @param newDescription The new description of the server.
     * @param oldDescription The old description of the server.
     */
    public ServerChangeDescriptionEventImpl(ServerImpl server, String newDescription, String oldDescription) {
        super(server);
        this.newDescription = newDescription;
        this.oldDescription = oldDescription;
    }

    @Override
    public Optional<String> getOldDescription() {
        return Optional.ofNullable(oldDescription);
    }

    @Override
    public Optional<String> getNewDescription() {
        return Optional.ofNullable(newDescription);
    }
}
