package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.message.Message;

public interface MessageEditListener extends Listener {
    
    /**
     * Called when a message was edited.
     * 
     * @param api The api.
     * @param message The edited message with the new content.
     * @param oldContent The old content.
     */
    public void onMessageEdit(DiscordAPI api, Message message, String oldContent);

}
