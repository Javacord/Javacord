package org.javacord.api.entity.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.sticker.StickerItem;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageInteraction;
import org.javacord.api.listener.message.MessageAttachableListenerManager;
import org.javacord.api.util.DiscordRegexPattern;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.Stream;

/**
 * This class represents a Discord message.
 */
public interface Message extends DiscordEntity, Comparable<Message>, UpdatableFromCache<Message>,
        MessageAttachableListenerManager {

    /**
     * Returns a {@code MessageBuilder} according to this {@code Message}.
     *
     * @return The {@code MessageBuilder}.
     * @see MessageBuilder#fromMessage(Message)
     */
    default MessageBuilder toMessageBuilder() {
        return MessageBuilder.fromMessage(this);
    }

    /**
     * Returns a {@code WebhookMessageBuilder} according to this {@code Message}.
     *
     * @return The {@code WebhookMessageBuilder}.
     * @see WebhookMessageBuilder#fromMessage(Message)
     */
    default WebhookMessageBuilder toWebhookMessageBuilder() {
        return WebhookMessageBuilder.fromMessage(this);
    }

    /**
     * Cross posts the message if it is in an announcement channel.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return The new message object.
     */
    static CompletableFuture<Message> crossPost(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().crossPost(channelId, messageId);
    }

    /**
     * Cross posts the message if it is in an announcement channel.
     *
     * @return The new message object.
     */
    default CompletableFuture<Message> crossPost() {
        return crossPost(getApi(), getChannel().getId(), getId());
    }

    /**
     * Deletes the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().delete(channelId, messageId);
    }

    /**
     * Deletes the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, String channelId, String messageId) {
        return api.getUncachedMessageUtil().delete(channelId, messageId, null);
    }

    /**
     * Deletes the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param reason    The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, long channelId, long messageId, String reason) {
        return api.getUncachedMessageUtil().delete(channelId, messageId, reason);
    }

    /**
     * Deletes the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param reason    The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, String channelId, String messageId, String reason) {
        return api.getUncachedMessageUtil().delete(channelId, messageId, reason);
    }

    /**
     * Deletes the message.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the message.
     *
     * @param reason The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete(String reason) {
        return Message.delete(getApi(), getChannel().getId(), getId(), reason);
    }

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param api        The discord api instance.
     * @param channelId  The id of the message's channel.
     * @param messageIds The ids of the messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, long channelId, long... messageIds) {
        return api.getUncachedMessageUtil().delete(channelId, messageIds);
    }

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param api        The discord api instance.
     * @param channelId  The id of the message's channel.
     * @param messageIds The ids of the messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, String channelId, String... messageIds) {
        return api.getUncachedMessageUtil().delete(channelId, messageIds);
    }

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param api      The discord api instance.
     * @param messages The messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, Message... messages) {
        return api.getUncachedMessageUtil().delete(messages);
    }

    /**
     * Deletes multiple messages at once.
     * This method does not have a size or age restriction.
     * Messages younger than two weeks are sent in batches of 100 messages to the bulk delete API,
     * older messages are deleted with individual delete requests.
     *
     * @param api      The discord api instance.
     * @param messages The messages to delete.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, Iterable<Message> messages) {
        return api.getUncachedMessageUtil().delete(messages);
    }

    /**
     * Updates the content of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content   The new content of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, long channelId, long messageId, String content) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content);
    }

    /**
     * Updates the content of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content   The new content of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, String channelId, String messageId, String content) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content, true, Collections.emptyList(), false);
    }

    /**
     * Updates the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, long channelId, long messageId, EmbedBuilder... embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, false,
                Arrays.asList(embeds), true);
    }

    /**
     * Updates the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, long channelId, long messageId, List<EmbedBuilder> embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, false, embeds, true);
    }

    /**
     * Updates the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, String channelId, String messageId, EmbedBuilder... embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, false, Arrays.asList(embeds), true);
    }

    /**
     * Updates the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, String channelId, String messageId,
                                           List<EmbedBuilder> embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, false, embeds, true);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content   The new content of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(
            DiscordApi api, long channelId, long messageId, String content, EmbedBuilder... embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content, true, Arrays.asList(embeds), true);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content   The new content of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(
            DiscordApi api, long channelId, long messageId, String content, List<EmbedBuilder> embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content, true, embeds, true);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content   The new content of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(
            DiscordApi api, String channelId, String messageId, String content, EmbedBuilder... embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content, true, Arrays.asList(embeds), true);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content   The new content of the message.
     * @param embeds    An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(
            DiscordApi api, String channelId, String messageId, String content, List<EmbedBuilder> embeds) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content, true, embeds, true);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api           The discord api instance.
     * @param channelId     The id of the message's channel.
     * @param messageId     The id of the message.
     * @param content       The new content of the message.
     * @param updateContent Whether to update or remove the content.
     * @param embeds        An array of the new embeds of the message.
     * @param updateEmbed   Whether to update or remove the embed.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, long channelId, long messageId, String content,
                                           boolean updateContent, List<EmbedBuilder> embeds, boolean updateEmbed) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content, updateContent, embeds, updateEmbed);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api           The discord api instance.
     * @param channelId     The id of the message's channel.
     * @param messageId     The id of the message.
     * @param content       The new content of the message.
     * @param updateContent Whether to update or remove the content.
     * @param embeds        An array of the new embeds of the message.
     * @param updateEmbed   Whether to update or remove the embed.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Message> edit(DiscordApi api, String channelId, String messageId, String content,
                                           boolean updateContent, List<EmbedBuilder> embeds, boolean updateEmbed) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, content, updateContent, embeds, updateEmbed);
    }

    /**
     * Updates the content of the message.
     *
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Message> edit(String content) {
        return new MessageUpdater(this).setContent(content).applyChanges();
    }

    /**
     * Updates the embed of the message.
     *
     * @param embeds An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Message> edit(EmbedBuilder... embeds) {
        return new MessageUpdater(this).addEmbeds(Arrays.asList(embeds)).applyChanges();
    }

    /**
     * Updates the embed of the message.
     *
     * @param embeds An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Message> edit(List<EmbedBuilder> embeds) {
        return new MessageUpdater(this).addEmbeds(embeds).applyChanges();
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param content The new content of the message.
     * @param embeds  An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Message> edit(String content, EmbedBuilder... embeds) {
        return new MessageUpdater(this).setContent(content).addEmbeds(embeds).applyChanges();
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param content The new content of the message.
     * @param embeds  An array of the new embeds of the message.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Message> edit(String content, List<EmbedBuilder> embeds) {
        return new MessageUpdater(this).setContent(content).addEmbeds(embeds).applyChanges();
    }

    /**
     * Creates a new {@link MessageUpdater} for this message that can be used similarly to a builder to edit this
     * message.
     *
     * @return the new message updater
     */
    default MessageUpdater createUpdater() {
        return new MessageUpdater(this);
    }

    /**
     * Removes the content of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    static CompletableFuture<Message> removeContent(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, "");
    }

    /**
     * Removes the content of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    static CompletableFuture<Message> removeContent(DiscordApi api, String channelId, String messageId) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, true, Collections.emptyList(), false);
    }

    /**
     * Removes the content of the message.
     *
     * @return A future to check if the removal was successful.
     */
    default CompletableFuture<Message> removeContent() {
        return new MessageUpdater(this).setContent("").applyChanges();
    }

    /**
     * Removes the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    static CompletableFuture<Message> removeEmbed(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, false, Collections.emptyList(), true);
    }

    /**
     * Removes the embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    static CompletableFuture<Message> removeEmbed(DiscordApi api, String channelId, String messageId) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, false, Collections.emptyList(), true);
    }

    /**
     * Removes the embed of the message.
     *
     * @return A future to check if the removal was successful.
     */
    default CompletableFuture<Message> removeEmbed() {
        return new MessageUpdater(this).addEmbeds(Collections.emptyList()).applyChanges();
    }

    /**
     * Removes the content and embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    static CompletableFuture<Message> removeContentAndEmbed(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, true, Collections.emptyList(), true);
    }

    /**
     * Removes the content and embed of the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to check if the removal was successful.
     */
    static CompletableFuture<Message> removeContentAndEmbed(DiscordApi api, String channelId, String messageId) {
        return api.getUncachedMessageUtil().edit(channelId, messageId, null, true, Collections.emptyList(), true);
    }

    /**
     * Removes the content and embed of the message.
     *
     * @return A future to check if the removal was successful.
     */
    default CompletableFuture<Message> removeContentAndEmbed() {
        return new MessageUpdater(this).setContent("").addEmbeds(Collections.emptyList()).applyChanges();
    }


    /**
     * Adds a unicode reaction to the message.
     *
     * @param api          The discord api instance.
     * @param channelId    The id of the message's channel.
     * @param messageId    The id of the message.
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(DiscordApi api, long channelId, long messageId, String unicodeEmoji) {
        return api.getUncachedMessageUtil().addReaction(channelId, messageId, unicodeEmoji);
    }

    /**
     * Adds a unicode reaction to the message.
     *
     * @param api          The discord api instance.
     * @param channelId    The id of the message's channel.
     * @param messageId    The id of the message.
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(
            DiscordApi api, String channelId, String messageId, String unicodeEmoji) {
        return api.getUncachedMessageUtil().addReaction(channelId, messageId, unicodeEmoji);
    }


    /**
     * Adds a reaction to the message.
     *
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> addReaction(Emoji emoji) {
        return Message.addReaction(getApi(), getChannel().getId(), getId(), emoji);
    }


    /**
     * Adds a reaction to the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji     The emoji.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(DiscordApi api, long channelId, long messageId, Emoji emoji) {
        return api.getUncachedMessageUtil().addReaction(channelId, messageId, emoji);
    }


    /**
     * Adds a unicode reaction to the message.
     *
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> addReaction(String unicodeEmoji) {
        return Message.addReaction(getApi(), getChannel().getId(), getId(), unicodeEmoji);
    }

    /**
     * Adds a reaction to the message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji     The emoji.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(DiscordApi api, String channelId, String messageId, Emoji emoji) {
        return api.getUncachedMessageUtil().addReaction(channelId, messageId, emoji);
    }

    /**
     * Deletes all reactions on this message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> removeAllReactions(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().removeAllReactions(channelId, messageId);
    }

    /**
     * Deletes all reactions on this message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> removeAllReactions(DiscordApi api, String channelId, String messageId) {
        return api.getUncachedMessageUtil().removeAllReactions(channelId, messageId);
    }

    /**
     * Deletes all reactions on this message.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeAllReactions() {
        return Message.removeAllReactions(getApi(), getChannel().getId(), getId());
    }

    /**
     * Pins this message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the pin was successful.
     */
    static CompletableFuture<Void> pin(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().pin(channelId, messageId);
    }

    /**
     * Pins this message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the pin was successful.
     */
    static CompletableFuture<Void> pin(DiscordApi api, String channelId, String messageId) {
        return api.getUncachedMessageUtil().pin(channelId, messageId);
    }

    /**
     * Pins this message.
     *
     * @return A future to tell us if the pin was successful.
     */
    default CompletableFuture<Void> pin() {
        return Message.pin(getApi(), getChannel().getId(), getId());
    }

    /**
     * Unpins this message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> unpin(DiscordApi api, long channelId, long messageId) {
        return api.getUncachedMessageUtil().unpin(channelId, messageId);
    }

    /**
     * Unpins this message.
     *
     * @param api       The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> unpin(DiscordApi api, String channelId, String messageId) {
        return api.getUncachedMessageUtil().unpin(channelId, messageId);
    }

    /**
     * Unpins this message.
     *
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> unpin() {
        return Message.unpin(getApi(), getChannel().getId(), getId());
    }

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    String getContent();

    /**
     * Gets the last time the message was edited.
     *
     * @return The last time the message was edited.
     */
    Optional<Instant> getLastEditTimestamp();

    /**
     * Gets the attachments of the message.
     *
     * @return The attachments of the message.
     */
    List<MessageAttachment> getAttachments();

    /**
     * Gets the readable content of the message, which replaces all mentions etc. with the actual name.
     * The replacement happens as following:
     * <ul>
     * <li><b>User mentions</b>:
     * <code>@nickname</code> if the user has a nickname, <code>@name</code> if the user has no nickname, unchanged if
     * the user is not in the cache.
     * <li><b>Role mentions</b>:
     * <code>@name</code> if the role exists in the server, otherwise <code>#deleted-role</code>
     * <li><b>Channel mentions</b>:
     * <code>#name</code> if the text channel exists in the server, otherwise <code>#deleted-channel</code>
     * <li><b>Custom emoji</b>:
     * <code>:name:</code>. If the emoji is known, the real name is used, otherwise the name from the mention tag.
     * </ul>
     *
     * @return The readable content of the message.
     */
    default String getReadableContent() {
        return getApi().makeMentionsReadable(getContent(), getServer().orElse(null));
    }

    /**
     * Gets the link leading to this message.
     *
     * @return The message link.
     * @throws AssertionError If the link is malformed.
     */
    default URL getLink() throws AssertionError {
        try {
            return new URL("https://" + Javacord.DISCORD_DOMAIN + "/channels/"
                    + getServer().map(DiscordEntity::getIdAsString).orElse("@me")
                    + "/"
                    + getChannel().getIdAsString()
                    + "/"
                    + getIdAsString());
        } catch (MalformedURLException e) {
            throw new AssertionError("Message link is malformed", e);
        }
    }

    /**
     * Gets all custom emojis in the message.
     *
     * @return The list of custom emojis in the message.
     */
    List<CustomEmoji> getCustomEmojis();

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
     * Gets the activity of the message.
     *
     * @return The activity of the message.
     */
    Optional<MessageActivity> getActivity();

    /**
     * Gets the flags of the message.
     *
     * @return The flags of the message.
     */
    EnumSet<MessageFlag> getFlags();

    /**
     * Checks if the message is pinned.
     *
     * @return Whether the message is pinned or not.
     */
    boolean isPinned();

    /**
     * Checks if this message is text-to-speech.
     *
     * @return Whether this message is text-to-speech or not.
     */
    boolean isTts();

    /**
     * Checks if the message mentions everyone.
     *
     * @return Whether the message mentions everyone or not.
     */
    boolean mentionsEveryone();

    /**
     * Gets all embeds of the message.
     *
     * @return All embeds of the message.
     */
    List<Embed> getEmbeds();

    /**
     * Gets the user author of the message.
     * The author is not present, if it's a webhook.
     *
     * @return The user author of the message.
     */
    Optional<User> getUserAuthor();

    /**
     * Gets the author of the message.
     * Might be a user or a webhook.
     *
     * @return The author of the message.
     */
    MessageAuthor getAuthor();

    /**
     * Gets the message reference of the message.
     *
     * @return The message reference.
     */
    Optional<MessageReference> getMessageReference();

    /**
     * Gets the message referenced with a reply.
     * Only present if this message is type {@code MessageType.REPLY},
     * discord decided to send it and the message hasn't been deleted.
     *
     * @return The referenced message.
     */
    Optional<Message> getReferencedMessage();

    /**
     * Requests the message referenced with a reply.
     *
     * <p>If the message is in the cache, the message is served from the cache.
     *
     * @return The referenced message.
     */
    default Optional<CompletableFuture<Message>> requestReferencedMessage() {
        return getReferencedMessage().map(message ->
                getReferencedMessage().map(CompletableFuture::completedFuture)
                        .orElseGet(() -> getApi().getMessageById(message.getId(), getChannel())));
    }

    /**
     * Checks if the message is kept in cache forever.
     *
     * @return Whether the message is kept in cache forever or not.
     */
    boolean isCachedForever();

    /**
     * Sets if the message is kept in cache forever.
     *
     * @param cachedForever Whether the message should be kept in cache forever or not.
     */
    void setCachedForever(boolean cachedForever);

    /**
     * Gets all reactions of the message.
     *
     * @return All reactions of the message.
     */
    List<Reaction> getReactions();

    /**
     * Gets the Message Interaction Object if the message is a response to an Interaction without an existing message.
     *
     * @return The Message Interaction Object.
     */
    Optional<MessageInteraction> getMessageInteraction();

    /**
     * Gets all components of the message.
     *
     * @return All components of the message.
     */
    List<HighLevelComponent> getComponents();

    /**
     * Gets all users mentioned in this message.
     *
     * @return All users mentioned in this message.
     */
    List<User> getMentionedUsers();

    /**
     * Gets all roles mentioned in this message.
     *
     * @return All roles mentioned in this message.
     */
    List<Role> getMentionedRoles();

    /**
     * Gets the nonce of the message.
     *
     * @return The nonce of the message.
     */
    Optional<String> getNonce();

    /**
     * Gets the sticker items of the message.
     *
     * @return The sticker items of the message.
     */
    Set<StickerItem> getStickerItems();

    /**
     * Gets all channels mentioned in this message.
     *
     * @return All channels mentioned in this message.
     */
    default List<ServerChannel> getMentionedChannels() {
        List<ServerChannel> mentionedChannels = new ArrayList<>();
        Matcher channelMention = DiscordRegexPattern.CHANNEL_MENTION.matcher(getContent());
        while (channelMention.find()) {
            String channelId = channelMention.group("id");
            getApi().getServerChannelById(channelId)
                    .filter(channel -> !mentionedChannels.contains(channel))
                    .ifPresent(mentionedChannels::add);
        }
        return Collections.unmodifiableList(mentionedChannels);
    }

    /**
     * Checks if the message was sent in a {@link ChannelType#PRIVATE_CHANNEL private channel}.
     *
     * @return Whether the message was sent in a private channel.
     */
    default boolean isPrivateMessage() {
        return getChannel().getType() == ChannelType.PRIVATE_CHANNEL;
    }

    /**
     * Checks if the message was sent in a {@link ChannelType#SERVER_TEXT_CHANNEL server channel}.
     *
     * @return Whether the message was sent in a server channel.
     */
    default boolean isServerMessage() {
        return getChannel().getType() == ChannelType.SERVER_TEXT_CHANNEL;
    }

    /**
     * Gets a reaction by its emoji.
     *
     * @param emoji The emoji of the reaction.
     * @return The reaction for the given emoji.
     */
    default Optional<Reaction> getReactionByEmoji(Emoji emoji) {
        return getReactions().stream().filter(reaction -> reaction.getEmoji().equals(emoji)).findAny();
    }


    /**
     * Gets a reaction by its unicode emoji.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return The reaction for the given emoji.
     */
    default Optional<Reaction> getReactionByEmoji(String unicodeEmoji) {
        return getReactions().stream()
                .filter(reaction -> unicodeEmoji.equals(reaction.getEmoji().asUnicodeEmoji().orElse(null))).findAny();
    }

    /**
     * Adds reactions to the message.
     *
     * @param emojis The emojis.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> addReactions(Emoji... emojis) {
        return CompletableFuture.allOf(
                Arrays.stream(emojis)
                        .map(this::addReaction)
                        .toArray(CompletableFuture[]::new));
    }

    /**
     * Adds unicode reactions to the message.
     *
     * @param unicodeEmojis The unicode emoji strings.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReactions(String... unicodeEmojis);

    /**
     * Removes a user from the list of reactors of a given emoji reaction.
     *
     * @param user  The user to remove.
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionByEmoji(User user, Emoji emoji) {
        return Reaction.removeUser(getApi(), getChannel().getId(), getId(), emoji, user.getId());
    }

    /**
     * Removes a user from the list of reactors of a given unicode emoji reaction.
     *
     * @param user         The user to remove.
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionByEmoji(User user, String unicodeEmoji);

    /**
     * Removes all reactors of a given emoji reaction.
     *
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionByEmoji(Emoji emoji) {
        return getReactionByEmoji(emoji).map(Reaction::remove).orElseGet(() -> CompletableFuture.completedFuture(null));
    }

    /**
     * Removes all reactors of a given unicode emoji reaction.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionByEmoji(String unicodeEmoji);

    /**
     * Removes a user from the list of reactors of the given emoji reactions.
     *
     * @param user   The user to remove.
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionsByEmoji(User user, Emoji... emojis) {
        return CompletableFuture.allOf(
                Arrays.stream(emojis)
                        .map(emoji -> removeReactionByEmoji(user, emoji))
                        .toArray(CompletableFuture[]::new));
    }

    /**
     * Removes a user from the list of reactors of the given unicode emoji reactions.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @param user          The user to remove.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionsByEmoji(User user, String... unicodeEmojis);


    /**
     * Removes all reactors of the given emoji reactions.
     *
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionsByEmoji(Emoji... emojis) {
        return CompletableFuture.allOf(
                Arrays.stream(emojis)
                        .map(this::removeReactionByEmoji)
                        .toArray(CompletableFuture[]::new));
    }

    /**
     * Removes all reactors of the given unicode emoji reactions.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionsByEmoji(String... unicodeEmojis);

    /**
     * Removes you from the list of reactors of a given emoji reaction.
     *
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeOwnReactionByEmoji(Emoji emoji) {
        return removeReactionByEmoji(getApi().getYourself(), emoji);
    }

    /**
     * Removes you from the list of reactors of a given unicode emoji reaction.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeOwnReactionByEmoji(String unicodeEmoji);

    /**
     * Removes you from the list of reactors of the given emoji reactions.
     *
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeOwnReactionsByEmoji(Emoji... emojis) {
        return removeReactionsByEmoji(getApi().getYourself(), emojis);
    }

    /**
     * Removes you from the list of reactors of the given unicode emoji reactions.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeOwnReactionsByEmoji(String... unicodeEmojis);

    /**
     * Gets the server text channel of the message.
     * Only present if the message was sent in a server.
     *
     * @return The server text channel.
     */
    default Optional<ServerTextChannel> getServerTextChannel() {
        return getChannel().asServerTextChannel();
    }

    /**
     * Gets the server thread channel of the message.
     * Only present if the message was sent in a thread in a server.
     *
     * @return The server thread channel.
     */
    default Optional<ServerThreadChannel> getServerThreadChannel() {
        return getChannel().asServerThreadChannel();
    }

    /**
     * Gets the private channel of the message.
     * Only present if the message was sent in a private conversation.
     *
     * @return The private channel.
     */
    default Optional<PrivateChannel> getPrivateChannel() {
        return getChannel().asPrivateChannel();
    }

    /**
     * Gets the server of the message.
     *
     * @return The server of the message.
     */
    default Optional<Server> getServer() {
        return getServerTextChannel()
                .map(Channel::asServerChannel)
                .orElseGet(() -> getServerThreadChannel().flatMap(Channel::asServerChannel))
                .map(ServerChannel::getServer);
    }

    /**
     * Gets up to a given amount of messages before this message.
     *
     * @param limit The limit of messages to get.
     * @return The messages.
     * @see TextChannel#getMessagesBefore(int, long)
     * @see #getMessagesBeforeAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesBefore(int limit) {
        return getChannel().getMessagesBefore(limit, this);
    }

    /**
     * Gets messages before this message until one that meets the given condition is found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @return The messages.
     * @see TextChannel#getMessagesBefore(int, long)
     * @see #getMessagesBeforeAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesBeforeUntil(Predicate<Message> condition) {
        return getChannel().getMessagesBeforeUntil(condition, this);
    }

    /**
     * Gets messages before this message while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @return The messages.
     * @see TextChannel#getMessagesBeforeWhile(Predicate, long)
     * @see #getMessagesBeforeAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesBeforeWhile(Predicate<Message> condition) {
        return getChannel().getMessagesBeforeWhile(condition, this);
    }

    /**
     * Gets a stream of messages before this message sorted from newest to oldest.
     *
     * <p>The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @return The stream.
     * @see TextChannel#getMessagesBeforeAsStream(long)
     * @see #getMessagesBefore(int)
     */
    default Stream<Message> getMessagesBeforeAsStream() {
        return getChannel().getMessagesBeforeAsStream(this);
    }

    /**
     * Gets up to a given amount of messages after this message.
     *
     * @param limit The limit of messages to get.
     * @return The messages.
     * @see TextChannel#getMessagesAfter(int, long)
     * @see #getMessagesAfterAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesAfter(int limit) {
        return getChannel().getMessagesAfter(limit, this);
    }

    /**
     * Gets messages after this message until one that meets the given condition is found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @return The messages.
     * @see TextChannel#getMessagesAfter(int, long)
     * @see #getMessagesAfterAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesAfterUntil(Predicate<Message> condition) {
        return getChannel().getMessagesAfterUntil(condition, this);
    }

    /**
     * Gets messages after this message while they meet the given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param condition The condition that has to be met.
     * @return The messages.
     * @see TextChannel#getMessagesAfterWhile(Predicate, long)
     * @see #getMessagesAfterAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesAfterWhile(Predicate<Message> condition) {
        return getChannel().getMessagesAfterWhile(condition, this);
    }

    /**
     * Gets a stream of messages after this message sorted from oldest to newest.
     *
     * <p>The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @return The stream.
     * @see TextChannel#getMessagesAfterAsStream(long)
     * @see #getMessagesAfter(int)
     */
    default Stream<Message> getMessagesAfterAsStream() {
        return getChannel().getMessagesAfterAsStream(this);
    }

    /**
     * Gets up to a given amount of messages around this message.
     * This message will be part of the result in addition to the messages around and does not count towards the limit.
     * Half of the messages will be older than this message and half of the message will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param limit The limit of messages to get.
     * @return The messages.
     * @see TextChannel#getMessagesAround(int, long)
     * @see #getMessagesAroundAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesAround(int limit) {
        return getChannel().getMessagesAround(limit, this);
    }

    /**
     * Gets messages around this message until one that meets the given condition is found.
     * If no message matches the condition, an empty set is returned.
     * This message will be part of the result in addition to the messages around and is matched against the condition
     * and will abort retrieval.
     * Half of the messages will be older than this message and half of the message will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param condition The abort condition for when to stop retrieving messages.
     * @return The messages.
     * @see TextChannel#getMessagesAround(int, long)
     * @see #getMessagesAroundAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesAroundUntil(Predicate<Message> condition) {
        return getChannel().getMessagesAroundUntil(condition, this);
    }

    /**
     * Gets messages around this message while they meet the given condition.
     * If this message does not match the condition, an empty set is returned.
     * This message will be part of the result in addition to the messages around and is matched against the condition
     * and will abort retrieval.
     * Half of the messages will be older than this message and half of the message will be newer.
     * If there aren't enough older or newer messages, the actual amount of messages will be less than the given limit.
     * It's also not guaranteed to be perfectly balanced.
     *
     * @param condition The condition that has to be met.
     * @return The messages.
     * @see TextChannel#getMessagesAroundWhile(Predicate, long)
     * @see #getMessagesAroundAsStream()
     */
    default CompletableFuture<MessageSet> getMessagesAroundWhile(Predicate<Message> condition) {
        return getChannel().getMessagesAroundWhile(condition, this);
    }

    /**
     * Gets a stream of messages around this message. The first message in the stream will be this message.
     * After that you will always get an older message and a newer message alternating as long as on both sides
     * messages are available. If only on one side further messages are available, only those are delivered further on.
     * It's not guaranteed to be perfectly balanced.
     *
     * <p>The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @return The stream.
     * @see TextChannel#getMessagesAroundAsStream(long)
     * @see #getMessagesAround(int)
     */
    default Stream<Message> getMessagesAroundAsStream() {
        return getChannel().getMessagesAroundAsStream(this);
    }

    /**
     * Gets all messages between this message and the given message, excluding the boundaries.
     *
     * @param other The id of the other boundary messages.
     * @return The messages.
     * @see TextChannel#getMessagesBetween(long, long)
     * @see #getMessagesBetweenAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBetween(long other) {
        return getChannel().getMessagesBetween(getId(), other);
    }

    /**
     * Gets all messages between this message and the given message, excluding the boundaries.
     *
     * @param other The other boundary messages.
     * @return The messages.
     * @see TextChannel#getMessagesBetween(long, long)
     * @see #getMessagesBetweenAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBetween(Message other) {
        return getMessagesBetween(other.getId());
    }

    /**
     * Gets all messages between this message and the given message, excluding the boundaries, until one that meets the
     * given condition is found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param other     The id of the other boundary messages.
     * @param condition The abort condition for when to stop retrieving messages.
     * @return The messages.
     * @see TextChannel#getMessagesBetweenUntil(Predicate, long, long)
     * @see #getMessagesBetweenAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenUntil(long other, Predicate<Message> condition) {
        return getChannel().getMessagesBetweenUntil(condition, getId(), other);
    }

    /**
     * Gets all messages between this message and the given message, excluding the boundaries, until one that meets the
     * given condition is found.
     * If no message matches the condition, an empty set is returned.
     *
     * @param other     The other boundary messages.
     * @param condition The abort condition for when to stop retrieving messages.
     * @return The messages.
     * @see TextChannel#getMessagesBetweenUntil(Predicate, long, long)
     * @see #getMessagesBetweenAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenUntil(Message other, Predicate<Message> condition) {
        return getMessagesBetweenUntil(other.getId(), condition);
    }

    /**
     * Gets all messages between this message and the given message, excluding the boundaries, while they meet the
     * given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param other     The id of the other boundary messages.
     * @param condition The condition that has to be met.
     * @return The messages.
     * @see TextChannel#getMessagesBetweenWhile(Predicate, long, long)
     * @see #getMessagesBetweenAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenWhile(long other, Predicate<Message> condition) {
        return getChannel().getMessagesBetweenWhile(condition, getId(), other);
    }

    /**
     * Gets all messages between this message and the given message, excluding the boundaries, while they meet the
     * given condition.
     * If the first message does not match the condition, an empty set is returned.
     *
     * @param other     The other boundary messages.
     * @param condition The condition that has to be met.
     * @return The messages.
     * @see TextChannel#getMessagesBetweenWhile(Predicate, long, long)
     * @see #getMessagesBetweenAsStream(long)
     */
    default CompletableFuture<MessageSet> getMessagesBetweenWhile(Message other, Predicate<Message> condition) {
        return getMessagesBetweenWhile(other.getId(), condition);
    }

    /**
     * Gets a stream of all messages between this message and the given message, excluding the boundaries, sorted from
     * this message to the given message.
     *
     * <p>The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param other The id of the other boundary messages.
     * @return The stream.
     * @see TextChannel#getMessagesBetweenAsStream(long, long)
     * @see #getMessagesBetween(long)
     */
    default Stream<Message> getMessagesBetweenAsStream(long other) {
        return getChannel().getMessagesBetweenAsStream(getId(), other);
    }

    /**
     * Gets a stream of all messages between this message and the given message, excluding the boundaries, sorted from
     * this message to the given message.
     *
     * <p>The messages are retrieved in batches synchronously from Discord,
     * so consider not using this method from a listener directly.
     *
     * @param other The other boundary messages.
     * @return The stream.
     * @see TextChannel#getMessagesBetweenAsStream(long, long)
     * @see #getMessagesBetween(long)
     */
    default Stream<Message> getMessagesBetweenAsStream(Message other) {
        return getMessagesBetweenAsStream(other.getId());
    }

    /**
     * Checks if the given user is allowed to add <b>new</b> reactions to the message.
     *
     * @param user The user to check.
     * @return Whether the given user is allowed to add <b>new</b> reactions to the message or not.
     */
    default boolean canAddNewReactions(User user) {
        Optional<ServerTextChannel> channel = getServerTextChannel();
        return !channel.isPresent()
                || channel.get().hasPermission(user, PermissionType.ADMINISTRATOR)
                || channel.get().hasPermissions(user,
                PermissionType.VIEW_CHANNEL,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.ADD_REACTIONS);
    }

    /**
     * Checks if the user of the connected account is allowed to add <b>new</b> reactions to the message.
     *
     * @return Whether the user of the connected account is allowed to add <b>new</b> reactions to the message or not.
     */
    default boolean canYouAddNewReactions() {
        return canAddNewReactions(getApi().getYourself());
    }

    /**
     * Checks if the given user can delete this message.
     *
     * @param user The user to check.
     * @return Whether the given user can delete the message or not.
     */
    default boolean canDelete(User user) {
        // You cannot delete messages in channels you cannot see
        if (!getChannel().canSee(user)) {
            return false;
        }
        // The user can see the message and is the author
        if (getAuthor().asUser().orElse(null) == user) {
            return true;
        }
        return getServerTextChannel().map(channel -> channel.canManageMessages(user)).orElse(false);
    }

    /**
     * Replies to this message with the given text.
     *
     * @param messageContent The text to reply with.
     * @return The sent message.
     */
    default CompletableFuture<Message> reply(String messageContent) {
        return new MessageBuilder().replyTo(getId()).setContent(messageContent).send(getChannel());
    }

    /**
     * Replies to this message with the given embed.
     *
     * @param embed The EmbedBuilder to reply with.
     * @return The sent message.
     */
    default CompletableFuture<Message> reply(EmbedBuilder embed) {
        return new MessageBuilder().replyTo(getId()).setEmbed(embed).send(getChannel());
    }

    /**
     * Checks if the user of the connected account can delete this message.
     *
     * @return Whether the user of the connected account can delete the message or not.
     */
    default boolean canYouDelete() {
        return canDelete(getApi().getYourself());
    }

    @Override
    default Optional<Message> getCurrentCachedInstance() {
        return getApi().getCachedMessageById(getId());
    }

    @Override
    default CompletableFuture<Message> getLatestInstance() {
        return getChannel().getMessageById(getId());
    }

    /**
     * Creates a thread for this message.
     *
     * @param name                The Thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThread(String name, AutoArchiveDuration autoArchiveDuration) {
        return getServerTextChannel()
                .map(serverTextChannel -> serverTextChannel.createThreadForMessage(this, name, autoArchiveDuration))
                .orElseThrow(() -> new IllegalStateException(
                        "In order to create a thread the channel of this message must be a ServerTextChannel"));
    }

    /**
     * Creates a thread for this message.
     *
     * @param name                The Thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThread(String name, Integer autoArchiveDuration) {
        return getServerTextChannel()
                .map(serverTextChannel -> serverTextChannel.createThreadForMessage(this, name, autoArchiveDuration))
                .orElseThrow(() -> new IllegalStateException(
                        "In order to create a thread the channel of this message must be a ServerTextChannel"));
    }
}
