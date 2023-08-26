package org.javacord.api.listener.channel.server.textable;

import org.javacord.api.entity.channel.TextableRegularServerChannel;

import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link TextableRegularServerChannel}.
 */
public interface TextableRegularServerChannelAttachableListener extends ServerTextChannelAttachableListener,
        ServerVoiceChannelAttachableListener {
}
