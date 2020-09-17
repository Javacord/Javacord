package org.javacord.api.event.server;

import org.javacord.api.event.channel.server.voice.ServerVoiceChannelEvent;

/**
 * A voice state update event.
 * Provided only for the current logged in user.
 */
public interface VoiceStateUpdateEvent extends ServerVoiceChannelEvent {

    /**
     * Gets the session id provided in this event.
     *
     * @return The session id.
     */
    String getSessionId();

}