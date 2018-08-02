package org.javacord.api.listener.channel;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.listener.channel.server.ServerChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link Channel}.
 */
public interface ChannelAttachableListener
        extends TextChannelAttachableListener, VoiceChannelAttachableListener, ServerChannelAttachableListener {
}
