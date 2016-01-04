package de.btobastian.javacord.listener.voice;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.VoiceChannel;
import de.btobastian.javacord.listener.Listener;

public interface VoiceChannelChangePositionListener extends Listener {
    
    /**
     * Called when the position of a voice channel has changed.
     * 
     * @param api The api.
     * @param channel The voice channel with the updated position.
     * @param oldPosition The old position of the voice channel.
     */
    public void onVoiceChannelChangePosition(DiscordAPI api, VoiceChannel channel, int oldPosition);

}
