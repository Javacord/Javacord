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
    
}
