package org.javacord.core.event.user;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.user.UserChangeSelfMutedEvent;

/**
 * The implementation of {@link UserChangeSelfMutedEvent}.
 */
public class UserChangeSelfMutedEventImpl extends ServerUserEventImpl implements UserChangeSelfMutedEvent {

    /**
     * The new self-muted state of the user.
     */
    private final boolean newSelfMuted;

    /**
     * The old self-muted state of the user.
     */
    private final boolean oldSelfMuted;

    /**
     * Creates a new user change self-muted event.
     *
     * @param userId The id of the user of the event.
     * @param server The server in which the self-muted state of the user was changed.
     * @param newSelfMuted The new self-muted state of the user.
     * @param oldSelfMuted The old self-muted state of the user.
     */
    public UserChangeSelfMutedEventImpl(long userId, Server server, boolean newSelfMuted, boolean oldSelfMuted) {
        super(userId, server);
        this.newSelfMuted = newSelfMuted;
        this.oldSelfMuted = oldSelfMuted;
    }

    @Override
    public boolean isNewSelfMuted() {
        return newSelfMuted;
    }

    @Override
    public boolean isOldSelfMuted() {
        return oldSelfMuted;
    }

}
