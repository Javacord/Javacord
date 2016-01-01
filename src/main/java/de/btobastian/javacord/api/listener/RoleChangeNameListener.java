package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Role;

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
