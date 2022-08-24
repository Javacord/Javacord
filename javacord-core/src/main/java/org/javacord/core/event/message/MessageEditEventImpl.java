package org.javacord.core.event.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageEditEvent;
import java.util.Optional;

/**
 * The implementation of {@link MessageEditEvent}.
 */
public class MessageEditEventImpl extends CertainMessageEventImpl implements MessageEditEvent {
    /**
     * The old message. May be <code>null</code>!
     */
    private final Message oldMessage;

    /**
     * Whether this event represents a real change of the contents of this message.
     */
    private final boolean isActualEdit;

    /**
     * Creates a new message edit event.
     *
     * @param api            The discord api instance.
     * @param messageId      The id of the message.
     * @param channel        The text channel in which the message was sent.
     * @param updatedMessage The updated message.
     * @param oldMessage     The old message.
     * @param isActualEdit   Whether this event represents a real change of the contents of this message.
     */
    public MessageEditEventImpl(DiscordApi api, long messageId, TextChannel channel, Message updatedMessage,
                                Message oldMessage, boolean isActualEdit) {
        super(updatedMessage);
        this.oldMessage = oldMessage;
        this.isActualEdit = isActualEdit;
    }

    @Override
    public Optional<Message> getOldMessage() {
        return Optional.ofNullable(oldMessage);
    }

    @Override
    public boolean isActualEdit() {
        return isActualEdit;
    }
}
