package de.btobastian.javacord.entities.message;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.*;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Gets the readable content of the message, which replaces all mentions etc. with the actual name.
     * The replacement happens as following:
     * <ul>
     * <li><b>User mentions</b>:
     * <code>@nickname</code> if the user has a nickname, <code>@name</code> if the user has no nickname,
     * <code>@invalid-user</code> if the user is not in the server.
     * <li><b>Channel mentions</b>:
     * <code>#name</code> if the text channel exists in the server, otherwise <code>#deleted-channel</code>
     * <li><b>Custom emoji</b>:
     * <code>:name:</code>. If the emoji is known, the real name is used, otherwise the name from the mention tag.
     * </ul>
     *
     * @return The readable content of the message.
     */
    default String getReadableContent() {
        String content = getContent();
        Matcher userMention = Pattern.compile("<@!?([0-9]+)>").matcher(content);
        while (userMention.find()) {
            String userId = userMention.group(1);
            String userName = getChannel().asServerChannel().map(c -> {
                Optional<User> user = c.getServer().getMembers().stream()
                        .filter(u -> userId.equals(String.valueOf(u.getId()))).findAny();
                return user.map(u -> u.getNickname(c.getServer()).orElse(u.getName())).orElse("invalid-user");
            }).orElse("invalid-user");
            content = userMention.replaceFirst("@" + userName);
            userMention.reset(content);
        }
        Matcher channelMention = Pattern.compile("<#([0-9]+)>").matcher(content);
        while (channelMention.find()) {
            String channelId = channelMention.group(1);
            String channelName = getChannel().asServerChannel().map(c ->
                c.getServer().getTextChannelById(channelId).map(ServerChannel::getName).orElse("deleted-channel")
            ).orElse("deleted-channel");
            content = channelMention.replaceFirst("#" + channelName);
            channelMention.reset(content);
        }
        Matcher customEmoji = Pattern.compile("<:([0-9a-zA-Z]+):([0-9]+)>").matcher(content);
        while (customEmoji.find()) {
            String emojiId = customEmoji.group(2);
            String name = getApi().getCustomEmojiById(emojiId).map(CustomEmoji::getName).orElse(customEmoji.group(1));
            content = customEmoji.replaceFirst(":" + name + ":");
            customEmoji.reset(content);
        }
        // TODO mention roles
        return content;
    }

    /**
     * Gets the type of the message.
     *
     * @return The type of the message.
     */
    MessageType getType();

    /**
     * Gets the text channel of the message.
     *
     * @return The text channel of the message.
     */
    TextChannel getChannel();

    /**
     * Gets a list with all embeds of the message.
     *
     * @return A list with all embeds of the message.
     */
    List<Embed> getEmbeds();

    /**
     * Gets the user author of the message.
     * The author is not present, if it's a webhook for example.
     *
     * @return The user author of the message.
     */
    Optional<User> getAuthor();

    /**
     * Checks if the message is kept in cache forever.
     *
     * @return Whether the message is kept in cache forever or not.
     */
    boolean isCachedForever();

    /**
     * Sets if the the message is kept in cache forever.
     *
     * @param cachedForever  Whether the message should be kept in cache forever or not.
     */
    void setCachedForever(boolean cachedForever);

    /**
     * Checks if the information of this message is still valid.
     * If the message got removed from cache but you stored this objects somewhere it might have outdated content.
     *
     * @return Whether the information of this message is still valid or not.
     */
    default boolean isValid() {
        return getApi().getCachedMessageById(getId()).orElse(null) == this;
    }

    /**
     * Whether this message is deleted or not.
     * Deleted messages might still be in the cache for not more than 60 seconds.
     *
     * @return Whether the message is deleted or not.
     */
    boolean isDeleted();

    /**
     * Gets a list with all reactions of the message.
     *
     * @return A list which contains all reactions of the message.
     */
    List<Reaction> getReactions();

    /**
     * Gets a reaction by it's emoji.
     *
     * @param emoji The emoji of the reaction.
     * @return The reaction for the given emoji.
     */
    default Optional<Reaction> getReactionByEmoji(Emoji emoji) {
        return getReactions().stream().filter(reaction -> reaction.getEmoji() == emoji).findAny();
    }

    /**
     * Gets a reaction by it's unicode emoji.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return The reaction for the given emoji.
     */
    default Optional<Reaction> getReactionByEmoji(String unicodeEmoji) {
        return getReactions().stream()
                .filter(reaction -> unicodeEmoji.equals(reaction.getEmoji().asUnicodeEmoji().orElse(null))).findAny();
    }

    /**
     * Adds a unicode reaction to the message.
     *
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> addReaction(String unicodeEmoji) {
        RestRequest<Void> request = new RestRequest<Void>(getApi(), HttpMethod.PUT, RestEndpoint.REACTION)
                .setUrlParameters(String.valueOf(getChannel().getId()), String.valueOf(getId()), unicodeEmoji, "@me")
                .setRatelimitRetries(50);
        getServerTextChannel().map(ServerChannel::getServer).map(DiscordEntity::getId).map(String::valueOf)
                .ifPresent(request::setCustomMajorParam);
        return request.execute(res -> null);
    }

    /**
     * Adds a reaction to the message.
     *
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> addReaction(Emoji emoji) {
        String value = emoji.asUnicodeEmoji().orElse(
                emoji.asCustomEmoji()
                        .map(e -> e.getName() + ":" + String.valueOf(e.getId()))
                        .orElse("UNKNOWN")
        );
        RestRequest<Void> request = new RestRequest<Void>(getApi(), HttpMethod.PUT, RestEndpoint.REACTION)
                .setUrlParameters(String.valueOf(getChannel().getId()), String.valueOf(getId()), value, "@me")
                .setRatelimitRetries(50);
        getServerTextChannel().map(ServerChannel::getServer).map(DiscordEntity::getId).map(String::valueOf)
                .ifPresent(request::setCustomMajorParam);
        return request.execute(res -> null);
    }

    /**
     * Deletes all reactions on this message.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeAllReactions() {
        RestRequest<Void> request = new RestRequest<Void>(getApi(), HttpMethod.DELETE, RestEndpoint.REACTION)
                .setUrlParameters(String.valueOf(getChannel().getId()), String.valueOf(getId()));
        getServerTextChannel().map(ServerChannel::getServer).map(DiscordEntity::getId).map(String::valueOf)
                .ifPresent(request::setCustomMajorParam);
        return request.execute(res -> null);
    }

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
     * Updates the content of the message.
     *
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> edit(String content) {
        return edit(content, null);
    }

    /**
     * Updates the embed of the message.
     *
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> edit(EmbedBuilder embed) {
        return edit(null, embed);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param content The new content of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> edit(String content, EmbedBuilder embed) {
        JSONObject body = new JSONObject();
        if (content != null) {
            body.put("content", content);
        }
        if (embed != null) {
            body.put("embed", embed.toJSONObject());
        }
        return new RestRequest<Void>(getApi(), HttpMethod.PATCH, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(getChannel().getId()) ,String.valueOf(getId()))
                .setBody(body)
                .execute(res -> null);
    }

    /**
     * Deletes the message.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), HttpMethod.DELETE, RestEndpoint.MESSAGE_DELETE)
                .setUrlParameters(String.valueOf(getChannel().getId()), String.valueOf(getId()))
                .setRatelimitRetries(25)
                .execute(res -> {
                    ((ImplMessage) this).setDeleted(true);
                    return null;
                });
    }

    /**
     * Gets the history of messages before this message.
     *
     * @param limit The limit of messages to get.
     * @return The history.
     * @see TextChannel#getHistoryBefore(int, long)
     */
    default CompletableFuture<MessageHistory> getHistoryBefore(int limit) {
        return getChannel().getHistoryBefore(limit, this);
    }

    /**
     * Gets the history of messages after this message.
     *
     * @param limit The limit of messages to get.
     * @return The history.
     * @see TextChannel#getHistoryAfter(int, long)
     */
    default CompletableFuture<MessageHistory> getHistoryAfter(int limit) {
        return getChannel().getHistoryAfter(limit, this);
    }

    /**
     * Gets the history of messages around this message.
     * Half of the message will be older than the given message and half of the message will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param limit The limit of messages to get.
     * @return The history.
     * @see TextChannel#getHistoryAround(int, long)
     */
    default CompletableFuture<MessageHistory> getHistoryAround(int limit) {
        return getChannel().getHistoryAround(limit, this);
    }

    /**
     * Adds a listener, which listens to this message being deleted.
     *
     * @param listener The listener to add.
     */
    default void addMessageDeleteListener(MessageDeleteListener listener) {
        getApi().addMessageDeleteListener(getId(), listener);
    }

    /**
     * Gets a list with all registered message delete listeners.
     *
     * @return A list with all registered message delete listeners.
     */
    default List<MessageDeleteListener> getMessageDeleteListeners() {
        return getApi().getMessageDeleteListeners(getId());
    }

    /**
     * Adds a listener, which listens to this message being edited.
     *
     * @param listener The listener to add.
     */
    default void addMessageEditListener(MessageEditListener listener) {
        getApi().addMessageEditListener(getId(), listener);
    }

    /**
     * Gets a list with all registered message edit listeners.
     *
     * @return A list with all registered message edit listeners.
     */
    default List<MessageEditListener> getMessageEditListeners() {
        return getApi().getMessageEditListeners(getId());
    }

    /**
     * Adds a listener, which listens to reactions being added to this message.
     *
     * @param listener The listener to add.
     */
    default void addReactionAddListener(ReactionAddListener listener) {
        getApi().addReactionAddListener(getId(), listener);
    }

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    default List<ReactionAddListener> getReactionAddListeners() {
        return getApi().getReactionAddListeners(getId());
    }

    /**
     * Adds a listener, which listens to reactions being removed from this message.
     *
     * @param listener The listener to add.
     */
    default void addReactionRemoveListener(ReactionRemoveListener listener) {
        getApi().addReactionRemoveListener(getId(), listener);
    }

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    default List<ReactionRemoveListener> getReactionRemoveListeners() {
        return getApi().getReactionRemoveListeners(getId());
    }

}
