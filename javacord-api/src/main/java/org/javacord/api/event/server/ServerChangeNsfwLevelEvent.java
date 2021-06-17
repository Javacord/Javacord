package org.javacord.api.event.server;

import org.javacord.api.entity.server.NsfwLevel;

public interface ServerChangeNsfwLevelEvent extends ServerEvent {

    /**
     * The old NSFW level of the server.
     *
     * @return the old NSFW level of the server.
     */
    NsfwLevel getOldNsfwLevel();

    /**
     * The new NSFW level of the server.
     *
     * @return the new NSFW level of the server.
     */
    NsfwLevel getNewNsfwLevel();

}
