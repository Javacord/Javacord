package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.User;

public interface UserChangeNameListener extends Listener {
    
    /**
     * Called when a user changed his name.
     * 
     * @param api The api.
     * @param user The user with the updated name.
     * @param oldName The old name of the user.
     */
    public void onUserChangeName(DiscordAPI api, User user, String oldName);

}
