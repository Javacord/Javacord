package org.javacord.api.entity.channel;

import java.time.Instant;

public interface ThreadMember {

    /**
     * The id of the thread.
     *
     * @return The id of the thread.
     */
    Long getId();

    /**
     * The id of the user.
     *
     * @return The id of the user.
     */
    Long getUserId();

    /**
     * The time the current user last joined the thread.
     *
     * @return The timestamp of the last time the current user joined the thread.
     */
    Instant getJoinTimestamp();

    /**
     * Any user-thread settings, currently only used for notifications.
     *
     * @return Any user-thread settings.
     */
    int getFlags();
}
