package de.btobastian.javacord;

/**
 * A discord voice channel.
 */
public interface VoiceChannel {

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
    
}
