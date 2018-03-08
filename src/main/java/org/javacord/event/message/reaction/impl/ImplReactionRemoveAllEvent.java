package org.javacord.event.message.reaction.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.event.message.impl.ImplRequestableMessageEvent;
import org.javacord.event.message.reaction.ReactionRemoveAllEvent;

/**
 * The implementation of {@link ReactionRemoveAllEvent}.
 */
public class ImplReactionRemoveAllEvent extends ImplRequestableMessageEvent implements ReactionRemoveAllEvent {

    /**
     * Creates a new reaction remove all event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ImplReactionRemoveAllEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

}
