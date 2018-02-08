package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserLeaveVoiceChannelEvent;

/**
 * This listener listens to user joining a voice-channel.
 */
@FunctionalInterface
public interface UserLeaveVoiceChannelListener {

    /**
     * This method is called every time a user joined a voice-channel.
     *
     * @param event The event
     */
    void onUserLeaveVoiceChannel(UserLeaveVoiceChannelEvent event);

}