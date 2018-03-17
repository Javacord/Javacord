package org.javacord.core.event.message.reaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.reaction.ReactionRemoveAllEvent;
import org.javacord.core.event.message.RequestableMessageEventImpl;

/**
 * The implementation of {@link ReactionRemoveAllEvent}.
 */
public class ReactionRemoveAllEventImpl extends RequestableMessageEventImpl implements ReactionRemoveAllEvent {

    /**
     * Creates a new reaction remove all event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ReactionRemoveAllEventImpl(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

}
