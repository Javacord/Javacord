package de.btobastian.javacord.listener.channel.server.voice;

import de.btobastian.javacord.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to users joining a server voice channel.
 */
@FunctionalInterface
public interface ServerVoiceChannelMemberLeaveListener extends ServerAttachableListener, UserAttachableListener,
                                                               ServerVoiceChannelAttachableListener,
                                                               GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user joins a server voice channel.
     *
     * @param event The event
     */
    void onServerVoiceChannelMemberLeave(ServerVoiceChannelMemberLeaveEvent event);

}