package org.javacord.core.event.user;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.user.OptionalUserEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link OptionalUserEvent}.
 */
public abstract class OptionalUserEventImpl extends EventImpl implements OptionalUserEvent {

    /**
     * The id of the user of the event.
     */
    private final long userId;

    /**
     * Creates a new optional user event.
     *
     * @param api The discord api instance.
     * @param userId The id of the user of the event.
     */
    public OptionalUserEventImpl(DiscordApi api, long userId) {
        super(api);
        this.userId = userId;
    }

    @Override
    public long getUserId() {
        return userId;
    }
}
