package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Message;

public interface MessageDeleteListener extends Listener {
    
    /**
     * Called when a message was deleted.
     * 
     * @param api The api.
     * @param message The deleted message.
     */
    public void onMessageDelete(DiscordAPI api, Message message);

}
