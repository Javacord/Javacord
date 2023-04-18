package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeTemplateEvent;

/**
 * The implementation of {@link ServerForumChannelChangeTemplateEvent}.
 */
public class ServerForumChannelChangeTemplateEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeTemplateEvent {

    /**
     * The old template of the channel.
     */
    private final String oldTemplate;

    /**
     * The new template of the channel.
     */
    private final String newTemplate;

    /**
     * Creates a new server forum channel change template event.
     *
     * @param channel The channel of the event.
     * @param oldTemplate The old template of the channel.
     * @param newTemplate The new template of the channel.
     */
    public ServerForumChannelChangeTemplateEventImpl(final ServerForumChannel channel,
                                                     final String oldTemplate, final String newTemplate) {
        super(channel);
        this.oldTemplate = oldTemplate;
        this.newTemplate = newTemplate;
    }

    @Override
    public String getOldTemplate() {
        return oldTemplate;
    }

    @Override
    public String getNewTemplate() {
        return newTemplate;
    }
}
