package org.javacord.core.event.user;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.user.UserChangeDeafenedEvent;

/**
 * The implementation of {@link UserChangeDeafenedEvent}.
 */
public class UserChangeDeafenedEventImpl extends ServerUserEventImpl implements UserChangeDeafenedEvent {

    /**
     * The new deafened state of the user.
     */
    private final boolean newDeafened;

    /**
     * The old deafened state of the user.
     */
    private final boolean oldDeafened;

    /**
     * Creates a new user change deafened event.
     *
     * @param userId The id of the user of the event.
     * @param server The server in which the deafened state of the user was changed.
     * @param newDeafened The new deafened state of the user.
     * @param oldDeafened The old deafened state of the user.
     */
    public UserChangeDeafenedEventImpl(long userId, Server server, boolean newDeafened, boolean oldDeafened) {
        super(userId, server);
        this.newDeafened = newDeafened;
        this.oldDeafened = oldDeafened;
    }

    @Override
    public boolean isNewDeafened() {
        return newDeafened;
    }

    @Override
    public boolean isOldDeafened() {
        return oldDeafened;
    }

}
