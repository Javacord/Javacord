package de.btobastian.javacord.listeners;

import de.btobastian.javacord.entities.channels.Channel;
import de.btobastian.javacord.listeners.server.channel.ServerChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link Channel}.
 */
public interface ChannelAttachableListener
        extends TextChannelAttachableListener, VoiceChannelAttachableListener, ServerChannelAttachableListener {
}
