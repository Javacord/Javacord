package de.btobastian.javacord.api;

import java.util.List;

/**
 * A discord server. Also called a guild.
 */
public interface Server {

    /**
     * Gets the name of the server.
     * 
     * @return The name of the server.
     */
    public String getName();
    
    /**
     * Gets the id of the server.
     * 
     * @return The id of the server.
     */
    public String getId();
    
    /**
     * Gets the owner of the server.
     * 
     * @return The owner of the server.
     */
    public User getOwner();
    
    /**
     * Gets a list with all channels.
     * 
     * @return A list with all channels.
     */
    public List<Channel> getChannels();
    
    /**
     * Creates a new channel.
     * 
     * @param name The name of the channel.
     * @param voice Whether the channel should be voice or text.
     * @return The created channel. <code>Null</code> if the channel couldn't be created.
     */
    public Channel createChannel(String name, boolean voice);
    
    /**
     * Deletes or leaves the server.
     */
    public void deleteOrLeave();
    
    /**
     * Kicks the given user.
     * 
     * @param user The user to kick.
     * @return Whether the user could be kicked or not.
     */
    public boolean kick(User user);
    
}
