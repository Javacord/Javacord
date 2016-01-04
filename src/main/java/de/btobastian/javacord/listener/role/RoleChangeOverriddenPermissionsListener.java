package de.btobastian.javacord.listener.role;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.permissions.Permissions;
import de.btobastian.javacord.permissions.Role;

public interface RoleChangeOverriddenPermissionsListener extends Listener {
    
    /**
     * Called when the overridden permissions of a role were changed.
     * 
     * @param api The api.
     * @param role The role.
     * @param channel The channel of the overridden permissions.
     * @param oldPermissions The old permissions.
     */
    public void onRoleChangeOverriddenPermissions(DiscordAPI api, Role role, Channel channel, Permissions oldPermissions);

}
