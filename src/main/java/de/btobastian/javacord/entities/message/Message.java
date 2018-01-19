package de.btobastian.javacord.entities.message;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplUnicodeEmoji;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.utils.DiscordRegexPattern;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;

/**
 * This class represents a Discord message.
 */
public interface Message extends DiscordEntity, Comparable<Message> {

    /**
     * Deletes the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, long channelId, long messageId) {
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.MESSAGE_DELETE)
                .setUrlParameters(String.valueOf(channelId), String.valueOf(messageId))
                .setRatelimitRetries(250)
                .execute(result -> null);
    }

    /**
     * Deletes the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> delete(DiscordApi api, String channelId, String messageId) {
        try {
            return delete(api, Long.parseLong(channelId), Long.parseLong(messageId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Updates the content of the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Void> edit(DiscordApi api, long channelId, long messageId, String content) {
        return edit(api, channelId, messageId, content, null);
    }

    /**
     * Updates the content of the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Void> edit(DiscordApi api, String channelId, String messageId, String content) {
        return edit(api, channelId, messageId, content, null);
    }

    /**
     * Updates the embed of the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Void> edit(DiscordApi api, long channelId, long messageId, EmbedBuilder embed) {
        return edit(api, channelId, messageId, null, embed);
    }

    /**
     * Updates the embed of the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Void> edit(DiscordApi api, String channelId, String messageId, EmbedBuilder embed) {
        return edit(api, channelId, messageId, null, embed);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Void> edit(
            DiscordApi api, long channelId, long messageId, String content, EmbedBuilder embed) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (content != null) {
            body.put("content", content);
        }
        if (embed != null) {
            embed.toJsonNode(body.putObject("embed"));
        }
        return new RestRequest<Void>(api, RestMethod.PATCH, RestEndpoint.MESSAGE)
                .setUrlParameters(String.valueOf(channelId), String.valueOf(messageId))
                .setBody(body)
                .execute(result -> null);
    }

    /**
     * Updates the content and the embed of the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param content The new content of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    static CompletableFuture<Void> edit(
            DiscordApi api, String channelId, String messageId, String content, EmbedBuilder embed) {
        try {
            return edit(api, Long.parseLong(channelId), Long.parseLong(messageId), content, embed);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Adds a unicode reaction to the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(DiscordApi api, long channelId, long messageId, String unicodeEmoji) {
        return new RestRequest<Void>(api, RestMethod.PUT, RestEndpoint.REACTION)
                .setUrlParameters(String.valueOf(channelId), String.valueOf(messageId), unicodeEmoji, "@me")
                .setRatelimitRetries(500)
                .execute(result -> null);
    }

    /**
     * Adds a unicode reaction to the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(
            DiscordApi api, String channelId, String messageId, String unicodeEmoji) {
        try {
            return addReaction(api, Long.parseLong(channelId), Long.parseLong(messageId), unicodeEmoji);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Adds a reaction to the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(DiscordApi api, long channelId, long messageId, Emoji emoji) {
        String value = emoji.asUnicodeEmoji().orElseGet(() ->
                emoji.asCustomEmoji()
                        .map(e -> e.getName() + ":" + String.valueOf(e.getId()))
                        .orElse("UNKNOWN")
        );
        return new RestRequest<Void>(api, RestMethod.PUT, RestEndpoint.REACTION)
                .setUrlParameters(String.valueOf(channelId), String.valueOf(messageId), value, "@me")
                .setRatelimitRetries(500)
                .execute(result -> null);
    }

    /**
     * Adds a reaction to the message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> addReaction(DiscordApi api, String channelId, String messageId, Emoji emoji) {
        try {
            return addReaction(api, Long.parseLong(channelId), Long.parseLong(messageId), emoji);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Deletes all reactions on this message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> removeAllReactions(DiscordApi api, long channelId, long messageId) {
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.REACTION)
                .setUrlParameters(String.valueOf(channelId), String.valueOf(messageId))
                .execute(result -> null);
    }

    /**
     * Deletes all reactions on this message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the deletion was successful.
     */
    static CompletableFuture<Void> removeAllReactions(DiscordApi api, String channelId, String messageId) {
        try {
            return removeAllReactions(api, Long.parseLong(channelId), Long.parseLong(messageId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Pins this message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the pin was successful.
     */
    static CompletableFuture<Void> pin(DiscordApi api, long channelId, long messageId) {
        return new RestRequest<Void>(api, RestMethod.PUT, RestEndpoint.PINS)
                .setUrlParameters(String.valueOf(channelId), String.valueOf(messageId))
                .execute(result -> null);
    }

    /**
     * Pins this message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the pin was successful.
     */
    static CompletableFuture<Void> pin(DiscordApi api, String channelId, String messageId) {
        try {
            return pin(api, Long.parseLong(channelId), Long.parseLong(messageId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Unpins this message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> unpin(DiscordApi api, long channelId, long messageId) {
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.PINS)
                .setUrlParameters(String.valueOf(channelId), String.valueOf(messageId))
                .execute(result -> null);
    }

    /**
     * Unpins this message.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> unpin(DiscordApi api, String channelId, String messageId) {
        try {
            return unpin(api, Long.parseLong(channelId), Long.parseLong(messageId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
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
        Matcher userMention = DiscordRegexPattern.USER_MENTION.matcher(content);
        while (userMention.find()) {
            String userId = userMention.group("id");
            Optional<User> userOptional = getApi().getCachedUserById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String userName = getServer().map(user::getDisplayName).orElseGet(user::getName);
                content = userMention.replaceFirst("@" + userName);
                userMention.reset(content);
            }
        }
        Matcher roleMention = DiscordRegexPattern.ROLE_MENTION.matcher(content);
        while (roleMention.find()) {
            String roleId = roleMention.group("id");
            String roleName = getServer()
                    .flatMap(server -> server
                            .getRoleById(roleId)
                            .map(Role::getName))
                    .orElse("deleted-role");
            content = roleMention.replaceFirst("@" + roleName);
            roleMention.reset(content);
        }
        Matcher channelMention = DiscordRegexPattern.CHANNEL_MENTION.matcher(content);
        while (channelMention.find()) {
            String channelId = channelMention.group("id");
            String channelName = getServer()
                    .flatMap(server -> server
                            .getTextChannelById(channelId)
                            .map(ServerChannel::getName))
                    .orElse("deleted-channel");
            content = channelMention.replaceFirst("#" + channelName);
            channelMention.reset(content);
        }
        Matcher customEmoji = DiscordRegexPattern.CUSTOM_EMOJI.matcher(content);
        while (customEmoji.find()) {
            String emojiId = customEmoji.group("id");
            String name = getApi()
                    .getCustomEmojiById(emojiId)
                    .map(CustomEmoji::getName)
                    .orElseGet(() -> customEmoji.group("name"));
            content = customEmoji.replaceFirst(":" + name + ":");
            customEmoji.reset(content);
        }
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
     * Gets a list with all reactions of the message.
     *
     * @return A list which contains all reactions of the message.
     */
    List<Reaction> getReactions();

    /**
     * Gets a list with all users mentioned in this message.
     *
     * @return A list with all users mentioned in this message.
     */
    List<User> getMentionedUsers();

    /**
     * Gets a list with all roles mentioned in this message.
     *
     * @return A list with all roles mentioned in this message.
     */
    List<Role> getMentionedRoles();

    /**
     * Gets a list with all channels mentioned in this message.
     *
     * @return A list with all channels mentioned in this message.
     */
    default List<ServerTextChannel> getMentionedChannels() {
        List<ServerTextChannel> mentionedChannels = new ArrayList<>();
        Matcher channelMention = DiscordRegexPattern.CHANNEL_MENTION.matcher(getContent());
        while (channelMention.find()) {
            String channelId = channelMention.group("id");
            getApi().getServerTextChannelById(channelId)
                    .filter(channel -> !mentionedChannels.contains(channel))
                    .ifPresent(mentionedChannels::add);
        }
        return mentionedChannels;
    }

    /**
     * Gets a reaction by its emoji.
     *
     * @param emoji The emoji of the reaction.
     * @return The reaction for the given emoji.
     */
    default Optional<Reaction> getReactionByEmoji(Emoji emoji) {
        return getReactions().stream().filter(reaction -> reaction.getEmoji() == emoji).findAny();
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
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> addReaction(Emoji emoji) {
        return Message.addReaction(getApi(), getChannel().getId(), getId(), emoji);
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
    default CompletableFuture<Void> addReactions(String... unicodeEmojis) {
        return addReactions(Arrays.stream(unicodeEmojis).map(ImplUnicodeEmoji::fromString).toArray(Emoji[]::new));
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
     * Removes a user from the list of reactors of a given emoji reaction.
     *
     * @param user The user to remove.
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionByEmoji(User user, Emoji emoji) {
        return Reaction.removeUser(getApi(), getChannel().getId(), getId(), emoji, user);
    }

    /**
     * Removes a user from the list of reactors of the given emoji reactions.
     *
     * @param user The user to remove.
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
     * Removes a user from the list of reactors of a given unicode emoji reaction.
     *
     * @param user The user to remove.
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionByEmoji(User user, String unicodeEmoji) {
        return removeReactionByEmoji(user, ImplUnicodeEmoji.fromString(unicodeEmoji));
    }

    /**
     * Removes a user from the list of reactors of the given unicode emoji reactions.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @param user The user to remove.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionsByEmoji(User user, String... unicodeEmojis) {
        return removeReactionsByEmoji(user, Arrays.stream(unicodeEmojis).map(ImplUnicodeEmoji::fromString).toArray(Emoji[]::new));
    }

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
     * Removes all reactors of a given unicode emoji reaction.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionByEmoji(String unicodeEmoji) {
        return removeReactionByEmoji(ImplUnicodeEmoji.fromString(unicodeEmoji));
    }

    /**
     * Removes all reactors of the given unicode emoji reactions.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeReactionsByEmoji(String... unicodeEmojis) {
        return removeReactionsByEmoji(Arrays.stream(unicodeEmojis).map(ImplUnicodeEmoji::fromString).toArray(Emoji[]::new));
    }

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
     * Removes you from the list of reactors of the given emoji reactions.
     *
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeOwnReactionsByEmoji(Emoji... emojis) {
        return removeReactionsByEmoji(getApi().getYourself(), emojis);
    }

    /**
     * Removes you from the list of reactors of a given unicode emoji reaction.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeOwnReactionByEmoji(String unicodeEmoji) {
        return removeOwnReactionByEmoji(ImplUnicodeEmoji.fromString(unicodeEmoji));
    }

    /**
     * Removes you from the list of reactors of the given unicode emoji reactions.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> removeOwnReactionsByEmoji(String... unicodeEmojis) {
        return removeOwnReactionsByEmoji(Arrays.stream(unicodeEmojis).map(ImplUnicodeEmoji::fromString).toArray(Emoji[]::new));
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
     * Gets the group channel of the message.
     * Only present if the message was sent in a group channel.
     *
     * @return The group channel.
     */
    default Optional<GroupChannel> getGroupChannel() {
        return Optional.ofNullable(getChannel() instanceof GroupChannel ? (GroupChannel) getChannel() : null);
    }

    /**
     * Gets the server of the message.
     *
     * @return The server of the message.
     */
    default Optional<Server> getServer() {
        return getServerTextChannel().map(ServerChannel::getServer);
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
        return Message.edit(getApi(), getChannel().getId(), getId(), content, embed);
    }

    /**
     * Deletes the message.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
       return Message.delete(getApi(), getChannel().getId(), getId());
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
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> unpin() {
        return Message.unpin(getApi(), getChannel().getId(), getId());
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
                    PermissionType.READ_MESSAGES,
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
     * Checks if the user of the connected account can delete this message.
     *
     * @return Whether the user of the connected account can delete the message or not.
     */
    default boolean canYouDelete() {
        return canDelete(getApi().getYourself());
    }

    /**
     * Adds a listener, which listens to this message being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {
        return getApi().addMessageDeleteListener(getId(), listener);
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
     * @return The manager of the listener.
     */
    default ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {
        return getApi().addMessageEditListener(getId(), listener);
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
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return getApi().addReactionAddListener(getId(), listener);
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
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return getApi().addReactionRemoveListener(getId(), listener);
    }

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    default List<ReactionRemoveListener> getReactionRemoveListeners() {
        return getApi().getReactionRemoveListeners(getId());
    }

    /**
     * Adds a listener, which listens to all reactions being removed at once from this message.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(
            ReactionRemoveAllListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Message.class, getId(), ReactionRemoveAllListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction remove all listeners.
     *
     * @return A list with all registered reaction remove all listeners.
     */
    default List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Message.class, getId(), ReactionRemoveAllListener.class);
    }

}
