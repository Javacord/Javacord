package de.btobastian.javacord.api.listener;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.DiscordAPI;
import de.btobastian.javacord.api.User;

public interface TypingStartListener extends Listener {
    
    /**
     * Called when a user starts typing.
     * 
     * @param api The api.
     * @param user The user who starts typing.
     * @param channel The channel where the users is typing. <code>Null</code> if the user is typing in private a chat.
     */
    public void onTypingStart(DiscordAPI api, User user, Channel channel);

}
