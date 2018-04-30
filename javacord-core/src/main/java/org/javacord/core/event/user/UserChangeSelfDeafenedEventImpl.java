package org.javacord.core.event.user;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.user.UserChangeSelfDeafenedEvent;

/**
 * The implementation of {@link UserChangeSelfDeafenedEvent}.
 */
public class UserChangeSelfDeafenedEventImpl extends ServerUserEventImpl implements UserChangeSelfDeafenedEvent {

    /**
     * The new self-deafened state of the user.
     */
    private final boolean newSelfDeafened;

    /**
     * The old self-deafened state of the user.
     */
    private final boolean oldSelfDeafened;

    /**
     * Creates a new user change self-deafened event.
     *
     * @param userId The id of the user of the event.
     * @param server The server in which the self-deafened state of the user was changed.
     * @param newSelfDeafened The new self-deafened state of the user.
     * @param oldSelfDeafened The old self-deafened state of the user.
     */
    public UserChangeSelfDeafenedEventImpl(
            long userId, Server server, boolean newSelfDeafened, boolean oldSelfDeafened) {
        super(userId, server);
        this.newSelfDeafened = newSelfDeafened;
        this.oldSelfDeafened = oldSelfDeafened;
    }

    @Override
    public boolean isNewSelfDeafened() {
        return newSelfDeafened;
    }

    @Override
    public boolean isOldSelfDeafened() {
        return oldSelfDeafened;
    }

}
