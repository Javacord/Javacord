package org.javacord.api.listener.channel.server.voice;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.voice.ServerStageVoiceChannelChangeTopicEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server stage voice channel change name changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
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
