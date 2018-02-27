package de.btobastian.javacord.listener.channel.server;

import de.btobastian.javacord.entity.channel.ServerChannel;
import de.btobastian.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import de.btobastian.javacord.listener.channel.server.voice.ServerVoiceChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link ServerChannel}.
 */
public interface ServerChannelAttachableListener extends
        ServerTextChannelAttachableListener, ServerVoiceChannelAttachableListener, ChannelCategoryAttachableListener {
}
