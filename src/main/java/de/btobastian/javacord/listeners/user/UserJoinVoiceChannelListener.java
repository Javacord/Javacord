package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserJoinVoiceChannelEvent;

/**
 * This listener listens to user joining a voice-channel.
 */
@FunctionalInterface
public interface UserJoinVoiceChannelListener {

    /**
     * This method is called every time a user joined a voice-channel.
     *
     * @param event The event
     */
    void onUserJoinVoiceChannel(UserJoinVoiceChannelEvent event);

}