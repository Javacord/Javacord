package de.btobastian.javacord.listener.role;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.permissions.Role;

public interface RoleChangePositionListener extends Listener {

    /**
     * Called when the position of a role was changed.
     * This will also be called for every other effected role.
     * 
     * @param api The api.
     * @param role The role with the updated position.
     * @param oldPosition The old position of the role.
     */
    public void onRoleChangePosition(DiscordAPI api, Role role, int oldPosition);
    
}
