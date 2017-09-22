package de.btobastian.javacord.events.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.emoji.Emoji;

import java.util.Optional;

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

    /**
     * Gets the reaction if the message is cached and the reaction exists.
     *
     * @return The reaction.
     */
    public Optional<Reaction> getReaction() {
        Optional<Message> message = getMessage();
        if (message.isPresent()) {
            return message.get().getReactionByEmoji(getEmoji());
        }
        return Optional.empty();
    }

    /**
     * Gets the amount of users who used the reaction.
     * This is not present, if the message is not cached!
     *
     * @return The amount of users who used the reaction.
     */
    public Optional<Integer> getCount() {
        return getMessage().map(msg -> msg.getReactionByEmoji(getEmoji()).map(Reaction::getCount).orElse(0));
    }

}
