package org.javacord.api.event.channel.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.server.ServerEvent;
import java.util.List;

public interface ThreadListSyncEvent extends ServerEvent {

    /**
     * The parent channel ids whose threads are being synced.
     *
     * @return The parent channel ids whose threads are being synced.
     *      If omitted, then threads were synced for the entire guild.
     *      This array may contain channel_ids that have no active threads as well,
     *      so you know to clear that data.
     */
    List<Long> getChannelIds();

    /**
     * All active threads in the given channels that the current user can access.
     *
     * @return The active threads.
     */
    List<ServerThreadChannel> getServerThreadChannels();

    /**
     * All thread member objects from the synced threads for the current user,
     *  indicating which threads the current user has been added to.
     *
     * @return All thread member objects from the synced threads for the current user.
     */
    List<ThreadMember> getMembers();
}
