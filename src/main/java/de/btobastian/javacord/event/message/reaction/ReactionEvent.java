package de.btobastian.javacord.event.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.channel.TextChannel;
import de.btobastian.javacord.event.message.RequestableMessageEvent;

/**
 * A reaction event.
 */
public abstract class ReactionEvent extends RequestableMessageEvent {

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
