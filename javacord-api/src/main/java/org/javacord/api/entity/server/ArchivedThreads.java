package org.javacord.api.entity.server;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import java.util.List;

public interface ArchivedThreads {

    /**
     * Gets the active threads.
     *
     * @return The active Server Thread Channels.
     */
    List<ServerThreadChannel> getServerThreadChannels();

    /**
     * A thread member object for each returned thread the current user has joined.
     *
     * @return The thread members.
     */
    List<ThreadMember> getThreadMembers();

    /**
     * Whether there are more threads.
     *
     * @return whether there are potentially additional threads that could be returned on a subsequent call
     */
    boolean hasMoreThreads();
}
