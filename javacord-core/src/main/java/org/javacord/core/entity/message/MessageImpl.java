package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.CountDetails;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageActivity;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.MessageReference;
import org.javacord.api.entity.message.MessageType;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.sticker.StickerItem;
import org.javacord.api.entity.user.User;
import org.javacord.api.exception.MissingIntentException;
import org.javacord.api.interaction.MessageInteraction;
import org.javacord.api.util.DiscordRegexPattern;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import org.javacord.core.entity.message.component.ActionRowImpl;
import org.javacord.core.entity.message.embed.EmbedImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.sticker.StickerItemImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.interaction.MessageInteractionImpl;
import org.javacord.core.listener.message.InternalMessageAttachableListenerManager;
import org.javacord.core.util.cache.MessageCacheImpl;

import java.awt.Color;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;

/**
 * The implementation of {@link Message}.
 */
public class MessageImpl implements Message, InternalMessageAttachableListenerManager {
    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The channel of the message.
     */
    private final TextChannel channel;

    /**
     * The id of the message.
     */
    private final long id;

    /**
     * The content of the message.
     */
    private volatile String content;

    /**
     * The components of the message.
     */
    private final List<HighLevelComponent> components = new ArrayList<>();

    /**
     * The Message Interaction Object of the message.
     */
    private final MessageInteraction messageInteraction;

    /**
     * The type of the message.
     */
    private final MessageType type;

    /**
     * The flags of the message.
     */
    private final EnumSet<MessageFlag> flags = EnumSet.noneOf(MessageFlag.class);

    /**
     * The pinned flag of the message.
     */
    private volatile boolean pinned;

    /**
     * The text-to-speech flag of the message.
     */
    private volatile boolean tts;

    /**
     * If the message mentions everyone or not.
     */
    private volatile boolean mentionsEveryone;

    /**
     * Gets the last edit time.
     */
    private volatile Instant lastEditTime;

    /**
     * The author of the message.
     */
    private MessageAuthor author;

    /**
     * The activity of the message.
     */
    private final MessageActivityImpl activity;

    /**
     * The nonce of the message.
     */
    private final String nonce;

    /**
     * The message referenced via message reply.
     */
    private final Message referencedMessage;

    /**
     * The message reference.
     */
    private final MessageReference messageReference;

    /**
     * If the message should be cached forever or not.
     */
    private volatile boolean cacheForever = false;

    /**
     * All embeds.
     */
    private final List<Embed> embeds = new ArrayList<>();

    /**
     * All reactions.
     */
    private final List<Reaction> reactions = new ArrayList<>();

    /**
     * The attachments of the message.
     */
    private final List<MessageAttachment> attachments = new ArrayList<>();

    /**
     * The users mentioned in this message.
     */
    private final List<User> mentions = new ArrayList<>();

    /**
     * The roles mentioned in this message.
     */
    private final List<Role> roleMentions = new ArrayList<>();

    /**
     * The sticker items in this message.
     */
    private final Set<StickerItem> stickerItems = new HashSet<>();

    /**
     * The approximate position of the message in a thread.
     */
    private final Integer position;

    /**
     * Creates a new message object.
     *
     * @param api     The discord api instance.
     * @param channel The channel of the message.
     * @param data    The json data of the message.
     */
    public MessageImpl(DiscordApiImpl api, TextChannel channel, JsonNode data) {
        this.api = api;
        this.channel = channel;

        id = data.get("id").asLong();

        type = MessageType.byType(data.get("type").asInt(), data.has("webhook_id"));

        Long webhookId = data.has("webhook_id") ? data.get("webhook_id").asLong() : null;
        author = new MessageAuthorImpl(this, webhookId, data);

        activity = data.hasNonNull("activity")
                ? new MessageActivityImpl(this, data.get("activity"))
                : null;

        nonce = data.hasNonNull("nonce")
                ? data.path("nonce").asText()
                : null;

        referencedMessage = data.hasNonNull("referenced_message")
                ? api.getOrCreateMessage(channel, data.get("referenced_message"))
                : null;

        messageReference = data.hasNonNull("message_reference")
                ? new MessageReferenceImpl(api, referencedMessage, data.get("message_reference"))
                : null;

        messageInteraction = data.hasNonNull("interaction")
            ? new MessageInteractionImpl(this, data.get("interaction"))
            : null;

        position = data.hasNonNull("position") ? data.get("position").asInt() : null;

        setUpdatableFields(data);

        MessageCacheImpl cache = (MessageCacheImpl) channel.getMessageCache();
        cache.addMessage(this);
    }

    /**
     * Create a new message with the given data.
     *
     * @param api               The discord api instance.
     * @param channel           The channel of the message.
     * @param id                The id of the message.
     * @param type              The type of the message.
     * @param author            The author of the message.
     * @param activity          The activity of the message.
     * @param content           The content of the message.
     * @param nonce             The nonce of the message.
     * @param referencedMessage The message referenced via message reply.
     * @param messageReference  The message reference.
     * @param messageInteraction The reference to the interaction that the message responds to.
     * @param pinned            The pinned flag of the message.
     * @param tts               The text-to-speech flag of the message.
     * @param mentionsEveryone  If the message mentions everyone or not.
     * @param lastEditTime      The last edit time.
     * @param components        The components of the message.
     * @param embeds            The embeds of the message.
     * @param reactions         The reactions of the message.
     * @param attachments       The attachments of the message.
     * @param mentions          The users mentioned in this message.
     * @param roleMentions      The roles mentioned in this message.
     * @param stickerItems      The sticker items in this message.
     * @param position          The position in this message
     */
    private MessageImpl(DiscordApiImpl api, TextChannel channel, long id, MessageType type,
                        MessageAuthor author, MessageActivityImpl activity, String content,
                        String nonce, Message referencedMessage, MessageReference messageReference,
                        boolean pinned, boolean tts, boolean mentionsEveryone,
                        Instant lastEditTime, List<HighLevelComponent> components,
                        List<Embed> embeds, List<Reaction> reactions,
                        List<MessageAttachment> attachments, List<User> mentions,
                        List<Role> roleMentions, Set<StickerItem> stickerItems, MessageInteraction messageInteraction,
                        Integer position) {
        this.api = api;
        this.channel = channel;
        this.id = id;
        this.type = type;
        this.author = author;
        this.activity = activity;
        this.content = content;
        this.nonce = nonce;
        this.referencedMessage = referencedMessage;
        this.messageReference = messageReference;
        this.messageInteraction = messageInteraction;
        this.pinned = pinned;
        this.tts = tts;
        this.mentionsEveryone = mentionsEveryone;
        this.lastEditTime = lastEditTime;
        this.components.addAll(components);
        this.embeds.addAll(embeds);
        this.reactions.addAll(reactions);
        this.attachments.addAll(attachments);
        this.mentions.addAll(mentions);
        this.roleMentions.addAll(roleMentions);
        this.stickerItems.addAll(stickerItems);
        this.position = position;
    }

    /**
     * Copy the message.
     *
     * @return The message.
     */
    public MessageImpl copyMessage() {
        return new MessageImpl(api, channel, id, type, author, activity, content, nonce, referencedMessage,
                messageReference, pinned, tts, mentionsEveryone, lastEditTime, components,
                embeds, reactions, attachments, mentions, roleMentions, stickerItems, messageInteraction, position);
    }

    /**
     * Sets the updatable fields of the message.
     *
     * @param data The json data of the message.
     */
    public void setUpdatableFields(JsonNode data) {
        if (data.has("content")) {
            content = data.get("content").asText("");
        }

        if (data.has("pinned")) {
            pinned = data.path("pinned").asBoolean(false);
        }

        if (data.has("tts")) {
            tts = data.path("tts").asBoolean(false);
        }

        if (data.has("mention_everyone")) {
            mentionsEveryone = data.path("mention_everyone").asBoolean(false);
        }

        if (data.has("edited_timestamp")) {
            if (data.get("edited_timestamp").isNull()) {
                lastEditTime = null;
            } else {
                lastEditTime = OffsetDateTime.parse(data.get("edited_timestamp").asText()).toInstant();
            }
        }

        if (data.has("embeds")) {
            embeds.clear();
            for (JsonNode embedJson : data.get("embeds")) {
                Embed embed = new EmbedImpl(embedJson);
                embeds.add(embed);
            }
        }

        if (data.has("flags")) {
            flags.clear();
            int flag = data.get("flags").asInt();
            for (MessageFlag value : MessageFlag.values()) {
                if ((flag & value.getId()) == value.getId()) {
                    flags.add(value);
                }
            }
        }

        if (data.has("author")) {
            author = new MessageAuthorImpl(this, author.getWebhookId().orElse(null), data);
        }

        if (data.has("components")) {
            components.clear();
            for (JsonNode componentJson : data.get("components")) {
                ActionRowImpl actionRow = new ActionRowImpl(componentJson);
                components.add(actionRow);
            }
        }

        if (data.has("reactions")) {
            reactions.clear();
            for (JsonNode reactionJson : data.get("reactions")) {
                Reaction reaction = new ReactionImpl(this, reactionJson);
                reactions.add(reaction);
            }
        }

        if (data.has("attachments")) {
            attachments.clear();
            for (JsonNode attachmentJson : data.get("attachments")) {
                MessageAttachment attachment = new MessageAttachmentImpl(this, attachmentJson);
                attachments.add(attachment);
            }
        }

        if (data.has("mentions")) {
            mentions.clear();
            for (JsonNode mentionJson : data.get("mentions")) {
                User user = new UserImpl(api, mentionJson, (MemberImpl) null,
                        getServer().map(ServerImpl.class::cast).orElse(null));
                mentions.add(user);
            }
        }

        if (data.has("mention_roles")) {
            roleMentions.clear();
            getServer().ifPresent(server -> {
                for (JsonNode roleMentionJson : data.get("mention_roles")) {
                    server.getRoleById(roleMentionJson.asText()).ifPresent(roleMentions::add);
                }
            });
        }

        if (data.has("sticker_items")) {
            stickerItems.clear();
            for (JsonNode stickerItemJson : data.get("sticker_items")) {
                stickerItems.add(new StickerItemImpl(api, stickerItemJson));
            }
        }
    }

    /**
     * Adds an emoji to the list of reactions.
     *
     * @param emoji The emoji.
     * @param you   Whether this reaction is used by you or not.
     * @param burstColors The HEX colors used for super reactions.
     * @param isSuperReaction Whether this reaction is a super reaction or not.
     */
    public void addReaction(Emoji emoji, boolean you, boolean isSuperReaction, List<Color> burstColors) {
        Optional<Reaction> reaction = reactions.stream().filter(r -> emoji.equalsEmoji(r.getEmoji())).findAny();
        reaction.ifPresent(r -> ((ReactionImpl) r).incrementCount(you));
        reaction.ifPresent(r ->  {
            CountDetailsImpl countDetails = (CountDetailsImpl) r.getCountDetails();
            r.getCountDetails().incrementCount(isSuperReaction);
            ((ReactionImpl) r).setCountDetails(countDetails);
        });
        if (!reaction.isPresent()) {
            CountDetails countDetails = new CountDetailsImpl(isSuperReaction);
            reactions.add(new ReactionImpl(this, emoji, 1, you, countDetails, burstColors));
        }
    }

    /**
     * Removes an emoji from the list of reactions.
     *
     * @param emoji The emoji.
     * @param you   Whether this reaction is used by you or not.
     * @param isSuperReaction Whether this reaction is a super reaction or not.
     */
    public void removeReaction(Emoji emoji, boolean you, boolean isSuperReaction) {
        Optional<Reaction> reaction = reactions.stream().filter(r -> emoji.equalsEmoji(r.getEmoji())).findAny();
        reaction.ifPresent(r -> ((ReactionImpl) r).decrementCount(you));
        reaction.ifPresent(r ->  {
            CountDetailsImpl countDetails = (CountDetailsImpl) r.getCountDetails();
            r.getCountDetails().decrementCount(isSuperReaction);
            ((ReactionImpl) r).setCountDetails(countDetails);
        });
        reactions.removeIf(r -> r.getCount() <= 0);
    }

    /**
     * Removes all reaction from this message.
     */
    public void removeAllReactionsFromCache() {
        reactions.clear();
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean canYouReadContent() {
        return api.getIntents().contains(Intent.MESSAGE_CONTENT)
                || !content.isEmpty()
                || isPrivateMessage()
                || author.getId() == api.getYourself().getId()
                || getMentionedUsers().contains(api.getYourself());
    }

    @Override
    public String getContent() {
        if (canYouReadContent()) {
            return content;
        }

        throw new MissingIntentException("The MESSAGE_CONTENT intent is required to receive the content of a message. "
                + "Please see https://javacord.org/wiki/basic-tutorials/gateway-intents.html "
                + "on how to enable this privileged intent or check with the method Message#canYouReadContent if you "
                + "have received the content in special cases like: DMs, bot mentions or if it's your own messages");
    }

    @Override
    public Optional<Instant> getLastEditTimestamp() {
        return Optional.ofNullable(lastEditTime);
    }

    @Override
    public List<MessageAttachment> getAttachments() {
        if (canYouReadContent()) {
            return Collections.unmodifiableList(attachments);
        }

        throw new MissingIntentException("The MESSAGE_CONTENT intent is required to receive attachments of a message. "
                + "Please see https://javacord.org/wiki/basic-tutorials/gateway-intents.html "
                + "on how to enable this privileged intent or check with the method Message#canYouReadContent if you "
                + "have received the attachments in special cases like: DMs, "
                + "bot mentions or if it's your own messages");
    }

    @Override
    public List<CustomEmoji> getCustomEmojis() {
        String content = getContent();
        List<CustomEmoji> emojis = new ArrayList<>();
        Matcher customEmoji = DiscordRegexPattern.CUSTOM_EMOJI.matcher(content);
        while (customEmoji.find()) {
            long id = Long.parseLong(customEmoji.group("id"));
            String name = customEmoji.group("name");
            boolean animated = customEmoji.group(0).charAt(1) == 'a';
            // TODO Maybe it would be better to cache the custom emoji objects inside the message object
            CustomEmoji emoji = getApi().getKnownCustomEmojiOrCreateCustomEmoji(id, name, animated);
            emojis.add(emoji);
        }
        return Collections.unmodifiableList(emojis);
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Optional<MessageActivity> getActivity() {
        return Optional.ofNullable(activity);
    }

    @Override
    public EnumSet<MessageFlag> getFlags() {
        return EnumSet.copyOf(flags);
    }

    @Override
    public boolean isPinned() {
        return pinned;
    }

    @Override
    public boolean isTts() {
        return tts;
    }

    @Override
    public boolean mentionsEveryone() {
        return mentionsEveryone;
    }

    @Override
    public List<Embed> getEmbeds() {
        if (canYouReadContent()) {
            return Collections.unmodifiableList(new ArrayList<>(embeds));
        }

        throw new MissingIntentException("The MESSAGE_CONTENT intent is required to receive embeds of a message. "
                + "Please see https://javacord.org/wiki/basic-tutorials/gateway-intents.html "
                + "on how to enable this privileged intent or check with the method Message#canYouReadContent if you "
                + "have received the embeds in special cases like: DMs, bot mentions or if it's your own messages");
    }

    @Override
    public MessageAuthor getAuthor() {
        return author;
    }

    @Override
    public Optional<User> getUserAuthor() {
        return author.asUser();
    }

    @Override
    public Optional<MessageReference> getMessageReference() {
        return Optional.ofNullable(messageReference);
    }

    @Override
    public Optional<Message> getReferencedMessage() {
        return Optional.ofNullable(referencedMessage);
    }

    @Override
    public boolean isCachedForever() {
        return cacheForever;
    }

    @Override
    public void setCachedForever(boolean cachedForever) {
        this.cacheForever = cachedForever;
        if (cachedForever) {
            // Just make sure it's in the cache
            ((MessageCacheImpl) channel.getMessageCache()).addMessage(this);
            ((MessageCacheImpl) channel.getMessageCache()).addCacheForeverMessage(this);
        } else {
            ((MessageCacheImpl) channel.getMessageCache()).removeCacheForeverMessage(this);
        }
    }

    @Override
    public List<Reaction> getReactions() {
        return Collections.unmodifiableList(new ArrayList<>(reactions));
    }

    @Override
    public Optional<MessageInteraction> getMessageInteraction() {
        return Optional.ofNullable(messageInteraction);
    }

    @Override
    public List<HighLevelComponent> getComponents() {
        if (canYouReadContent()) {
            return Collections.unmodifiableList(new ArrayList<>(this.components));
        }

        throw new MissingIntentException("The MESSAGE_CONTENT intent is required to receive the components "
                + "of a message. Please see https://javacord.org/wiki/basic-tutorials/gateway-intents.html "
                + "on how to enable this privileged intent or check with the method Message#canYouReadContent if you "
                + "have received the components in special cases like: DMs, "
                + "bot mentions or if it's your own messages");
    }

    @Override
    public List<User> getMentionedUsers() {
        return Collections.unmodifiableList(new ArrayList<>(mentions));
    }

    @Override
    public List<Role> getMentionedRoles() {
        return Collections.unmodifiableList(new ArrayList<>(roleMentions));
    }

    @Override
    public Optional<String> getNonce() {
        return Optional.ofNullable(nonce);
    }

    @Override
    public Set<StickerItem> getStickerItems() {
        return Collections.unmodifiableSet(stickerItems);
    }

    @Override
    public CompletableFuture<Void> addReactions(String... unicodeEmojis) {
        return addReactions(Arrays.stream(unicodeEmojis).map(UnicodeEmojiImpl::fromString).toArray(Emoji[]::new));
    }

    @Override
    public CompletableFuture<Void> removeReactionByEmoji(User user, String unicodeEmoji) {
        return removeReactionByEmoji(user, UnicodeEmojiImpl.fromString(unicodeEmoji));
    }

    @Override
    public CompletableFuture<Void> removeReactionByEmoji(String unicodeEmoji) {
        return removeReactionByEmoji(UnicodeEmojiImpl.fromString(unicodeEmoji));
    }

    @Override
    public CompletableFuture<Void> removeReactionsByEmoji(User user, String... unicodeEmojis) {
        return removeReactionsByEmoji(user,
                Arrays.stream(unicodeEmojis).map(UnicodeEmojiImpl::fromString).toArray(Emoji[]::new));
    }

    @Override
    public CompletableFuture<Void> removeReactionsByEmoji(String... unicodeEmojis) {
        return removeReactionsByEmoji(
                Arrays.stream(unicodeEmojis).map(UnicodeEmojiImpl::fromString).toArray(Emoji[]::new));
    }

    @Override
    public CompletableFuture<Void> removeOwnReactionByEmoji(String unicodeEmoji) {
        return removeOwnReactionByEmoji(UnicodeEmojiImpl.fromString(unicodeEmoji));
    }

    @Override
    public CompletableFuture<Void> removeOwnReactionsByEmoji(String... unicodeEmojis) {
        return removeOwnReactionsByEmoji(
                Arrays.stream(unicodeEmojis).map(UnicodeEmojiImpl::fromString).toArray(Emoji[]::new));
    }

    @Override
    public Optional<Integer> getPosition() {
        return Optional.ofNullable(position);
    }

    @Override
    public int compareTo(Message otherMessage) {
        return Long.compareUnsigned(getId(), otherMessage.getId());
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
                || !((o == null)
                || (getClass() != o.getClass())
                || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Message (id: %s, content: %s)", getIdAsString(), getContent());
    }
}
