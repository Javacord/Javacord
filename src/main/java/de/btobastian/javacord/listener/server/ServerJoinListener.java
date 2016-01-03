package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Server;

public interface ServerJoinListener {

    /**
     * Called when the users joins or creates a new server.
     * 
     * @param api The api.
     * @param server The joined or created server.
     */
    public void onServerJoin(DiscordAPI api, Server server);
    
}
