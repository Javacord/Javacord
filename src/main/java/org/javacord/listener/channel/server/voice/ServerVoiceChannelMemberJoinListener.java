package org.javacord.listener.channel.server.voice;

import org.javacord.event.channel.server.voice.ServerVoiceChannelMemberJoinEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to users joining a server voice channel.
 */
@FunctionalInterface
public interface ServerVoiceChannelMemberJoinListener extends ServerAttachableListener, UserAttachableListener,
                                                              ServerVoiceChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user joins a server voice channel.
     *
     * @param event The event
     */
    void onServerVoiceChannelMemberJoin(ServerVoiceChannelMemberJoinEvent event);

}