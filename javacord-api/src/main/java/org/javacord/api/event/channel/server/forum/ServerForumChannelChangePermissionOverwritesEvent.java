package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.forum.PermissionOverwrite;

import java.util.List;

/**
 * A server forum channel change permission overwrites event.
 */
public interface ServerForumChannelChangePermissionOverwritesEvent extends ServerForumChannelEvent {
    /**
     * Gets the new permission overwrites of the forum channel.
     *
     * @return The new permission overwrites of the forum channel.
     */
    List<PermissionOverwrite> getNewPermissionOverwrites();

    /**
     * Gets the old permission overwrites of the forum channel.
     *
     * @return The old permission overwrites of the forum channel.
     */
    List<PermissionOverwrite> getOldPermissionOverwrites();
}
