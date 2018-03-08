package org.javacord.event.server.impl;

import org.javacord.entity.server.ExplicitContentFilterLevel;
import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeExplicitContentFilterLevelEvent;

/**
 * The implementation of {@link ServerChangeExplicitContentFilterLevelEvent}.
 */
public class ImplServerChangeExplicitContentFilterLevelEvent extends ImplServerEvent
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
    public ImplServerChangeExplicitContentFilterLevelEvent(
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
