package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMembersChunkEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server members chunks.
 * These chunks can be requested from Discord for a server. Discord then responds with multiple chunks that combined
 * contain all members of the server.
 */
@FunctionalInterface
public interface ServerMembersChunkListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server members chunk is received.
     *
     * @param event The event.
     */
    void onServerMembersChunk(ServerMembersChunkEvent event);
}
