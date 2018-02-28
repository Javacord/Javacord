package de.btobastian.javacord.listener;

import de.btobastian.javacord.entity.channel.TextChannel;
import de.btobastian.javacord.listener.channel.group.GroupChannelAttachableListener;
import de.btobastian.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import de.btobastian.javacord.listener.user.channel.PrivateChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link TextChannel}.
 */
public interface TextChannelAttachableListener
        extends ServerTextChannelAttachableListener, GroupChannelAttachableListener, PrivateChannelAttachableListener {
}
