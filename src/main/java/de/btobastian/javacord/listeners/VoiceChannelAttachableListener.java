package de.btobastian.javacord.listeners;

import de.btobastian.javacord.entities.channels.VoiceChannel;
import de.btobastian.javacord.listeners.group.channel.GroupChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelAttachableListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link VoiceChannel}.
 */
public interface VoiceChannelAttachableListener
        extends ServerVoiceChannelAttachableListener, GroupChannelAttachableListener, PrivateChannelAttachableListener {
}
