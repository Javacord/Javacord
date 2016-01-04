package de.btobastian.javacord.listener.user;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.User;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.permissions.Permissions;

public interface UserChangeOverriddenPermissionsListener extends Listener {
    
    /**
     * Called when the overridden permissions of an user were changed.
     * 
     * @param api The api.
     * @param user The user.
     * @param channel The channel of the overridden permissions.
     * @param oldPermissions The old permissions.
     */
    public void onUserChangeOverriddenPermissions(DiscordAPI api, User user, Channel channel, Permissions oldPermissions);

}
