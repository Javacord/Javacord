package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Server;

public interface ServerJoinListener {

    /**
     * Called when the users joins or creates a new server.
     * 
     * @param api The api.
     * @param server The joined or created server.
     */
    public void onServerJoin(DiscordAPI api, Server server);
    
}
