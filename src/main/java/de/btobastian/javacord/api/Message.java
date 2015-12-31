package de.btobastian.javacord.api;

import java.util.List;

/**
 * A discord message.
 */
public interface Message {

    /**
     * Gets the id of the message.
     * 
     * @return The id of the message.
     */
    public String getId();
    
    /**
     * Gets the content of the message.
     * 
     * @return The content of the message.
     */
    public String getContent();
    
    /**
     * Gets the {@link Channel channel} of the message.
     * <code>Null</code> if the message is a private message.
     * 
     * @return The channel of the message.
     */
    public Channel getChannelReceiver();
    
    /**
     * Gets the user the message was sent to.
     * <code>Null</code> if the message is no private message.
     * 
     * @return The user who received the message.
     */
    public User getUserReceiver();
    
    /**
     * Gets the {@link MessageReceiver receiver} of the message.
     * Could be a channel or a user.
     * 
     * @return The receiver of the message.
     * @see {@link #getChannelReceiver()} and {@link #getUserReceiver()}.
     */
    public MessageReceiver getReceiver();
    
    /**
     * Gets the author of the message.
     * 
     * @return The author of the message.
     */
    public User getAuthor();
    
    /**
     * Checks if the message is a private message.
     * 
     * @return Whether th message is private or not.
     */
    public boolean isPrivateMessage();
    
    /**
     * Gets all mentioned users.
     * 
     * @return A list with all mentioned users.
     */
    public List<User> getMentions();
    
    /**
     * Replies to the message.
     * 
     * @param message The message to send.
     * @return The new message.
     */
    public Message reply(String message);
    
    /**
     * Replies to the message.
     * 
     * @param message The message to send.
     * @param tts Whether the message should be tts or not.
     * @return The new message.
     */
    public Message reply(String message, boolean tts);
    
    /**
     * Checks if the message is tts.
     * 
     * @return Whether the message is tts or not.
     */
    public boolean isTts();
    
}
