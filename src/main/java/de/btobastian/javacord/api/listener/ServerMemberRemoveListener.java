package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;

public interface ServerMemberRemoveListener extends Listener {

    /**
     * Called when an user leaves a server.
     * 
     * @param api The api.
     * @param server The server the user left.
     * @param user The user who left.
     */
    public void onServerMemberRemove(DiscordAPI api, Server server, User user);
    
}
