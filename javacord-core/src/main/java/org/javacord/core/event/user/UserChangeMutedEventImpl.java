package org.javacord.core.event.user;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.user.UserChangeMutedEvent;

/**
 * The implementation of {@link UserChangeMutedEvent}.
 */
public class UserChangeMutedEventImpl extends ServerUserEventImpl implements UserChangeMutedEvent {

    /**
     * The new muted state of the user.
     */
    private final boolean newMuted;

    /**
     * The old muted state of the user.
     */
    private final boolean oldMuted;

    /**
     * Creates a new user change muted event.
     *
     * @param userId The id of the user of the event.
     * @param server The server in which the muted state of the user was changed.
     * @param newMuted The new muted state of the user.
     * @param oldMuted The old muted state of the user.
     */
    public UserChangeMutedEventImpl(long userId, Server server, boolean newMuted, boolean oldMuted) {
        super(userId, server);
        this.newMuted = newMuted;
        this.oldMuted = oldMuted;
    }

    @Override
    public boolean isNewMuted() {
        return newMuted;
    }

    @Override
    public boolean isOldMuted() {
        return oldMuted;
    }

}
