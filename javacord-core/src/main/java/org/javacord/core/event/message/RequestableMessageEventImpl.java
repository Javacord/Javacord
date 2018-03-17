package org.javacord.core.event.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.RequestableMessageEvent;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link RequestableMessageEvent}.
 */
public abstract class RequestableMessageEventImpl extends OptionalMessageEventImpl implements RequestableMessageEvent {

    /**
     * Creates a new requestable message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public RequestableMessageEventImpl(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

    @Override
    public CompletableFuture<Message> requestMessage() {
        return getMessage()
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> getChannel().getMessageById(getMessageId()));
    }

}
