package de.btobastian.javacord.listener.channel;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.listener.Listener;

public interface ChannelChangeTopicListener extends Listener {
    
    /**
     * Called when the topic of a channel has changed.
     * 
     * @param api The api.
     * @param channel The channel with the updated topic.
     * @param oldTopic The old topic of the channel.
     */
    public void onChannelChangeTopic(DiscordAPI api, Channel channel, String oldTopic);

}
