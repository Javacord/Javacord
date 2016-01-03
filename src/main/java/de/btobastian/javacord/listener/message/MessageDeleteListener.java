package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.message.Message;

public interface MessageDeleteListener extends Listener {
    
    /**
     * Called when a message was deleted.
     * 
     * @param api The api.
     * @param message The deleted message.
     */
    public void onMessageDelete(DiscordAPI api, Message message);

}
