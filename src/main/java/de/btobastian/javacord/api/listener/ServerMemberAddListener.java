package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;

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
