package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.server.thread.ServerPrivateThreadJoinEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

import java.util.Set;

/**
 * The implementation of {@link ServerPrivateThreadJoinEvent}.
 */
public class ServerPrivateThreadJoinEventImpl extends ServerThreadChannelEventImpl
        implements ServerPrivateThreadJoinEvent {
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
    public ServerPrivateThreadJoinEventImpl(ServerThreadChannelImpl channel,
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
