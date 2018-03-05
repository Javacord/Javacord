package org.javacord.listener;

import org.javacord.entity.channel.VoiceChannel;
import org.javacord.listener.channel.group.GroupChannelAttachableListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelAttachableListener;
import org.javacord.listener.user.channel.PrivateChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link VoiceChannel}.
 */
public interface VoiceChannelAttachableListener
        extends ServerVoiceChannelAttachableListener, GroupChannelAttachableListener, PrivateChannelAttachableListener {
}
