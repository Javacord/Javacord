package org.javacord.api.listener.channel.server;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link ServerChannel}.
 */
public interface ServerChannelAttachableListener extends
        ServerTextChannelAttachableListener, ServerVoiceChannelAttachableListener, ChannelCategoryAttachableListener {
}
