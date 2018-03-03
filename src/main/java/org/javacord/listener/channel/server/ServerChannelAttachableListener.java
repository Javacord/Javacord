package org.javacord.listener.channel.server;

import org.javacord.entity.channel.ServerChannel;
import org.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelAttachableListener;
import org.javacord.entity.channel.ServerChannel;

/**
 * This is a marker interface for listeners that can be attached to a {@link ServerChannel}.
 */
public interface ServerChannelAttachableListener extends
        ServerTextChannelAttachableListener, ServerVoiceChannelAttachableListener, ChannelCategoryAttachableListener {
}
