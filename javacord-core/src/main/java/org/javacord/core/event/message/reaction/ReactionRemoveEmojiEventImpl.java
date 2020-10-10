package org.javacord.core.event.message.reaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.event.message.reaction.ReactionRemoveEmojiEvent;
import org.javacord.core.event.message.RequestableMessageEventImpl;

/**
 * The implementation of {@link ReactionRemoveEmojiEvent}.
 */
public class ReactionRemoveEmojiEventImpl extends RequestableMessageEventImpl implements ReactionRemoveEmojiEvent {

    private final Emoji emoji;

    /**
     * Creates a new reaction remove emoji event.
     *
     * @param api The discord api instance.
     * @param emoji The emoji of this event.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ReactionRemoveEmojiEventImpl(DiscordApi api, Emoji emoji, long messageId, TextChannel channel) {
        super(api, messageId, channel);
        this.emoji = emoji;
    }

    @Override
    public Emoji getEmoji() {
        return emoji;
    }

}
