package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.member.ServerMembersChunkEvent;
import org.javacord.core.event.server.ServerEventImpl;
import java.util.Collections;
import java.util.List;

/**
 * The implementation of {@link ServerMembersChunkEvent}.
 */
public class ServerMembersChunkEventImpl extends ServerEventImpl implements ServerMembersChunkEvent {

    private final List<Member> membersChunk;

    /**
     * Creates a new server members chunk event.
     *
     * @param server The server of the event.
     * @param membersChunk The chunk of members.
     */
    public ServerMembersChunkEventImpl(Server server, List<Member> membersChunk) {
        super(server);
        this.membersChunk = membersChunk;
    }

    @Override
    public List<Member> getMembers() {
        return Collections.unmodifiableList(membersChunk);
    }
}
