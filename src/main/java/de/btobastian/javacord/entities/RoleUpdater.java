package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.permissions.Role;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * A builder to update many of a user's roles simultaneously.
 */
public class RoleUpdater {

    /**
     * The server for the role changes to take place on.
     */
    private Server server;

    /**
     * The user to update the roles of.
     */
    private User user;

    /**
     * A collection of the roles changed by this builder.
     * The user's roles will be changed to this upon calling {@link #build()}
     */
    private Collection<Role> roles;

    /**
     * A constructor to generate this builder in order to update a user's roles.
     *
     * @param server The server for the role changes to take place on.
     * @param user The user to update the roles of.
     */
    RoleUpdater(Server server, User user){
        this.server = server;
        this.user = user;
        this.roles = server.getRolesOf(user);
    }

    /**
     * Queues a role to be added to the user.
     *
     * @param role The role to be added.
     * @return This builder, allowing for chaining.
     */
    public RoleUpdater addRole(Role role){
        roles.add(role);
        return this;
    }

    /**
     * Queues a collection of roles to be added to a user.
     *
     * @param roles The collection of roles to be added.
     * @return This builder, allowing for chaining.
     */
    public RoleUpdater addAllRoles(Collection<Role> roles){
        roles.forEach(this::addRole);
        return this;
    }

    /**
     * Queues a role to be removed from the user.
     *
     * @param role The role to be removed.
     * @return This builder, allowing for chaining.
     */
    public RoleUpdater removeRole(Role role){
        roles.remove(role);
        return this;
    }

    /**
     * Queues a collection of roles to be removed a user.
     *
     * @param roles The collection of roles to be removed.
     * @return This builder, allowing for chaining.
     */
    public RoleUpdater removeAllRoles(Collection<Role> roles){
        roles.forEach(this::removeRole);
        return this;
    }

    /**
     * Performs the changes queued by {@link #addRole(Role)} and {@link #removeRole(Role)}
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> build(){
        return server.updateRoles(user, roles);
    }
}
