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
     * @return The created channel. <code>Null</code> if the channel couldn't be created.
     */
    public Channel createChannel(String name);
    
    /**
     * Creates a new voice channel.
     * 
     * @param name The name of the channel.
     * @return The created channel. <code>Null</code> if the channel couldn't be created.
     */
    public VoiceChannel createVoiceChannel(String name);
    
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
    
    /**
     * Gets a list with all voice channels.
     * 
     * @return A list with all voice channels.
     */
    public List<VoiceChannel> getVoiceChannels();
    
    /**
     * Gets a list with all roles.
     * 
     * @return A list with all roles.
     */
    public List<Role> getRoles();
    
    /**
     * Gets the region of the server.
     * 
     * @return The region of the server.
     */
    public String getRegion();
    
    /**
     * Unbans the user with the given id.
     * 
     * @param user The id of the user to unban.
     */
    public void unban(String userId);
    
    /**
     * Bans the user.
     * 
     * @param user The user to ban.
     */
    public void ban(User user);
    
    /**
     * Bans the user.
     * 
     * @param user The user to ban.
     * @param deleteDays Deletes all messages of the user which are younger than <code>deleteDays</code> days.
     */
    public void ban(User user, int deleteDays);
    
    
    
}
