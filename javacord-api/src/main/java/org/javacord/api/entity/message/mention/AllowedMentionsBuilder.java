package org.javacord.api.entity.message.mention;

import org.javacord.api.entity.message.mention.internal.AllowedMentionsBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.Collection;

/**
 * This class is used to create mentions.
 */
public class AllowedMentionsBuilder {

    /**
     * The mention delegate used by this instance.
     */
    private final AllowedMentionsBuilderDelegate delegate = DelegateFactory.createAllowedMentionsBuilderDelegate();

    /**
     * Gets the delegate used by this mentions builder internally.
     *
     * @return The delegate used by this mention builder internally.
     */
    public AllowedMentionsBuilderDelegate getDelegate() {
        return delegate;
    }


    /**
     * Mentions all mentioned roles.
     * This will override any explicit role mentions added via {@link #addRole(long)} or {@link #addRoles(Collection)}
     *
     * @param value If roles should be mentioned or not.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder setMentionRoles(boolean value) {
        delegate.setMentionRoles(value);
        return this;
    }

    /**
     * Mentions all mentioned users.
     * This will override any explicit user mentions added via {@link #addUser(long)} or {@link #addUsers(Collection)}
     *
     * @param value If users should be mentioned or not.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder setMentionUsers(boolean value) {
        delegate.setMentionUsers(value);
        return this;
    }

    /**
     * Mentions @everyone and @here.
     *
     * @param value If @everyone and @here should be mentioned.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder setMentionEveryoneAndHere(boolean value) {
        delegate.setMentionEveryoneAndHere(value);
        return this;
    }

    /**
     * Adds a role to the list which will be mentioned.
     *
     * @param roleId The id of the role.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder addRole(String roleId) {
        delegate.addRole(roleId);
        return this;
    }

    /**
     * Adds a role to the list which will be mentioned.
     *
     * @param roleId The id of the role.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder addRole(long roleId) {
        delegate.addRole(roleId);
        return this;
    }

    /**
     * Adds the roles to the list which will be mentioned.
     *
     * @param roleIds A collection of role id's which will be mentioned.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder addRoles(Collection<Long> roleIds) {
        delegate.addRoles(roleIds);
        return this;
    }


    /**
     * Adds a user to the list which will be mentioned.
     *
     * @param userId The id of the user.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder addUser(String userId) {
        delegate.addUser(userId);
        return this;
    }

    /**
     * Adds a user to the list which will be mentioned.
     *
     * @param userId The id of the user.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder addUser(long userId) {
        delegate.addUser(userId);
        return this;
    }

    /**
     * Adds the users to the list which will be mentioned.
     *
     * @param userIds A collection of user id's which will be mentioned.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder addUsers(Collection<Long> userIds) {
        delegate.addUsers(userIds);
        return this;
    }


    /**
     * Removes a role from the list which will be mentioned.
     *
     * @param roleId The id of the role.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder removeRole(String roleId) {
        delegate.removeRole(roleId);
        return this;
    }

    /**
     * Removes a role from the list which will be mentioned.
     *
     * @param roleId The id of the role.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder removeRole(long roleId) {
        delegate.removeRole(roleId);
        return this;
    }

    /**
     * Removes the roles from the list which will be mentioned.
     *
     * @param roleIds A collection of role id's which will be removed from the list.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder removeRoles(Collection<Long> roleIds) {
        delegate.removeRoles(roleIds);
        return this;
    }

    /**
     * Removes an user from the list which will be mentioned.
     *
     * @param userId The id of the user.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder removeUser(String userId) {
        delegate.removeUser(userId);
        return this;
    }

    /**
     * Removes an user from the list which will be mentioned.
     *
     * @param userId The id of the user.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder removeUser(long userId) {
        delegate.removeUser(userId);
        return this;
    }

    /**
     * Removes the users from the list which will be mentioned.
     *
     * @param userIds A collection of user id's which will be removed from the list.
     * @return The current instance in order to chain call methods.
     */
    public AllowedMentionsBuilder removeUsers(Collection<Long> userIds) {
        delegate.removeUsers(userIds);
        return this;
    }

    /**
     * Creates a {@link AllowedMentions} instance with the given values.
     *
     * @return The created permissions instance.
     */
    public AllowedMentions build() {
        return delegate.build();
    }

}