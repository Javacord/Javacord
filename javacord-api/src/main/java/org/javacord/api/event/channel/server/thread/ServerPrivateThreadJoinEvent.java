package org.javacord.api.event.channel.server.thread;

import org.javacord.api.entity.channel.ThreadMember;

public interface ServerPrivateThreadJoinEvent extends ServerThreadChannelEvent {
    /**
     * Gets the thread members that joined the thread.
     *
     * @return The thread members that joined the thread.
     */
    ThreadMember getThreadMember();
}
