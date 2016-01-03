package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;
import de.btobastian.javacord.listener.Listener;

public interface ServerMemberAddListener extends Listener {

    /**
     * Called when an user joins a server.
     * 
     * @param api The api.
     * @param server The server the user joined.
     * @param user The user who joined.
     */
    public void onServerMemberAdd(DiscordAPI api, Server server, User user);
    
}
