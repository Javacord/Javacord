package org.javacord.api.entity.server;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import java.util.List;

public interface ActiveThreads {

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
}
