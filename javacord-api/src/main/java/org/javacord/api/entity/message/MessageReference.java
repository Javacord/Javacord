package org.javacord.api.entity.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface MessageReference {

    /**
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    DiscordApi getApi();

    /**
     * Gets the server id of the message reference.
     *
     * @return The server if of the message reference.
     */
    Optional<Long> getServerId();

    /**
     * Gets the channel id of the message reference.
     *
     * @return The channel if of the message reference.
     */
    long getChannelId();

    /**
     * Gets the message id of the message reference.
     *
     * @return The message if of the message reference.
     */
    Optional<Long> getMessageId();

    /**
     * Gets the referenced message.
     *
     * @return The referenced Message.
     */
    Optional<Message> getMessage();

    /**
     * Gets the server of the message reference.
     *
     * @return The server of the message reference.
     */
    default Optional<Server> getServer() {
        return getServerId().flatMap(id -> getApi().getServerById(id));
    }

    /**
     * Gets the server of the message reference.
     *
     * @return The server of the message reference.
     */
    default Optional<TextChannel> getChannel() {
        return getApi().getTextChannelById(getChannelId());
    }

    /**
     * Requests the referenced message if it isn't present.
     *
     * @return The referenced Message.
     */
    default Optional<CompletableFuture<Message>> requestMessage() {
        Optional<Message> optionalMessage = getMessage();
        if (optionalMessage.isPresent()) {
            return optionalMessage.map(CompletableFuture::completedFuture);
        }

        return getMessageId().flatMap(messageId -> getChannel().map(channel -> channel.getMessageById(messageId)));
    }
}
