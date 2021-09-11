package org.javacord.core.event.channel.thread;

import org.javacord.api.entity.channel.ChannelThread;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.thread.ThreadListSyncEvent;
import org.javacord.core.event.server.ServerEventImpl;

import java.util.List;

public class ThreadListSyncEventImpl extends ServerEventImpl implements ThreadListSyncEvent {

    /**
     * The server.
     */
    private final Server server;

    /**
     * The parent channel ids whose threads are being synced.
     * If omitted, then threads were synced for the entire guild.
     * This array may contain channel_ids that have no active threads as well,
     * so you know to clear that data.
     */
    private final List<Long> channelIds;

    /**
     * All active threads in the given channels that the current user can access.
     */
    private final List<ChannelThread> threads;

    /**
     * All thread member objects from the synced threads for the current user,
     * indicating which threads the current user has been added to.
     */
    private final List<ThreadMember> members;

    /**
     * Creates a Thread List Sync Event.
     *
     * @param server The server to which this event belongs.
     * @param channelIds The channel ids whose threads are being synced.
     * @param threads The active threads in the given channels that the current user can access.
     * @param members All thread member objects from the synced threads for the current user.
     */
    public ThreadListSyncEventImpl(Server server, List<Long> channelIds, List<ChannelThread> threads,
                                    List<ThreadMember> members) {
        super(server);

        this.server = server;
        this.channelIds = channelIds;
        this.threads = threads;
        this.members = members;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public List<Long> getChannelIds() {
        return channelIds;
    }

    @Override
    public List<ChannelThread> getThreads() {
        return threads;
    }

    @Override
    public List<ThreadMember> getMembers() {
        return members;
    }
}
