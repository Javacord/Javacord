package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMemberCountEvent;

/**
 * The implementation of {@link ServerThreadChannelChangeMemberCountEvent}.
 */
public class ServerThreadChannelChangeMemberCountEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeMemberCountEvent {
    /**
     * The new count of members in this thread.
     */
    private final int newMemberCount;
    /**
     * The old count of members in this thread.
     */
    private final int oldMemberCount;

    /**
     * Creates a new server text channel member count update event.
     *
     * @param channel The channel of the event.
     * @param newMemberCount The new count of members in this thread.
     * @param oldMemberCount The old count of members in this thread.
     */
    public ServerThreadChannelChangeMemberCountEventImpl(ServerThreadChannel channel, int newMemberCount,
                                                         int oldMemberCount) {
        super(channel);
        this.newMemberCount = newMemberCount;
        this.oldMemberCount = oldMemberCount;
    }

    @Override
    public int getNewMemberCount() {
        return newMemberCount;
    }

    @Override
    public int getOldMemberCount() {
        return oldMemberCount;
    }
}
