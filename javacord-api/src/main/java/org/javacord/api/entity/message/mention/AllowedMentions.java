package org.javacord.api.entity.message.mention;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents a mention.
 */
public interface AllowedMentions {

    /**
     * Gets the allowed user mentions from the message.
     *
     * @return The allowed mentions for users of the message.
     */
    Optional<List<Long>> getAllowedRoleMentions();

    /**
     * Gets the allowed role mentions from the message.
     *
     * @return The allowed mentions for roles of the message.
     */
    Optional<List<Long>> getAllowedUserMentions();

    /**
     * Gets the allowed general mentions from the message.
     *
     * @return The general allowed mentions of the message.
     */
    Optional<List<String>> getGeneralMentions();

}