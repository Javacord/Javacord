package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.utils.JavacordCompletableFuture;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A message event where the message is NOT guaranteed to be in the cache.
 */
public abstract class OptionalMessageEvent extends MessageEvent {

    /**
     * The message of the event. Might be <code>null</code>.
     */
    private final Message message;

    /**
     * Creates a new optional message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public OptionalMessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
        message = api.getCachedMessageById(messageId).orElse(null);
    }

    /**
     * Gets the message from the cache.
     *
     * @return The message from the cache.
     */
    public Optional<Message> getMessage() {
        return Optional.ofNullable(message);
    }

    /**
     * Requests a message from Discord, if it's not cached.
     *
     * @return The message either from the cache or directly from Discord.
     * @see TextChannel#getMessageById(long)
     */
    public CompletableFuture<Message> requestMessage() {
        Optional<Message> message = getMessage();
        if (!message.isPresent()) {
            getChannel().getMessageById(getMessageId());
        }
        CompletableFuture<Message> future = new JavacordCompletableFuture<>();
        message.ifPresent(future::complete);
        return future;
    }

}
