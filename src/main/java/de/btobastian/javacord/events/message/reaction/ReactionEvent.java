package de.btobastian.javacord.events.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.events.message.OptionalMessageEvent;

/**
 * A reaction event.
 */
public abstract class ReactionEvent extends OptionalMessageEvent {

    /**
     * Creates a new reaction event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ReactionEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

}
