package org.javacord.core.event.channel.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.thread.ThreadMembersUpdateEvent;
import org.javacord.core.event.server.ServerEventImpl;

import java.util.List;

public class ThreadMembersUpdateEventImpl extends ServerEventImpl implements ThreadMembersUpdateEvent {

    /**
     * The thread of the members update.
     */
    private final ServerThreadChannel serverThreadChannel;

    /**
     * The approximate number of members in the thread, capped at 50.
     */
    private final int memberCount;

    /**
     * The users who were added to the thread.
     */
    private final List<ThreadMember> addedMembers;

    /**
     * The ids of the users who were removed from the thread.
     */
    private final List<Long> removedMemberIds;

    /**
     * Creates a Thread Members Update Event.
     *
     * @param serverThreadChannel The thread.
     * @param server              The server to which this event belongs.
     * @param memberCount         Approximate number of members in the thread.
     * @param addedMembers        The users who were added to the thread.
     * @param removedMemberIds    The ids of users who were removed from the thread.
     */
    public ThreadMembersUpdateEventImpl(final ServerThreadChannel serverThreadChannel, final Server server,
                                        final int memberCount, final List<ThreadMember> addedMembers,
                                        final List<Long> removedMemberIds) {
        super(server);
        this.serverThreadChannel = serverThreadChannel;
        this.memberCount = memberCount;
        this.addedMembers = addedMembers;
        this.removedMemberIds = removedMemberIds;
    }

    @Override
    public ServerThreadChannel getThread() {
        return serverThreadChannel;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public List<ThreadMember> getAddedMembers() {
        return addedMembers;
    }

    @Override
    public List<Long> getRemovedMemberIds() {
        return removedMemberIds;
    }

}
