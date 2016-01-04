package de.btobastian.javacord;

import de.btobastian.javacord.message.MessageReceiver;
import de.btobastian.javacord.permissions.Permissions;

/**
 * A discord channel.
 */
public interface Channel extends MessageReceiver {

    /**
     * Gets the name of the channel.
     * 
     * @return The name of the channel.
     */
    public String getName();
    
    /**
     * Gets the id of the channel.
     * 
     * @return The id of the channel.
     */
    public String getId();
    
    /**
     * Gets the topic of the channel.
     * 
     * @return The topic of the channel.
     */
    public String getTopic();
    
    /**
     * Gets the position of the channel.
     * 
     * @return The position of the channel.
     */
    public int getPosition();
    
    /**
     * Gets the server of the channel.
     * 
     * @return The server of the channel.
     */
    public Server getServer();
    
    /**
     * Deletes the channel.
     * 
     * @return Whether the deletion was successful or not.
     */
    public boolean delete();
    
    /**
     * Shows the "is typing.." status for 5 seconds.
     */
    public void type();
    
    /**
     * Gets the overridden permissions of the user.
     * 
     * @param user The user to check.
     * @return The overridden permissions.
     */
    public Permissions getOverriddenPermissions(User user);
    
    /**
     * Gets an invite builder.
     * 
     * @return An invite builder.
     */
    public InviteBuilder getInviteBuilder();
    
    /**
     * Updates the overridden permissions.
     * 
     * @param channel The user.
     * @param permissions The permissions to set.
     * @return Whether the channel could be updated or not.
     */
    public boolean updateOverriddenPermissions(User user, Permissions permissions);
    
    /**
     * Updates the name of the channel.
     * 
     * @param name The new name of the channel.
     * @return Whether the channel could be updated or not.
     */
    public boolean updateName(String name);
    
    /**
     * Updates the position of the channel.
     * 
     * @param position The new position of the channel.
     * @return Whether the channel could be updated or not.
     */
    public boolean updatePosition(int position);
    
    /**
     * Updates the topic of the channel.
     * 
     * @param topic The new topic of the channel.
     * @return Whether the channel could be updated or not.
     */
    public boolean updateTopic(String topic);
    
}
