package org.javacord.api.listener.channel.server.voice;

import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to users leaving a server voice channel.
 */
@FunctionalInterface
public interface ServerVoiceChannelMemberLeaveListener extends ServerAttachableListener, UserAttachableListener,
                                                               ServerVoiceChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user leaves a server voice channel.
     *
     * @param event The event
     */
    void onServerVoiceChannelMemberLeave(ServerVoiceChannelMemberLeaveEvent event);

}
