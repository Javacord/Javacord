package org.javacord.listener;

import org.javacord.entity.channel.Channel;
import org.javacord.listener.channel.server.ServerChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link Channel}.
 */
public interface ChannelAttachableListener
        extends TextChannelAttachableListener, VoiceChannelAttachableListener, ServerChannelAttachableListener {
}
