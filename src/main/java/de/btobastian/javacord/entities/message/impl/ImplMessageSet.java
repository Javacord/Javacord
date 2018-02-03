package de.btobastian.javacord.entities.message.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageSet;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link MessageSet}.
 */
public class ImplMessageSet implements MessageSet {

    /**
     * A list with all messages.
     */
    private final List<Message> messages = new ArrayList<>();

    /**
     * The text channel of the messages.
     */
    private final TextChannel channel;

    /**
     * Creates a new message history.
     *
     * @param channel The channel of the messages.
     */
    private ImplMessageSet(TextChannel channel) {
        this.channel = channel;
    }

    /**
     * Gets the history of messages in the given channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @return The history.
     */
    public static CompletableFuture<MessageSet> getHistory(TextChannel channel, int limit) {
        return getHistory(channel, limit, -1, -1);
    }

    /**
     * Gets the history of messages before a given message in the given channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @return The history.
     */
    public static CompletableFuture<MessageSet> getHistoryBefore(TextChannel channel, int limit, long before) {
        return getHistory(channel, limit, before, -1);
    }

    /**
     * Gets the history of messages after a given message in the given channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param after Get messages after the message with this id.
     * @return The history.
     */
    public static CompletableFuture<MessageSet> getHistoryAfter(TextChannel channel, int limit, long after) {
        return getHistory(channel, limit, -1, after);
    }

    /**
     * Gets the history of messages around a given message in the given channel.
     * Half of the message will be older than the given message and half of the message will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param around Get messages around the message with this id.
     * @return The history.
     */
    public static CompletableFuture<MessageSet> getHistoryAround(TextChannel channel, int limit, long around) {
        CompletableFuture<MessageSet> future = new CompletableFuture<>();
        channel.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                // calculate the half limit.
                int halfLimit = limit / 2;

                // get the newer half
                ImplMessageSet history = (ImplMessageSet) getHistoryAfter(channel, halfLimit, around).join();

                // calculate the message id for getting the older half + around message
                long referenceMessageId = (history.getMessages().size() == 0) ? -1 : history.getOldestMessage().getId();

                // get the older half + around message
                MessageSet historyBefore = getHistoryBefore(channel, halfLimit + 1, referenceMessageId).join();

                // combine the messages of these "histories"
                history.messages.addAll(((ImplMessageSet) historyBefore).messages);
                history.messages.sort(null);

                // we are done
                future.complete(history);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Gets the history of messages in the given channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @param after Get messages after the message with this id.
     *
     * @return The history.
     */
    private static CompletableFuture<MessageSet> getHistory(
            TextChannel channel, int limit, long before, long after) {
        CompletableFuture<MessageSet> future = new CompletableFuture<>();
        channel.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                ImplMessageSet history = new ImplMessageSet(channel);

                // get the initial batch with the first <= 100 messages
                int initialBatchSize = ((limit % 100) == 0) ? 100 : limit % 100;
                Message[] msgArray = history.request(initialBatchSize, before, after, -1).join();
                history.messages.addAll(Arrays.asList(msgArray));
                history.messages.sort(null);

                // limit <= 100 => initial request got all messages
                // msgArray is empty => READ_MESSAGE_HISTORY permission is denied or no more messages available
                if ((limit <= 100) || (msgArray.length == 0)) {
                    future.complete(history);
                    return;
                }

                // calculate the amount of remaining message to get
                // this will be a multiple of 100 and at least 100
                int remainingMessages = limit - initialBatchSize;

                // get remaining messages
                for (int step = 0; step < remainingMessages / 100; ++step) {
                    msgArray = history.request(
                            100,
                            // before was set or both were not set
                            (before != -1) || (after == -1) ? history.getOldestMessage().getId() : -1,
                            (after != -1) ? history.getNewestMessage().getId() : -1,
                            -1
                    ).join();

                    // no more messages available
                    if (msgArray.length == 0) {
                        break;
                    }

                    // combine the messages of these "histories"
                    history.messages.addAll(Arrays.asList(msgArray));
                    history.messages.sort(null);
                }

                // we are done
                future.complete(history);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Requests the messages from Discord.
     *
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @param after Get messages after the message with this id.
     * @param around Get messages around the message with this id.
     * @return A future to check if the request was successful.
     */
    private CompletableFuture<Message[]> request(int limit, long before, long after, long around) {
        RestRequest<Message[]> restRequest =
                new RestRequest<Message[]>(channel.getApi(), RestMethod.GET, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(channel.getId()));

        if (limit != -1) {
            restRequest.addQueryParameter("limit", String.valueOf(limit));
        }
        if (before != -1) {
            restRequest.addQueryParameter("before", String.valueOf(before));
        }
        if (after != -1) {
            restRequest.addQueryParameter("after", String.valueOf(after));
        }
        if (around != -1) {
            restRequest.addQueryParameter("around", String.valueOf(around));
        }

        return restRequest.execute(result -> {
            Collection<Message> messages = new ArrayList<>();
            for (JsonNode messageJson : result.getJsonBody()) {
                messages.add(((ImplDiscordApi) channel.getApi()).getOrCreateMessage(channel, messageJson));
            }
            return messages.toArray(new Message[messages.size()]);
        });
    }

    @Override
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}