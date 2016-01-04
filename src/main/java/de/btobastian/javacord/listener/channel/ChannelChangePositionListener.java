package de.btobastian.javacord.listener.channel;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;

public interface ChannelChangePositionListener extends Listener {
    
    /**
     * Called when the position of a channel has changed.
     * 
     * @param api The api.
     * @param channel The channel with the updated position.
     * @param oldPosition The old position of the channel.
     */
    public void onChannelChangePosition(DiscordAPI api, Channel channel, int oldPosition);

}
