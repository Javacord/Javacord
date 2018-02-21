package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerVoiceChannelMemberJoinEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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