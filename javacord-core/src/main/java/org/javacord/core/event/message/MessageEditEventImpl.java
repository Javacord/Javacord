package org.javacord.core.event.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageEditEvent;
import java.util.Optional;


/**
 * The implementation of {@link MessageEditEvent}.
 */
public class MessageEditEventImpl extends RequestableMessageEventImpl implements MessageEditEvent {

    /**
     * The updated message.
     */
    private final Message updatedMessage;

    /**
     * The old message. May be <code>null</code>!
     */
    private final Message oldMessage;

    /**
     * Whether this is a special case like embedding links.
     */
    private final boolean specialCase;


    /**
     * Creates a new message edit event.
     *
     * @param api            The discord api instance.
     * @param messageId      The id of the message.
     * @param channel        The text channel in which the message was sent.
     * @param updatedMessage The updated message.
     * @param oldMessage     The old message.
     * @param specialCase    Whether this is a special case like embedding links.
     */
    public MessageEditEventImpl(DiscordApi api, long messageId, TextChannel channel, Message updatedMessage,
                                Message oldMessage, boolean specialCase) {
        super(api, messageId, channel);
        this.updatedMessage = updatedMessage;
        this.oldMessage = oldMessage;
        this.specialCase = specialCase;
    }

    @Override
    public Message getUpdatedMessage() {
        return updatedMessage;
    }

    @Override
    public Optional<Message> getOldMessage() {
        return Optional.ofNullable(oldMessage);
    }

    @Override
    public boolean isSpecialCase() {
        return specialCase;
    }
}