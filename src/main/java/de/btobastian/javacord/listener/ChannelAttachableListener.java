package de.btobastian.javacord.listener;

import de.btobastian.javacord.entity.channel.Channel;
import de.btobastian.javacord.listener.channel.server.ServerChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link Channel}.
 */
public interface ChannelAttachableListener
        extends TextChannelAttachableListener, VoiceChannelAttachableListener, ServerChannelAttachableListener {
}
