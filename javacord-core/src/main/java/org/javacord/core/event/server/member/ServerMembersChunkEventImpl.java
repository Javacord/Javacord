package org.javacord.core.event.server.member;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMembersChunkEvent;
import org.javacord.core.event.server.ServerEventImpl;

import java.util.Collections;
import java.util.List;

/**
 * The implementation of {@link ServerMembersChunkEvent}.
 */
public class ServerMembersChunkEventImpl extends ServerEventImpl implements ServerMembersChunkEvent {

    private final List<User> membersChunk;

    /**
     * Creates a new server member join event.
     *
     * @param server The server of the event.
     * @param membersChunk The chunk of members.
     */
    public ServerMembersChunkEventImpl(Server server, List<User> membersChunk) {
        super(server);
        this.membersChunk = membersChunk;
    }

    @Override
    public List<User> getMembersChunk() {
        return Collections.unmodifiableList(membersChunk);
    }
}
