package de.btobastian.javacord.entities.message;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a Discord message.
 */
public interface Message extends DiscordEntity, Comparable<Message> {

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    String getContent();

    /**
     * Gets the text channel of the message.
     *
     * @return The text channel of the message.
     */
    TextChannel getChannel();

    /**
     * Gets the embed of the message.
     *
     * @return The embed of the message.
     */
    Optional<Embed> getEmbed();

    /**
     * Gets the user author of the message.
     * The author is not present, if it's a webhook for example.
     *
     * @return The user author of the message.
     */
    Optional<User> getAuthor();

    /**
     * Gets the server text channel of the message.
     * Only present if the message was sent in a server.
     *
     * @return The server text channel.
     */
    default Optional<ServerTextChannel> getServerTextChannel() {
        return Optional.ofNullable(getChannel() instanceof ServerTextChannel ? (ServerTextChannel) getChannel() : null);
    }

    /**
     * Gets the private channel of the message.
     * Only present if the message was sent in a private conversation.
     *
     * @return The private channel.
     */
    default Optional<PrivateChannel> getPrivateChannel() {
        return Optional.ofNullable(getChannel() instanceof PrivateChannel ? (PrivateChannel) getChannel() : null);
    }

    /**
     * Gets the private channel of the message.
     * Only present if the message was sent in a private conversation.
     *
     * @return The private channel.
     */
    default Optional<GroupChannel> getGroupChannel() {
        return Optional.ofNullable(getChannel() instanceof GroupChannel ? (GroupChannel) getChannel() : null);
    }

    /**
     * Deletes the message.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        new RestRequest(getApi(), HttpMethod.DELETE, RestEndpoint.MESSAGE_DELETE)
                .setUrlParameters(String.valueOf(getChannel().getId()), String.valueOf(getId()))
                .setRatelimitRetries(25)
                .execute()
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        future.completeExceptionally(throwable);
                        return;
                    }
                    future.complete(null);
                });
        return future;
    }

}
