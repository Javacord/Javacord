package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.DiscordEntity;
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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The implementation of {@link MessageSet}.
 */
public class ImplMessageSet implements MessageSet {

    /**
     * A read-only navigable set with all messages to let the JDK do the dirty work.
     */
    private final NavigableSet<Message> messages;

    /**
     * Creates a new message set.
     *
     * @param messages The messages to be contained in this set.
     */
    public ImplMessageSet(NavigableSet<Message> messages) {
        this.messages = Collections.unmodifiableNavigableSet(messages);
    }

    /**
     * Creates a new message set.
     *
     * @param messages The messages to be contained in this set.
     */
    public ImplMessageSet(Collection<Message> messages) {
        this(new TreeSet<>(messages));
    }

    /**
     * Creates a new message set.
     *
     * @param messages The messages to be contained in this set.
     */
    public ImplMessageSet(Message... messages) {
        this(Arrays.asList(messages));
    }

    /**
     * Gets up to a given amount of messages in the given channel from the newer end.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @return The messages.
     */
    public static CompletableFuture<MessageSet> getMessages(TextChannel channel, int limit) {
        return getMessages(channel, limit, -1, -1);
    }

    /**
     * Gets up to a given amount of messages in the given channel before a given message in any channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @return The messages.
     */
    public static CompletableFuture<MessageSet> getMessagesBefore(TextChannel channel, int limit, long before) {
        return getMessages(channel, limit, before, -1);
    }

    /**
     * Gets up to a given amount of messages in the given channel after a given message in any channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param after Get messages after the message with this id.
     * @return The messages.
     */
    public static CompletableFuture<MessageSet> getMessagesAfter(TextChannel channel, int limit, long after) {
        return getMessages(channel, limit, -1, after);
    }

    /**
     * Gets up to a given amount of messages in the given channel around a given message in any channel.
     * The given message will be part of the result in addition to the messages around if it was sent in the given
     * channel and does not count towards the limit.
     * Half of the messages will be older than the given message and half of the messages will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param around Get messages around the message with this id.
     * @return The messages.
     */
    public static CompletableFuture<MessageSet> getMessagesAround(TextChannel channel, int limit, long around) {
        CompletableFuture<MessageSet> future = new CompletableFuture<>();
        channel.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                // calculate the half limit.
                int halfLimit = limit / 2;

                // get the newer half
                MessageSet newerMessages = getMessagesAfter(channel, halfLimit, around).join();

                // get the older half + around message
                MessageSet olderMessages = getMessagesBefore(channel, halfLimit + 1, around + 1).join();

                // remove the oldest message if the around message is not part of the result while there is a result,
                // for example because the around message was from a different channel
                if (olderMessages.getNewestMessage()
                        .map(DiscordEntity::getId)
                        .map(id -> id != around)
                        .orElse(false)) {
                    olderMessages = olderMessages.tailSet(
                            olderMessages.getOldestMessage().orElseThrow(AssertionError::new),
                            false);
                }

                // combine the messages into one collection
                Collection<Message> messages = Stream
                        .of(olderMessages, newerMessages)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                // we are done
                future.complete(new ImplMessageSet(messages));
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Gets up to a given amount of messages in the given channel.
     *
     * @param channel The channel of the messages.
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @param after Get messages after the message with this id.
     *
     * @return The messages.
     */
    private static CompletableFuture<MessageSet> getMessages(TextChannel channel, int limit, long before, long after) {
        CompletableFuture<MessageSet> future = new CompletableFuture<>();
        channel.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                // get the initial batch with the first <= 100 messages
                int initialBatchSize = ((limit % 100) == 0) ? 100 : limit % 100;
                MessageSet initialMessages = request(channel, initialBatchSize, before, after).join();

                // limit <= 100 => initial request got all messages
                // initialMessages is empty => READ_MESSAGE_HISTORY permission is denied or no more messages available
                if ((limit <= 100) || initialMessages.isEmpty()) {
                    future.complete(initialMessages);
                    return;
                }

                // calculate the amount and direction of remaining message to get
                // this will be a multiple of 100 and at least 100
                int remainingMessages = limit - initialBatchSize;
                int steps = remainingMessages / 100;
                // "before" is set or both are not set
                boolean older = (before != -1) || (after == -1);
                boolean newer = after != -1;

                // get remaining messages
                List<MessageSet> messageSets = new ArrayList<>();
                MessageSet lastMessages = initialMessages;
                messageSets.add(lastMessages);
                for (int step = 0; step < steps; ++step) {
                    lastMessages = request(channel,
                                           100,
                                           lastMessages.getOldestMessage()
                                                   .filter(message -> older)
                                                   .map(DiscordEntity::getId)
                                                   .orElse(-1L),
                                           lastMessages.getNewestMessage()
                                                   .filter(message -> newer)
                                                   .map(DiscordEntity::getId)
                                                   .orElse(-1L)).join();

                    // no more messages available
                    if (lastMessages.isEmpty()) {
                        break;
                    }

                    messageSets.add(lastMessages);
                }

                // combine the message sets
                future.complete(new ImplMessageSet(messageSets.stream()
                                                           .flatMap(Collection::stream)
                                                           .collect(Collectors.toList())));
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    /**
     * Requests the messages from Discord.
     *
     * @param channel The channel of which to get messages from.
     * @param limit The limit of messages to get.
     * @param before Get messages before the message with this id.
     * @param after Get messages after the message with this id.
     * @return A future to check if the request was successful.
     */
    private static CompletableFuture<MessageSet> request(TextChannel channel, int limit, long before, long after) {
        RestRequest<MessageSet> restRequest =
                new RestRequest<MessageSet>(channel.getApi(), RestMethod.GET, RestEndpoint.MESSAGE)
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

        return restRequest.execute(result -> {
            Collection<Message> messages = StreamSupport.stream(result.getJsonBody().spliterator(), false)
                    .map(messageJson -> ((ImplDiscordApi) channel.getApi()).getOrCreateMessage(channel, messageJson))
                    .collect(Collectors.toList());
            return new ImplMessageSet(messages);
        });
    }

    @Override
    public Message lower(Message message) {
        return messages.lower(message);
    }

    @Override
    public Message floor(Message message) {
        return messages.floor(message);
    }

    @Override
    public Message ceiling(Message message) {
        return messages.ceiling(message);
    }

    @Override
    public Message higher(Message message) {
        return messages.higher(message);
    }

    @Override
    public Message pollFirst() {
        return messages.pollFirst();
    }

    @Override
    public Message pollLast() {
        return messages.pollLast();
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return messages.contains(o);
    }

    @Override
    public Iterator<Message> iterator() {
        return messages.iterator();
    }

    @Override
    public Object[] toArray() {
        return messages.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return messages.toArray(a);
    }

    @Override
    public boolean add(Message message) {
        return messages.add(message);
    }

    @Override
    public boolean remove(Object o) {
        return messages.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return messages.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Message> c) {
        return messages.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return messages.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return messages.removeAll(c);
    }

    @Override
    public void clear() {
        messages.clear();
    }

    @Override
    public NavigableSet<Message> descendingSet() {
        return messages.descendingSet();
    }

    @Override
    public Iterator<Message> descendingIterator() {
        return messages.descendingIterator();
    }

    @Override
    public MessageSet subSet(Message fromElement, boolean fromInclusive, Message toElement, boolean toInclusive) {
        return new ImplMessageSet(messages.subSet(fromElement, fromInclusive, toElement, toInclusive));
    }

    @Override
    public MessageSet headSet(Message toElement, boolean inclusive) {
        return new ImplMessageSet(messages.headSet(toElement, inclusive));
    }

    @Override
    public MessageSet tailSet(Message fromElement, boolean inclusive) {
        return new ImplMessageSet(messages.tailSet(fromElement, inclusive));
    }

    @Override
    public Comparator<? super Message> comparator() {
        return messages.comparator();
    }

    @Override
    public MessageSet subSet(Message fromElement, Message toElement) {
        return new ImplMessageSet(messages.subSet(fromElement, toElement));
    }

    @Override
    public MessageSet headSet(Message toElement) {
        return new ImplMessageSet(messages.headSet(toElement));
    }

    @Override
    public MessageSet tailSet(Message fromElement) {
        return new ImplMessageSet(messages.tailSet(fromElement));
    }

    @Override
    public Message first() {
        return messages.first();
    }

    @Override
    public Message last() {
        return messages.last();
    }

}
