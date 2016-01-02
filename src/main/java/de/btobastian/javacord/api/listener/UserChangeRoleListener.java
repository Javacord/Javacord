package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Role;
import de.btobastian.javacord.api.User;

public interface UserChangeRoleListener extends Listener {

    /**
     * Called when the role of a user changed (added or removed).
     * 
     * @param api The api.
     * @param user The affected user.
     * @param role The role.
     * @param added Whether the user was added to the role or removed.
     */
    public void onUserChangeRole(DiscordAPI api, User user, Role role, boolean added);
    
}
