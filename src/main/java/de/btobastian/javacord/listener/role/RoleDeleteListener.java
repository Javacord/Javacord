package de.btobastian.javacord.listener.role;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.permissions.Role;

public interface RoleDeleteListener extends Listener {
    
    /**
     * Called when a new role was deleted.
     * 
     * @param api The api.
     * @param role The deleted role.
     */
    public void onRoleDelete(DiscordAPI api, Role role);

}
