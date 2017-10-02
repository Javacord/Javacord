package de.btobastian.javacord.entities.message.impl;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageHistory;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link MessageHistory}.
 */
public class ImplMessageHistory implements MessageHistory {

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
    private ImplMessageHistory(TextChannel channel) {
        this.channel = channel;
    }

    /**
     * Gets the history of messages in the given channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @return The history.
     */
    public static CompletableFuture<MessageHistory> getHistory(TextChannel channel, int limit) {
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
    public static CompletableFuture<MessageHistory> getHistoryBefore(TextChannel channel, int limit, long before) {
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
    public static CompletableFuture<MessageHistory> getHistoryAfter(TextChannel channel, int limit, long after) {
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
    public static CompletableFuture<MessageHistory> getHistoryAround(TextChannel channel, int limit, long around) {
        CompletableFuture<MessageHistory> future = new CompletableFuture<>();
        channel.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                ImplMessageHistory history = new ImplMessageHistory(channel);

                // First step: Get the messages around directly from discord.
                Message[] msgArray = history.request(limit == 100 ? 100 : limit % 100, -1, -1, around).join();
                history.messages.addAll(Arrays.asList(msgArray));
                history.messages.sort(Comparable::compareTo);

                if (limit / 100 == 0 || msgArray.length == 0) {
                    future.complete(history);
                    return;
                }

                // Second step: Calculate the amount of message to get before and after the oldest/newest message
                int messagesToFetchAfter = (int) (((limit - (limit == 100 ? 100 : limit % 100)) / 2D) + 0.5);
                int messagesToFetchBefore = (int) ((limit - (limit == 100 ? 100 : limit % 100)) / 2D);

                // Third step: Get message history for before and after
                MessageHistory historyBefore = null;
                if (messagesToFetchBefore > 0) {
                    historyBefore =
                            getHistoryBefore(channel, messagesToFetchBefore, history.getOldestMessage().getId()).join();
                }

                MessageHistory historyAfter = null;
                if (messagesToFetchAfter > 0) {
                    historyAfter =
                            getHistoryAfter(channel, messagesToFetchAfter, history.getNewestMessage().getId()).join();
                }

                // Forth step: Combine the messages of these "histories"
                if (historyBefore != null) {
                    history.messages.addAll(historyBefore.getMessages());
                }

                if (historyAfter != null) {
                    history.messages.addAll(historyAfter.getMessages());
                }

                history.messages.sort(Comparable::compareTo);

                // Fifth step: We are done! The answer is 42!
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
    private static CompletableFuture<MessageHistory> getHistory(TextChannel channel, int limit, long before, long after)
    {
        CompletableFuture<MessageHistory> future = new CompletableFuture<>();
        channel.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                ImplMessageHistory history = new ImplMessageHistory(channel);

                int step = 0;
                while (step < (double) limit / 100D) {
                    Message[] msgArray;
                    if (step == 0) {
                        msgArray = history.request((limit % 100) == 0 ? 100 : limit % 100, before, after, -1).join();
                    } else {
                        msgArray = history.request(
                                100,
                                before != -1 ? history.getOldestMessage().getId() : -1,
                                after != -1 ? history.getNewestMessage().getId() : -1,
                                -1
                        ).join();
                    }
                    history.messages.addAll(Arrays.asList(msgArray));
                    history.messages.sort(Comparable::compareTo);
                    step++;
                    if (msgArray.length == 0) {
                        break;
                    }
                }
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
                new RestRequest<Message[]>(channel.getApi(), HttpMethod.GET, RestEndpoint.MESSAGE)
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

        return restRequest.execute(res -> {
                    JSONArray messagesJson = res.getBody().getArray();
                    Message[] messages = new Message[messagesJson.length()];
                    for (int i = 0; i < messagesJson.length(); i++) {
                        messages[i] = ((ImplDiscordApi) channel.getApi())
                                .getOrCreateMessage(channel, messagesJson.getJSONObject(i));
                    }
                    return messages;
                });
    }

    @Override
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}