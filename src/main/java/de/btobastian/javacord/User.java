package de.btobastian.javacord;

import java.awt.image.BufferedImage;
import java.net.URL;

import de.btobastian.javacord.message.MessageReceiver;

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
    
    /**
     * Gets the avatar of the user as byte array.
     * 
     * @return The jpg-avatar of the user. The array is empty if the user has no avatar.
     */
    public byte[] getAvatarAsBytearray();
    
    /**
     * Gets the avatar of the user.
     * 
     * @return The jpg-avatar of the user. <code>Null</code> if the user has no avatar.
     */
    public BufferedImage getAvatar();
    
    /**
     * Gets the url of the users avatar.
     * 
     * @return The url of the users avatar. <code>Null</code> if the user has no avatar.
     */
    public URL getAvatarUrl();

}
