package org.javacord.api.entity.server;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import java.util.List;
import java.util.Set;

public interface ArchivedThreads {

    /**
     * Gets a list with the active threads.
     *
     * @return A list containing the active Server Thread Channels.
     */
    List<ServerThreadChannel> getServerThreadChannels();

    /**
     * A set with the thread member objects for each returned thread the current user has joined.
     *
     * @return A list containing the thread members.
     */
    Set<ThreadMember> getThreadMembers();

    /**
     * Whether there are more threads.
     *
     * @return whether there are potentially additional threads that could be returned on a subsequent call
     */
    boolean hasMoreThreads();
}
