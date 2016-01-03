package de.btobastian.javacord.listener.role;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Role;
import de.btobastian.javacord.listener.Listener;

public interface RoleChangeNameListener extends Listener {
    
    /**
     * Called when the name of a role was changed.
     * 
     * @param api The api.
     * @param role The role.
     * @param oldName The old name of the role.
     */
    public void onRoleChangeName(DiscordAPI api, Role role, String oldName);

}
