package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.server.thread.ServerPrivateThreadJoinEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

/**
 * The implementation of {@link ServerPrivateThreadJoinEvent}.
 */
public class ServerPrivateThreadJoinEventImpl extends ServerThreadChannelEventImpl
        implements ServerPrivateThreadJoinEvent {
    /**
     * The new members.
     */
    private final ThreadMember threadMember;

    /**
     * Creates a new instance of this class.
     *
     * @param channel The channel.
     * @param threadMember The new members.
     */
    public ServerPrivateThreadJoinEventImpl(ServerThreadChannelImpl channel, ThreadMember threadMember) {
        super(channel);
        this.threadMember = threadMember;
    }

    @Override
    public ThreadMember getThreadMember() {
        return threadMember;
    }
}
