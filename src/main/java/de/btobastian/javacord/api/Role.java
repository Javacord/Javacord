package de.btobastian.javacord.api;

import java.util.List;

import de.btobastian.javacord.api.permissions.Permissions;

/**
 * A discord role, e.g. "moderator".
 */
public interface Role {

    /**
     * Gets the id of the role.
     * 
     * @return The id of the role.
     */
    public String getId();
    
    /**
     * Gets the name of the role.
     * 
     * @return The name of the role.
     */
    public String getName();
    
    /**
     * Gets the server of this role.
     * 
     * @return The server of the role.
     */
    public Server getServer();
    
    /**
     * Gets the permissions of this role.
     * 
     * @return The permissions of this role.
     */
    public Permissions getPermission();
    
    /**
     * Gets the overridden permissions in the given channel.
     * 
     * @param channel The channel to check.
     * @return The overridden permissions.
     */
    public Permissions getOverriddenPermissions(Channel channel);
    
    /**
     * Gets all users with this role.
     * 
     * @return All users with this role.
     */
    public List<User> getUsers();
    
}
