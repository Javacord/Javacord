package org.javacord.api.event.server.role;

/**
 * A role change mentionable event.
 */
public interface RoleChangeMentionableEvent extends RoleEvent {

    /**
     * Gets the old mentionable flag of the role.
     *
     * @return The old mentionable flag of the role.
     */
    boolean getOldMentionableFlag();

    /**
     * Gets the new mentionable flag of the role.
     *
     * @return The new mentionable flag of the role.
     */
    boolean getNewMentionableFlag();

}
