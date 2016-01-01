package de.btobastian.javacord.api;

/**
 * A discord user.
 */
public interface User extends MessageReceiver {
    
    /**
     * Gets the name of the user.
     * 
     * @return The name of the user.
     */
    public String getName();
    
    /**
     * Gets the id of the user.
     * 
     * @return The id of the user.
     */
    public String getId();
    
    /**
     * Shows the "is typing.." status for 5 seconds.
     */
    public void type();
    
    /**
     * Checks if the user is yourself.
     * 
     * @return Whether the user is you or not.
     */
    public boolean isYourself();

}
