package org.javacord.api.listener.channel.server.voice;

import org.javacord.api.event.channel.server.voice.ServerStageVoiceChannelChangeTopicEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

public interface ServerStageVoiceChannelChangeTopicListener  extends ServerAttachableListener,
        ServerStageVoiceChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server stage voice channel's topic changes.
     *
     * @param event The event.
     */
    void onServerStageVoiceChannelChangeTopic(ServerStageVoiceChannelChangeTopicEvent event);
}
