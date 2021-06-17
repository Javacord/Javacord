package org.javacord.core.event.server;

import org.javacord.api.entity.server.NsfwLevel;
import org.javacord.api.event.server.ServerChangeNsfwLevelEvent;
import org.javacord.core.entity.server.ServerImpl;

/**
 * The implementation of {@link ServerChangeNsfwLevelEvent}.
 */
public class ServerChangeNsfwLevelEventImpl extends ServerEventImpl implements ServerChangeNsfwLevelEvent {

    /**
     * The old NSFW level of the server.
     */
    private final NsfwLevel oldNsfwLevel;

    /**
     * The new NSFW level of the server.
     */
    private final NsfwLevel newNsfwLevel;

    /**
     * Creates a new NSFW level change event.
     * @param server The server of the event.
     * @param newNsfwLevel The new NSFW level of the server.
     * @param oldNsfwLevel The old NSFW level of the server.
     */
    public ServerChangeNsfwLevelEventImpl(ServerImpl server, NsfwLevel newNsfwLevel, NsfwLevel oldNsfwLevel) {
        super(server);
        this.oldNsfwLevel = oldNsfwLevel;
        this.newNsfwLevel = newNsfwLevel;
    }

    @Override
    public NsfwLevel getOldNsfwLevel() {
        return oldNsfwLevel;
    }

    @Override
    public NsfwLevel getNewNsfwLevel() {
        return newNsfwLevel;
    }
}
