package org.javacord.api.event.server.member;

import java.util.Optional;

/**
 * A member change nickname event.
 */
public interface ServerMemberChangeNicknameEvent extends ServerMemberEvent {

    /**
     * Gets the new nickname of the member.
     *
     * @return The new nickname of the member.
     */
    Optional<String> getNewNickname();

    /**
     * Gets the old nickname of the member.
     *
     * @return The old nickname of the member.
     */
    Optional<String> getOldNickname();

}
