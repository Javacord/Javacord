package de.btobastian.javacord.listener.role;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Role;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.permissions.Permissions;

public interface RoleChangePermissionsListener extends Listener {
    
    /**
     * Called when the permissions of a role were changed.
     * 
     * @param api The api.
     * @param role The role.
     * @param oldPermissions The old permissions.
     */
    public void onRoleChangePermissions(DiscordAPI api, Role role, Permissions oldPermissions);

}
