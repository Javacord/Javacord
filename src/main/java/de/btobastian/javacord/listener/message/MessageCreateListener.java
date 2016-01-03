package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.message.Message;

public interface MessageCreateListener extends Listener {
    
    /**
     * Called when a new message was created.
     * 
     * @param api The api.
     * @param message The new message.
     */
    public void onMessageCreate(DiscordAPI api, Message message);

}
