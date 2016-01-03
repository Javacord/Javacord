package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;
import de.btobastian.javacord.listener.Listener;

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
