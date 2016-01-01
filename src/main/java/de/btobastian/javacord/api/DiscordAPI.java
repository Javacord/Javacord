package de.btobastian.javacord.api;

import java.io.UnsupportedEncodingException;
import java.util.List;

import de.btobastian.javacord.api.listener.Listener;
import de.btobastian.javacord.api.listener.ReadyListener;

/**
 * The discord api.
 */
public interface DiscordAPI {

    /**
     * Sets the email address to login.
     * 
     * @param email The email.
     */
    public void setEmail(String email);
    
    /**
     * Sets the password.
     * 
     * @param password The password.
     */
    public void setPassword(String password);
    
    /**
     * Gets the email used to connect.
     * 
     * @return The email.
     */
    public String getEmail();
    
    /**
     * Gets the password used to connect.
     * 
     * @return The password.
     */
    public String getPassword();
    
    /**
     * Attempts to login.
     * 
     * @param listener The listener informs you whether the connection was successful or not.
     */
    public void connect(ReadyListener listener);
    
    /**
     * Checks if the connection if ready.
     * 
     * @return Whether the connection if ready or not.
     */
    public boolean isReady();
    
    /**
     * Sets the encoding (default: UTF-8).
     * 
     * @param encoding The encoding to set.
     * @throws UnsupportedEncodingException if it's an unknown encoding.
     */
    public void setEncoding(String encoding) throws UnsupportedEncodingException;
    
    /**
     * Gets the used encoding.
     * 
     * @return The used encoding.
     */
    public String getEncoding();
    
    /**
     * Sets the current game.
     * This may have a short delay.
     * 
     * @param game The game to set.
     */
    public void setGame(String game);
    
    /**
     * Gets the current game.
     * 
     * @return The current game.
     */
    public String getGame();
    
    /**
     * Gets an user by it's id.
     * 
     * @param id The is of the user.
     * @return The user with the given id. <code>Null</code> if no user with the given id is known.
     */
    public User getUserById(String id);
    
    /**
     * Gets a message by its id.
     * 
     * @param messageId The id of the message.
     * @return The message. May be <code>null</code>, even if a message with the given id exists!
     */
    public Message getMessageById(String messageId);
    
    /**
     * Gets a list with all known users.
     * 
     * @return A list with all known users.
     */
    public List<User> getUsers();
    
    /**
     * Gets a list with all known servers.
     * 
     * @return A list with all known servers.
     */
    public List<Server> getServers();
    
    /**
     * Registers a listener.
     * 
     * @param listener The listener to register.
     */
    public void registerListener(Listener listener);
    
    /**
     * Accepts an invite.
     * 
     * @param inviteCode The invite code.
     * @return The server invited to. <code>Null</code> if the invite is invalid.
     */
    public Server acceptInvite(String inviteCode);
    
    /**
     * Gets the user object of yourself.
     * Sending yourself messages and doing other strange stuff can cause some errors, so don't do it.
     * 
     * @return Yourself.
     */
    public User getYourself();
    
}
