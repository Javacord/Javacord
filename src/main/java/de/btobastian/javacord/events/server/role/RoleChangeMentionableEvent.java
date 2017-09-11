package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;

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
     * @param server The server of the event.
     * @param role The role of the event.
     * @param oldMentionable The old mentionable flag of the role.
     */
    public RoleChangeMentionableEvent(DiscordApi api, Server server, Role role, boolean oldMentionable) {
        super(api, server, role);
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
