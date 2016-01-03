package de.btobastian.javacord;

/**
 * Helps to create invites.
 */
public interface InviteBuilder {

    /**
     * Sets the max uses of the invite.
     * 
     * @param maxUses The max uses of the invite.
     * @return This object.
     */
    public InviteBuilder setMaxUses(int maxUses);
    
    /**
     * Sets if the invite is temporary.
     * 
     * @param temporary Whether the invite should be temporary or not.
     * @return This object.
     */
    public InviteBuilder setTemporary(boolean temporary);
    
    /**
     * Sets the max age of the invite.
     * 
     * @param maxAge The max age of the invite in seconds.
     * @return This object.
     */
    public InviteBuilder setMaxAge(int maxAge);
    
    /**
     * Creates the invite.
     * 
     * @return The invite code. <code>Null</code> if you're not allowed to create invites.
     */
    public String create();
    
}
