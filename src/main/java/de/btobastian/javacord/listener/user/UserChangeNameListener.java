package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.User;
import de.btobastian.javacord.listener.Listener;

public interface UserChangeNameListener extends Listener {
    
    /**
     * Called when an user changed his name.
     * 
     * @param api The api.
     * @param user The user with the updated name.
     * @param oldName The old name of the user.
     */
    public void onUserChangeName(DiscordAPI api, User user, String oldName);

}
