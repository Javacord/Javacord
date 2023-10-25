package org.javacord.api.interaction;

import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.user.User;
import java.util.Optional;

public interface UserContextMenuInteraction extends ApplicationCommandInteraction {

    /**
     * Gets the target id.
     *
     * @return The target id.
     */
    long getTargetId();

    /**
     * Gets the target id.
     *
     * @return The target id.
     */
    default String getTargetIdAsString() {
        return String.valueOf(getTargetId());
    }

    /**
     * Gets the target user.
     *
     * @return The target user.
     */
    User getTarget();

    /**
     * Gets the target member.
     *
     * @return The target member.
     */
    Optional<Member> getTargetMember();

}
