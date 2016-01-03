package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Role;
import de.btobastian.javacord.User;
import de.btobastian.javacord.listener.Listener;

public interface UserChangeRoleListener extends Listener {

    /**
     * Called when the role of a user was changed (added or removed).
     * 
     * @param api The api.
     * @param user The affected user.
     * @param role The role.
     * @param added Whether the user was added to the role or removed.
     */
    public void onUserChangeRole(DiscordAPI api, User user, Role role, boolean added);
    
}
