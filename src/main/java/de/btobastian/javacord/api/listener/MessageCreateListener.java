package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Message;

public interface MessageCreateListener extends Listener {
    
    /**
     * Called when a new message was created.
     * 
     * @param api The api.
     * @param message The new message.
     */
    public void onMessageCreate(DiscordAPI api, Message message);

}
