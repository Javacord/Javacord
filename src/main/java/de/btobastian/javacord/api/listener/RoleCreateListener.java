package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Role;

public interface RoleCreateListener extends Listener {
    
    /**
     * Called when a new role was created.
     * 
     * @param api The api.
     * @param role The role.
     */
    public void onRoleCreate(DiscordAPI api, Role role);

}
