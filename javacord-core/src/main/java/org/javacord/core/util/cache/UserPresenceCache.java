package org.javacord.core.util.cache;

import org.javacord.core.entity.user.UserPresence;

import java.util.Optional;

/**
 * An immutable cache for all user presences.
 */
public class UserPresenceCache {

    private static final String USER_ID_INDEX_NAME = "user-id";

    private static final UserPresenceCache EMPTY_CACHE = new UserPresenceCache(Cache.<UserPresence>empty()
            .addIndex(USER_ID_INDEX_NAME, UserPresence::getUserId)
    );

    private final Cache<UserPresence> cache;

    private UserPresenceCache(Cache<UserPresence> cache) {
        this.cache = cache;
    }

    /**
     * Gets an empty user presence cache.
     *
     * @return An empty user presence cache.
     */
    public static UserPresenceCache empty() {
        return EMPTY_CACHE;
    }

    /**
     * Adds a user presence to the cache.
     *
     * @param presence The user presence to add.
     * @return The new user presence cache.
     */
    public UserPresenceCache addUserPresence(UserPresence presence) {
        return new UserPresenceCache(cache.addElement(presence));
    }

    /**
     * Removes a user presence from the cache.
     *
     * @param presence The user presence to remove.
     * @return The new user presence cache.
     */
    public UserPresenceCache removeUserPresence(UserPresence presence) {
        if (presence == null) {
            return this;
        }
        return new UserPresenceCache(cache.removeElement(presence));
    }

    /**
     * Get the presence for the user with the given id.
     *
     * @param userId The id of the user.
     * @return The presence for the user with the given id.
     */
    public Optional<UserPresence> getPresenceByUserId(long userId) {
        return cache.findAnyByIndex(USER_ID_INDEX_NAME, userId);
    }

}
