package org.javacord.api.event.channel.server.thread;

import org.javacord.api.entity.channel.ThreadMember;

import java.util.Set;

public interface ServerPrivateThreadJoinEvent extends ServerThreadChannelEvent {

    /**
     * Gets the new member count.
     *
     * @return The new member count.
     */
    Set<ThreadMember> getNewMembers();

    /**
     * Gets the old member count.
     *
     * @return The old member count.
     */
    Set<ThreadMember> getOldMembers();
}
