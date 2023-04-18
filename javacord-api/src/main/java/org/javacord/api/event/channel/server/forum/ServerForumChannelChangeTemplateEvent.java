package org.javacord.api.event.channel.server.forum;

public interface ServerForumChannelChangeTemplateEvent extends ServerForumChannelEvent {

    /**
     * Gets the new template of the channel.
     *
     * @return The new template of the channel.
     */
    String getNewTemplate();

    /**
     * Gets the old template of the channel.
     *
     * @return The old template of the channel.
     */
    String getOldTemplate();
}
