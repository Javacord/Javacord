package de.btobastian.javacord.listener.voice;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.VoiceChannel;
import de.btobastian.javacord.listener.Listener;

public interface VoiceChannelChangeNameListener extends Listener {
    
    /**
     * Called when the name of a voice channel has changed.
     * 
     * @param api The api.
     * @param channel The voice channel with the updated name.
     * @param oldName The old name of the voice channel.
     */
    public void onVoiceChannelChangeName(DiscordAPI api, VoiceChannel channel, String oldName);

}
