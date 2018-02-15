package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.entities.channels.ServerChannel;

/**
 * This is a marker interface for listeners that can be attached to a {@link ServerChannel}.
 */
public interface ServerChannelAttachableListener extends
        ServerTextChannelAttachableListener, ServerVoiceChannelAttachableListener {
}
