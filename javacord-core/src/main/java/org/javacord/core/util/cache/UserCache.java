package org.javacord.core.util.cache;

import org.javacord.api.entity.user.User;
import org.javacord.core.util.ImmutableToJavaMapper;

import java.util.Optional;
import java.util.Set;

/**
 * An immutable cache for all user entities.
 */
public class UserCache {

    private static final String ID_INDEX_NAME = "id";

    private static final UserCache EMPTY_CACHE = new UserCache(Cache.<User>empty()
            .addIndex(ID_INDEX_NAME, User::getId)
    );

    private final Cache<User> cache;

    private UserCache(Cache<User> cache) {
        this.cache = cache;
    }

    /**
     * Gets an empty channel cache.
     *
     * @return An empty channel cache.
     */
    public static UserCache empty() {
        return EMPTY_CACHE;
    }

    /**
     * Adds a user to the cache.
     *
     * @param user The user to add.
     * @return The new user cache.
     */
    public UserCache addUser(User user) {
        return new UserCache(cache.addElement(user));
    }

    /**
     * Removes a user from the cache.
     *
     * @param user The user to remove.
     * @return The new user cache.
     */
    public UserCache removeUser(User user) {
        return new UserCache(cache.removeElement(user));
    }

    /**
     * Gets a set with all channels in the cache.
     *
     * @return A set with all channels.
     */
    public Set<User> getUsers() {
        return ImmutableToJavaMapper.mapToJava(cache.getAll());
    }

    /**
     * Get the user with the given id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    public Optional<User> getUserById(long id) {
        return cache.findAnyByIndex(ID_INDEX_NAME, id);
    }

}
