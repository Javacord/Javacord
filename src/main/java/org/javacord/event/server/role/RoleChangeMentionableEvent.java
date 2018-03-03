package org.javacord.event.server.role;

import org.javacord.DiscordApi;
import org.javacord.entity.permission.Role;

/**
 * A role change mentionable event.
 */
public class RoleChangeMentionableEvent extends RoleEvent {

    /**
     * The old mentionable flag value.
     */
    private final boolean oldMentionable;

    /**
     * Creates a new role change mentionable event.
     *
     * @param api The api instance of the event.
     * @param role The role of the event.
     * @param oldMentionable The old mentionable flag of the role.
     */
    public RoleChangeMentionableEvent(DiscordApi api, Role role, boolean oldMentionable) {
        super(api, role);
        this.oldMentionable = oldMentionable;
    }

    /**
     * Gets the old mentionable flag of the role.
     *
     * @return The old mentionable flag of the role.
     */
    public boolean getOldMentionableFlag() {
        return oldMentionable;
    }

    /**
     * Gets the new mentionable flag of the role.
     *
     * @return The new mentionable flag of the role.
     */
    public boolean getNewMentionableFlag() {
        return !oldMentionable;
    }
}
