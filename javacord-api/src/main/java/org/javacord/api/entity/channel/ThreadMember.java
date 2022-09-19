package org.javacord.api.entity.channel;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ThreadMember extends DiscordEntity {

    /**
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    DiscordApi getApi();

    /**
     * Gets the server related to this thread member.
     *
     * @return The server of this thread member.
     */
    Server getServer();

    /**
     * The id of the user.
     *
     * @return The id of the user.
     */
    long getUserId();

    /**
     * Gets the user from the cache.
     *
     * @return The user if it is in the cache.
     */
    default Optional<User> getCachedUser() {
        return getApi().getCachedUserById(getUserId());
    }

    /**
     * Requests a user from Discord with the given id.
     *
     * <p>If the user is in the cache, the user is served from the cache.
     *
     * @return The user.
     */
    default CompletableFuture<User> requestUser() {
        return getApi().getUserById(getUserId());
    }

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
