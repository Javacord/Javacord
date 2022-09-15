package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeMemberCountEvent extends ServerThreadChannelEvent {

    /**
     * Gets the new member count.
     *
     * @return The new member count.
     */
    int getNewMemberCount();

    /**
     * Gets the old member count.
     *
     * @return The old member count.
     */
    int getOldMemberCount();
}
