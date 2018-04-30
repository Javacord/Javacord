package org.javacord.core.event.user;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.TextChannelUserEvent;
import org.javacord.api.event.user.UserEvent;
import org.javacord.core.event.server.ServerEventImpl;

import java.util.function.Supplier;

/**
 * The implementation of {@link TextChannelUserEvent}.
 */
public abstract class ServerUserEventImpl extends ServerEventImpl implements UserEvent {

    /**
     * The supplier for the user of the event.
     */
    private final Supplier<User> userSupplier;

    /**
     * Creates a new server user event.
     *
     * @param user The user of the event.
     * @param server The server of the event.
     */
    public ServerUserEventImpl(User user, Server server) {
        super(server);
        this.userSupplier = () -> user;
    }

    /**
     * Creates a new server user event.
     *
     * @param userId The id of the user of the event.
     * @param server The server of the event.
     */
    public ServerUserEventImpl(long userId, Server server) {
        super(server);
        // server related events should only get dispatched after all members are cached
        this.userSupplier = () -> getApi().getCachedUserById(userId).orElseThrow(AssertionError::new);
    }

    @Override
    public User getUser() {
        return userSupplier.get();
    }

}
