package de.btobastian.javacord.events.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.emoji.Emoji;

/**
 * A single reaction event.
 */
public class SingleReactionEvent extends ReactionEvent {

    /**
     * The emoji of the event.
     */
    private final Emoji emoji;

    /**
     * Creates a new single reaction event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param emoji The emoji.
     */
    public SingleReactionEvent(DiscordApi api, long messageId, TextChannel channel, Emoji emoji) {
        super(api, messageId, channel);
        this.emoji = emoji;
    }

    /**
     * Gets the emoji of the event.
     *
     * @return The emoji.
     */
    public Emoji getEmoji() {
        return emoji;
    }

}
