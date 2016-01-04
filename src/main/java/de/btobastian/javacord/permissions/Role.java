package de.btobastian.javacord.permissions;

import java.awt.Color;
import java.util.List;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;

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
    
    /**
     * Gets the position of the role.
     * 
     * @return The position of the role.
     */
    public int getPosition();
    
    /**
     * Updates the name of the role.
     * 
     * @param name The new name.
     * @return Whether the role could be updated or not.
     */
    public boolean updateName(String name);
    
    /**
     * Gets if the role's users should be displayed separately.
     * 
     * @return Whether to display the role's users separately or not.
     */
    public boolean getHoist();
    
    /**
     * Update the hoist (Whether to display the role's users separately or not).
     * 
     * @param hoist The new hoist.
     * @return Whether the role could be updated or not.
     */
    public boolean updateHoist(boolean hoist);
    
    /**
     * Gets the color of the role.
     * 
     * @return The color of the role.
     */
    public Color getColor();
    
    /**
     * Updates the color of the role.
     * 
     * @param color The new color.
     * @return Whether the role could be updated or not.
     */
    public boolean updateColor(Color color);
    
    /**
     * Updates the permissions of the role.
     * If a status if not set it will be converted to {@link PermissionState#DENIED}.
     * 
     * @param permissions The permissions to set.
     * @return Whether the role could be updated or not.
     */
    public boolean updatePermissions(Permissions permissions);
    
    /**
     * Adds an user to the role.
     * 
     * @param user The user to add.
     * @return Whether the user could be added or not.
     */
    public boolean addUser(User user);
    
    /**
     * Removes an user from the role.
     * 
     * @param user The user to remove.
     * @return Whether the user could be removed or not.
     */
    public boolean removeUser(User user);
    
    /**
     * Updates the overridden permissions.
     * 
     * @param channel The channel.
     * @param permissions The permissions to set.
     * @return Whether the role could be updated or not.
     */
    public boolean updateOverriddenPermissions(Channel channel, Permissions permissions);
    
}
