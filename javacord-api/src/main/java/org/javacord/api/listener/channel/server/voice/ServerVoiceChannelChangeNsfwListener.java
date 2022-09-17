package org.javacord.api.listener.channel.server.voice;

import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeNsfwEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server voice channel nsfw changes.
 */
@FunctionalInterface
public interface ServerVoiceChannelChangeNsfwListener extends ServerAttachableListener,
        ServerVoiceChannelAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server voice channel's nsfw changes.
     *
     * @param event The event.
     */
    void onServerVoiceChannelChangeNsfw(ServerVoiceChannelChangeNsfwEvent event);
}
