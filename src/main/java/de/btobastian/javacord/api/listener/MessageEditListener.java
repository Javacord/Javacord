package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.Message;

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
