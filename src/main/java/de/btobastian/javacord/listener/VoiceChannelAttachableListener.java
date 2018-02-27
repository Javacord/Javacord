package de.btobastian.javacord.listener;

import de.btobastian.javacord.entity.channel.VoiceChannel;
import de.btobastian.javacord.listener.channel.group.GroupChannelAttachableListener;
import de.btobastian.javacord.listener.channel.server.voice.ServerVoiceChannelAttachableListener;
import de.btobastian.javacord.listener.user.channel.PrivateChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link VoiceChannel}.
 */
public interface VoiceChannelAttachableListener
        extends ServerVoiceChannelAttachableListener, GroupChannelAttachableListener, PrivateChannelAttachableListener {
}
