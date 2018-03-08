package org.javacord.listener;

import org.javacord.entity.channel.TextChannel;
import org.javacord.listener.channel.group.GroupChannelAttachableListener;
import org.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.listener.channel.user.PrivateChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link TextChannel}.
 */
public interface TextChannelAttachableListener
        extends ServerTextChannelAttachableListener, GroupChannelAttachableListener, PrivateChannelAttachableListener {
}
