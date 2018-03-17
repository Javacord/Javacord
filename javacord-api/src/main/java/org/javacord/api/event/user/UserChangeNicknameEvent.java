package org.javacord.api.event.user;

import org.javacord.api.event.server.ServerEvent;

import java.util.Optional;

/**
 * A user change nickname event.
 */
public interface UserChangeNicknameEvent extends UserEvent, ServerEvent {

    /**
     * Gets the new nickname of the user.
     *
     * @return The new nickname of the user.
     */
    Optional<String> getNewNickname();

    /**
     * Gets the old nickname of the user.
     *
     * @return The old nickname of the user.
     */
    Optional<String> getOldNickname();

}
