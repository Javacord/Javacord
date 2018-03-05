package org.javacord.entity.message;

import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.message.embed.EmbedBuilder;
import org.javacord.entity.user.User;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.message.MessageAttachableListener;
import org.javacord.listener.message.MessageDeleteListener;
import org.javacord.listener.message.MessageEditListener;
import org.javacord.listener.message.reaction.ReactionAddListener;
import org.javacord.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.listener.message.reaction.ReactionRemoveListener;
import org.javacord.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides methods to interact with messages without having an instance of it.
 */
public interface UncachedMessageUtil {

    /**
     * Deletes the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> delete(long channelId, long messageId);

    /**
     * Deletes the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> delete(String channelId, String messageId);

    /**
     * Deletes the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param reason The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> delete(long channelId, long messageId, String reason);

    /**
     * Deletes the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param reason The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> delete(String channelId, String messageId, String reason);

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param channelId The id of the message's channel.
     * @param messageIds The ids of the messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> deleteAll(long channelId, long... messageIds);

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param channelId The id of the message's channel.
     * @param messageIds The ids of the messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> deleteAll(String channelId, String... messageIds);

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param messages The messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> deleteAll(Message... messages);

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param messages The messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> deleteAll(Iterable<Message> messages);

    /**
     * Updates the content of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(long channelId, long messageId, String content);

    /**
     * Updates the content of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(String channelId, String messageId, String content);

    /**
     * Updates the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(long channelId, long messageId, EmbedBuilder embed);

    /**
     * Updates the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(String channelId, String messageId, EmbedBuilder embed);

    /**
     * Updates the content and the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(long channelId, long messageId, String content, EmbedBuilder embed);

    /**
     * Updates the content and the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(String channelId, String messageId, String content, EmbedBuilder embed);

    /**
     * Updates the content and the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @param updateContent Whether to update or remove the content.
     * @param embed The new embed of the message.
     * @param updateEmbed Whether to update or remove the embed.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(long channelId, long messageId, String content, boolean updateContent,
                                 EmbedBuilder embed, boolean updateEmbed);

    /**
     * Updates the content and the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @param updateContent Whether to update or remove the content.
     * @param embed The new embed of the message.
     * @param updateEmbed Whether to update or remove the embed.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> edit(String channelId, String messageId, String content, boolean updateContent,
                                 EmbedBuilder embed, boolean updateEmbed);

    /**
     * Removes the content of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    CompletableFuture<Void> removeContent(long channelId, long messageId);

    /**
     * Removes the content of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    CompletableFuture<Void> removeContent(String channelId, String messageId);

    /**
     * Removes the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    CompletableFuture<Void> removeEmbed(long channelId, long messageId);

    /**
     * Removes the embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    CompletableFuture<Void> removeEmbed(String channelId, String messageId);

    /**
     * Removes the content and embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    CompletableFuture<Void> removeContentAndEmbed(long channelId, long messageId);

    /**
     * Removes the content and embed of the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    CompletableFuture<Void> removeContentAndEmbed(String channelId, String messageId);

    /**
     * Adds a unicode reaction to the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReaction(long channelId, long messageId, String unicodeEmoji);

    /**
     * Adds a unicode reaction to the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReaction(String channelId, String messageId, String unicodeEmoji);

    /**
     * Adds a reaction to the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReaction(long channelId, long messageId, Emoji emoji);

    /**
     * Adds a reaction to the message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReaction(String channelId, String messageId, Emoji emoji);

    /**
     * Deletes all reactions on this message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeAllReactions(long channelId, long messageId);

    /**
     * Deletes all reactions on this message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeAllReactions(String channelId, String messageId);

    /**
     * Pins this message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the pin was successful.
     */
    CompletableFuture<Void> pin(long channelId, long messageId);

    /**
     * Pins this message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the pin was successful.
     */
    CompletableFuture<Void> pin(String channelId, String messageId);

    /**
     * Unpins this message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> unpin(long channelId, long messageId);

    /**
     * Unpins this message.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> unpin(String channelId, String messageId);

    /**
     * Gets a list with all users who reacted with the given emoji.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @return A list with all users who reacted with the given emoji
     */
    CompletableFuture<List<User>> getUsersWhoReactedWithEmoji(long channelId, long messageId, Emoji emoji);

    /**
     * Gets a list with all users who reacted with the given emoji.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @return A list with all users who reacted with the given emoji
     */
    CompletableFuture<List<User>> getUsersWhoReactedWithEmoji(String channelId, String messageId, Emoji emoji);

    /**
     * Removes the reaction of the given user.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @param user The user to remove.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> removeUserReactionByEmoji(long channelId, long messageId, Emoji emoji, User user);

    /**
     * Removes the reaction of the given user.
     *
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @param user The user to remove.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> removeUserReactionByEmoji(String channelId, String messageId, Emoji emoji, User user);

    /**
     * Adds a listener, which listens to message deletions of a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageDeleteListener> addMessageDeleteListener(long messageId, MessageDeleteListener listener);

    /**
     * Adds a listener, which listens to message deletions of a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageDeleteListener> addMessageDeleteListener(Message message, MessageDeleteListener listener);

    /**
     * Gets a list with all registered message delete listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered message delete listeners.
     */
    List<MessageDeleteListener> getMessageDeleteListeners(long messageId);

    /**
     * Gets a list with all registered message delete listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered message delete listeners.
     */
    List<MessageDeleteListener> getMessageDeleteListeners(String messageId);

    /**
     * Gets a list with all registered message delete listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered message delete listeners.
     */
    List<MessageDeleteListener> getMessageDeleteListeners(Message message);

    /**
     * Adds a listener, which listens to message edits of a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageEditListener> addMessageEditListener(long messageId, MessageEditListener listener);

    /**
     * Adds a listener, which listens to message edits of a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageEditListener> addMessageEditListener(Message message, MessageEditListener listener);

    /**
     * Gets a list with all registered message edit listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered message edit listeners.
     */
    List<MessageEditListener> getMessageEditListeners(long messageId);

    /**
     * Gets a list with all registered message edit listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered message edit listeners.
     */
    List<MessageEditListener> getMessageEditListeners(String messageId);

    /**
     * Gets a list with all registered message edit listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered message edit listeners.
     */
    List<MessageEditListener> getMessageEditListeners(Message message);

    /**
     * Adds a listener, which listens to reactions being added to a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionAddListener> addReactionAddListener(long messageId, ReactionAddListener listener);

    /**
     * Adds a listener, which listens to reactions being added to a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionAddListener> addReactionAddListener(Message message, ReactionAddListener listener);

    /**
     * Gets a list with all registered reaction add listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners(long messageId);

    /**
     * Gets a list with all registered reaction add listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners(String messageId);

    /**
     * Gets a list with all registered reaction add listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners(Message message);

    /**
     * Adds a listener, which listens to reactions being removed from a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveListener> addReactionRemoveListener(long messageId, ReactionRemoveListener listener);

    /**
     * Adds a listener, which listens to reactions being removed from a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveListener> addReactionRemoveListener(Message message, ReactionRemoveListener listener);

    /**
     * Gets a list with all registered reaction remove listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners(long messageId);

    /**
     * Gets a list with all registered reaction remove listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners(String messageId);

    /**
     * Gets a list with all registered reaction remove listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners(Message message);

    /**
     * Adds a listener, which listens to all reactions being removed from a specific message at once.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(long messageId,
                                                                            ReactionRemoveAllListener listener);

    /**
     * Adds a listener, which listens to all reactions being removed from a specific message at once.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(Message message,
                                                                            ReactionRemoveAllListener listener);

    /**
     * Gets a list with all registered reaction remove all listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove all listeners.
     */
    List<ReactionRemoveAllListener> getReactionRemoveAllListeners(long messageId);

    /**
     * Gets a list with all registered reaction remove all listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove all listeners.
     */
    List<ReactionRemoveAllListener> getReactionRemoveAllListeners(String messageId);

    /**
     * Gets a list with all registered reaction remove all listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered reaction remove all listeners.
     */
    List<ReactionRemoveAllListener> getReactionRemoveAllListeners(Message message);

    /**
     * Adds a listener that implements one or more {@code MessageAttachableListener}s to the message with the given id.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addMessageAttachableListener(long messageId, T listener);

    /**
     * Adds a listener that implements one or more {@code MessageAttachableListener}s to the message with the given id.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addMessageAttachableListener(String messageId, T listener);

    /**
     * Adds a listener that implements one or more {@code MessageAttachableListener}s to the given message.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addMessageAttachableListener(Message message, T listener);

    /**
     * Removes a {@code MessageAttachableListener} from the message with the given id.
     *
     * @param messageId The id of the message.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> void removeListener(
            long messageId, Class<T> listenerClass, T listener);

    /**
     * Removes a {@code MessageAttachableListener} from the message with the given id.
     *
     * @param messageId The id of the message.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> void removeListener(
            String messageId, Class<T> listenerClass, T listener);

    /**
     * Removes a listener that implements one or more {@code MessageAttachableListener}s from the given message.
     *
     * @param message The message.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> void removeListener(
            Message message, Class<T> listenerClass, T listener);

    /**
     * Removes a listener that implements one or more {@code MessageAttachableListener}s from the message with the given
     * id.
     *
     * @param messageId The id of the message.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            long messageId, T listener);

    /**
     * Removes a listener that implements one or more {@code MessageAttachableListener}s from the message with the given
     * id.
     *
     * @param messageId The id of the message.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            String messageId, T listener);

    /**
     * Removes a listener that implements one or more {@code MessageAttachableListener}s from the given message.
     *
     * @param message The message.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            Message message, T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code MessageAttachableListener}s and their
     * assigned listener classes they listen to for the message with the given id.
     *
     * @param messageId The id of the message.
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code MessageAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getMessageAttachableListeners(long messageId);

    /**
     * Gets a map with all registered listeners that implement one or more {@code MessageAttachableListener}s and their
     * assigned listener classes they listen to for the message with the given id.
     *
     * @param messageId The id of the message.
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code MessageAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getMessageAttachableListeners(String messageId);

    /**
     * Gets a map with all registered listeners that implement one or more {@code MessageAttachableListener}s and their
     * assigned listener classes they listen to for the given message.
     *
     * @param message The message.
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code MessageAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getMessageAttachableListeners(Message message);
    
}
