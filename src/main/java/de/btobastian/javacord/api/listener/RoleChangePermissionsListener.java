package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Role;
import de.btobastian.javacord.api.permissions.Permissions;

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
