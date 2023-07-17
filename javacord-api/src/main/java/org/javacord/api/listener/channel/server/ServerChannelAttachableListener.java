package org.javacord.api.listener.channel.server;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.listener.channel.server.textable.TextableRegularServerChannelAttachableListener;

/**
 * This is a marker interface for listeners that can be attached to a {@link ServerChannel}.
 */
public interface ServerChannelAttachableListener extends
        TextableRegularServerChannelAttachableListener, ChannelCategoryAttachableListener {
}
