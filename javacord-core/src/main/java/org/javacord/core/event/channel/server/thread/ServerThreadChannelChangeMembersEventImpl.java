package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMembersEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

import java.util.Set;

/**
 * The implementation of {@link ServerThreadChannelChangeMembersEvent}.
 */
public class ServerThreadChannelChangeMembersEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeMembersEvent {
    /**
     * The new members.
     */
    private final Set<ThreadMember> newMembers;

    /**
     * The old members.
     */
    private final Set<ThreadMember> oldMembers;

    /**
     * Creates a new instance of this class.
     *
     * @param channel The channel.
     * @param newMembers The new members.
     * @param oldMembers The old members.
     */
    public ServerThreadChannelChangeMembersEventImpl(ServerThreadChannelImpl channel,
                                                     Set<ThreadMember> newMembers, Set<ThreadMember> oldMembers) {
        super(channel);
        this.newMembers = newMembers;
        this.oldMembers = oldMembers;
    }

    @Override
    public Set<ThreadMember> getNewMembers() {
        return newMembers;
    }

    @Override
    public Set<ThreadMember> getOldMembers() {
        return oldMembers;
    }
}
