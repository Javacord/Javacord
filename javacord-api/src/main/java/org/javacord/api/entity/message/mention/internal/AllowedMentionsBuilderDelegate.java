package org.javacord.api.entity.message.mention.internal;

import org.javacord.api.entity.message.mention.AllowedMentions;

import java.util.Collection;

public interface AllowedMentionsBuilderDelegate {
    /**
     * Sets the general mentions of the message.
     *
     * @param value Whether the message mentions @everyone and @here.
     */
    void setMentionEveryoneAndHere(boolean value);

    /**
     * Sets the general mentions of the message.
     *
     * @param value Whether the message mentions all mentioned roles.
     */
    void setMentionRoles(boolean value);

    /**
     * Sets the general mentions of the message.
     *
     * @param value Whether the message mentions all mentioned users.
     */
    void setMentionUsers(boolean value);

    /**
     * Adds an user to the mentions list which should be mentioned if mentioned.
     *
     * @param userId Whether the message mentions the mentioned user.
     */
    void addUser(long userId);

    /**
     * Adds an user to the mentions list which should be mentioned if mentioned.
     *
     * @param userId Whether the message mentions the mentioned user.
     */
    void addUser(String userId);

    /**
     * Adds a collection of user id's to the mentions list which should be mentioned if mentioned.
     *
     * @param userIds Whether the message mentions the mentioned users.
     */
    void addUsers(Collection<Long> userIds);

    /**
     * Adds a role to the mentions list which should be mentioned if mentioned.
     *
     * @param roleId Whether the message mentions the mentioned roles.
     */
    void addRole(long roleId);

    /**
     * Adds a role to the mentions list which should be mentioned if mentioned.
     *
     * @param roleId Whether the message mentions the mentioned roles.
     */
    void addRole(String roleId);

    /**
     * Adds a collection of roles to the mentions list which should be mentioned if mentioned.
     *
     * @param roleIds Whether the message mentions the mentioned roles.
     */
    void addRoles(Collection<Long> roleIds);

    /**
     * Removes an user from the mentions list if previously added.
     *
     * @param userId The id of the user which should be removed from the list.
     */
    void removeUser(long userId);

    /**
     * Removes an user from the mentions list if previously added.
     *
     * @param userId The id of the user which should be removed from the list.
     */
    void removeUser(String userId);

    /**
     * Removes a role from the mentions list if previously added.
     *
     * @param roleId The id of the role which should be removed from the list.
     */
    void removeRole(long roleId);

    /**
     * Removes a role from the mentions list if previously added.
     *
     * @param roleId The id of the role which should be removed from the list.
     */
    void removeRole(String roleId);

    /**
     * Removes a collection of user id's from the mentions list if previously added.
     *
     * @param userIds A collection of user id's which should be removed from the list.
     */
    void removeUsers(Collection<Long> userIds);

    /**
     * Removes a collection of role id's from the mentions list if previously added.
     *
     * @param roleIds A collection of role id's which should be removed from the list.
     */
    void removeRoles(Collection<Long> roleIds);

    /**
     * Creates a {@link AllowedMentions} instance with the given values.
     *
     * @return The created permissions instance.
     */
    AllowedMentions build();
}