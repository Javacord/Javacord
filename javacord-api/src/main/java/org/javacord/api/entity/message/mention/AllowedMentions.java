package org.javacord.api.entity.message.mention;

import java.util.EnumSet;
import java.util.List;

/**
 * This interface represents a mention.
 */
public interface AllowedMentions {

    /**
     * Gets the explicitly allowed user mentions from the message.
     * This could differ from the actual mentioned roles if {@link AllowedMentionsBuilder#setMentionRoles(boolean)} has
     * been set to true.
     *
     * @return The explicitly allowed mentions for users of the message.
     */
    List<Long> getAllowedRoleMentions();

    /**
     * Gets the explicitly allowed role mentions from the message.
     * This could differ from the actual mentioned users if {@link AllowedMentionsBuilder#setMentionUsers(boolean)} has
     * been set to true.
     *
     * @return The explicitly allowed mentions for roles of the message.
     */
    List<Long> getAllowedUserMentions();

    /**
     * Gets the explicitly allowed mention types from the message.
     *
     * @return The allowed mention types of the message.
     */
    EnumSet<AllowedMentionType> getMentionTypes();

}