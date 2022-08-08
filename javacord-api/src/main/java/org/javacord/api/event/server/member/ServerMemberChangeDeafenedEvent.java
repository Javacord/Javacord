package org.javacord.api.event.server.member;

import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user change deafened event.
 */
public interface ServerMemberChangeDeafenedEvent extends ServerMemberEvent {

    /**
     * Gets the new deafened state of the user.
     *
     * @return Whether the user is deafened now.
     */
    boolean isNewDeafened();

    /**
     * Gets the old deafened state of the user.
     *
     * @return Whether the user was deafened previously.
     */
    boolean isOldDeafened();

}
