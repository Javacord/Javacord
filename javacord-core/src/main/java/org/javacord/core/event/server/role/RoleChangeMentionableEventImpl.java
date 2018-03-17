package org.javacord.core.event.server.role;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.RoleChangeMentionableEvent;

/**
 * The implementation of {@link RoleChangeMentionableEvent}.
 */
public class RoleChangeMentionableEventImpl extends RoleEventImpl implements RoleChangeMentionableEvent {

    /**
     * The old mentionable flag value.
     */
    private final boolean oldMentionable;

    /**
     * Creates a new role change mentionable event.
     *
     * @param role The role of the event.
     * @param oldMentionable The old mentionable flag of the role.
     */
    public RoleChangeMentionableEventImpl(Role role, boolean oldMentionable) {
        super(role);
        this.oldMentionable = oldMentionable;
    }

    @Override
    public boolean getOldMentionableFlag() {
        return oldMentionable;
    }

    @Override
    public boolean getNewMentionableFlag() {
        return !oldMentionable;
    }
}
