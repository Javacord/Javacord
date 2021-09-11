package org.javacord.api.event.channel.thread;

import org.javacord.api.entity.channel.ChannelThread;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerEvent;

import java.util.List;

public interface ThreadMembersUpdateEvent extends ServerEvent {

    /**
     * The id of the thread.
     *
     * @return The id of the thread.
     */
    public ChannelThread getThread();

    /**
     * The server to which this event belongs.
     *
     * @return The server.
     */
    public Server getServer();

    /**
     * The approximate number of members in the thread, capped at 50.
     *
     * @return The approximate number of members.
     */
    public int getMemberCount();

    /**
     * The users who were added to the thread.
     *
     * @return The users as ThreadMembers.
     */
    public List<ThreadMember> getAddedMembers();

    /**
     * The ids of the users who were removed from the thread.
     *
     * @return The ids.
     */
    public List<Long> getRemovedMemberIds();
}
