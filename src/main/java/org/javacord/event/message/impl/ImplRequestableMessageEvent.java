package org.javacord.event.message.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.message.Message;
import org.javacord.event.message.RequestableMessageEvent;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link RequestableMessageEvent}.
 */
public abstract class ImplRequestableMessageEvent extends ImplOptionalMessageEvent implements RequestableMessageEvent {

    /**
     * Creates a new requestable message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ImplRequestableMessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

    @Override
    public CompletableFuture<Message> requestMessage() {
        return getMessage()
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> getChannel().getMessageById(getMessageId()));
    }

}
