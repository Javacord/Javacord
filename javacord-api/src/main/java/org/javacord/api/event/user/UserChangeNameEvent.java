package org.javacord.api.event.user;

import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user change name event.
 */
public interface UserChangeNameEvent extends ServerMemberEvent {

    /**
     * Gets the new name of the user.
     *
     * @return The new name of the user.
     */
    String getNewName();

    /**
     * Gets the old name of the user.
     *
     * @return The old name of the user.
     */
    String getOldName();

}
