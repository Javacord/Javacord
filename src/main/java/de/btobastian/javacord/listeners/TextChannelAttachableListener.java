package de.btobastian.javacord.listeners;

import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.listeners.group.channel.GroupChannelAttachableListener;
import de.btobastian.javacord.listeners.server.channel.ServerTextChannelAttachableListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link TextChannel}.
 */
public interface TextChannelAttachableListener
        extends ServerTextChannelAttachableListener, GroupChannelAttachableListener, PrivateChannelAttachableListener {
}
