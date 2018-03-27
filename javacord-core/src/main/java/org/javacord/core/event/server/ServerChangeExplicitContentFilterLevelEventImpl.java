package org.javacord.core.event.server;

import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeExplicitContentFilterLevelEvent;

/**
 * The implementation of {@link ServerChangeExplicitContentFilterLevelEvent}.
 */
public class ServerChangeExplicitContentFilterLevelEventImpl extends ServerEventImpl
        implements ServerChangeExplicitContentFilterLevelEvent {

    /**
     * The new explicit content filter level of the server.
     */
    private final ExplicitContentFilterLevel newExplicitContentFilterLevel;

    /**
     * The old explicit content filter level of the server.
     */
    private final ExplicitContentFilterLevel oldExplicitContentFilterLevel;

    /**
     * Creates a new server change explicit content filter level event.
     *
     * @param server The server of the event.
     * @param newExplicitContentFilterLevel The new explicit content filter level of the server.
     * @param oldExplicitContentFilterLevel The old explicit content filter level of the server.
     */
    public ServerChangeExplicitContentFilterLevelEventImpl(
            Server server, ExplicitContentFilterLevel newExplicitContentFilterLevel,
            ExplicitContentFilterLevel oldExplicitContentFilterLevel) {
        super(server);
        this.newExplicitContentFilterLevel = newExplicitContentFilterLevel;
        this.oldExplicitContentFilterLevel = oldExplicitContentFilterLevel;
    }

    @Override
    public ExplicitContentFilterLevel getOldExplicitContentFilterLevel() {
        return oldExplicitContentFilterLevel;
    }

    @Override
    public ExplicitContentFilterLevel getNewExplicitContentFilterLevel() {
        return newExplicitContentFilterLevel;
    }

}
