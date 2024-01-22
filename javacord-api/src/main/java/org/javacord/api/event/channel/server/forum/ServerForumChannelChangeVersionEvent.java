package org.javacord.api.event.channel.server.forum;

/**
 * A server forum channel change version event.
 */
public interface ServerForumChannelChangeVersionEvent extends ServerForumChannelEvent {

    /**
     * Gets the old version.
     *
     * @return The old version.
     */
    long getOldVersion();

    /**
     * Gets the new version.
     *
     * @return The new version.
     */
    long getNewVersion();
}
