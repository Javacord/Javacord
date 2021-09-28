package org.javacord.core.event.channel.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.thread.ThreadListSyncEvent;
import org.javacord.core.event.server.ServerEventImpl;

import java.util.List;

public class ThreadListSyncEventImpl extends ServerEventImpl implements ThreadListSyncEvent {

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
    private final List<ServerThreadChannel> serverThreadChannels;

    /**
     * All thread member objects from the synced threads for the current user,
     * indicating which threads the current user has been added to.
     */
    private final List<ThreadMember> members;

    /**
     * Creates a Thread List Sync Event.
     *
     * @param server               The server to which this event belongs.
     * @param channelIds           The channel ids whose threads are being synced.
     * @param serverThreadChannels The active threads in the given channels that the current user can access.
     * @param members              All thread member objects from the synced threads for the current user.
     */
    public ThreadListSyncEventImpl(final Server server, final List<Long> channelIds,
                                   final List<ServerThreadChannel> serverThreadChannels,
                                   final List<ThreadMember> members) {
        super(server);

        this.channelIds = channelIds;
        this.serverThreadChannels = serverThreadChannels;
        this.members = members;
    }

    @Override
    public List<Long> getChannelIds() {
        return channelIds;
    }

    @Override
    public List<ServerThreadChannel> getServerThreadChannels() {
        return serverThreadChannels;
    }

    @Override
    public List<ThreadMember> getMembers() {
        return members;
    }
}
