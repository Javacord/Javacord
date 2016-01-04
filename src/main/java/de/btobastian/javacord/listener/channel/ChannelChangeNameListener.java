package de.btobastian.javacord.listener.channel;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;

public interface ChannelChangeNameListener extends Listener {
    
    /**
     * Called when the name of a channel has changed.
     * 
     * @param api The api.
     * @param channel The channel with the updated name.
     * @param oldName The old name of the channel.
     */
    public void onChannelChangeName(DiscordAPI api, Channel channel, String oldName);

}
