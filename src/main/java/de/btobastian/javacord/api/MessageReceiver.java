package de.btobastian.javacord.api;

/**
 * Everyone who can receive messages.
 */
public interface MessageReceiver {
    
    /**
     * Sends a message.
     * 
     * @param message The message to send.
     * @return The sent message.
     */
    public Message sendMessage(String message);
    
    /**
     * Sends a message.
     * 
     * @param message The message to send.
     * @param tts Whether the message should be tts or not.
     * @return The sent message.
     */
    public Message sendMessage(String message, boolean tts);

}
