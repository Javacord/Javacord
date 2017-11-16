package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.*;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.impl.ImplEmbed;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The implementation of {@link Message}.
 */
public class ImplMessage implements Message {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

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
    private String content;

    /**
     * The type of the message.
     */
    private final MessageType type;

    /**
     * Gets the last edit time.
     */
    private Instant lastEditTime = null;

    /**
     * The author of the message.
     */
    private final MessageAuthor author;

    /**
     * If the message should be cached forever or not.
     */
    private boolean cacheForever = false;

    /**
     * As soon as we receive a message delete event, we mark the message as deleted.
     */
    private boolean deleted = false;

    /**
     * Whether the message should be kept in cache or not.
     */
    private boolean keepCached = true;

    /**
     * We use the counter to make sure a message is cached for at least 2 minutes!
     */
    private byte keepCachedCounter = 0;

    /**
     * A list with all embeds.
     */
    private List<Embed> embeds = new ArrayList<>();

    /**
     * A list with all reactions.
     */
    private List<Reaction> reactions = new ArrayList<>();

    /**
     * The attachments of the message.
     */
    private List<MessageAttachment> attachments = new ArrayList<>();

    /**
     * The users mentioned in this message.
     */
    private List<User> mentions = new ArrayList<>();

    /**
     * The roles mentioned in this message.
     */
    private List<Role> roleMentions = new ArrayList<>();

    /**
     * Creates a new message object.
     *
     * @param api The discord api instance.
     * @param channel The channel of the message.
     * @param data The json data of the message.
     */
    public ImplMessage(ImplDiscordApi api, TextChannel channel, JSONObject data) {
        this.api = api;
        this.channel = channel;

        id = Long.parseLong(data.getString("id"));
        content = data.getString("content");

        lastEditTime = data.has("edited_timestamp") && !data.isNull("edited_timestamp") ?
                OffsetDateTime.parse(data.getString("edited_timestamp")).toInstant() : null;

        type = MessageType.byType(data.getInt("type"), data.has("webhook_id"));

        Long webhookId = data.has("webhook_id") ? Long.parseLong(data.getString("webhook_id")) : null;
        author = new ImplMessageAuthor(this, webhookId, data.getJSONObject("author"));

        ImplMessageCache cache = (ImplMessageCache) channel.getMessageCache();
        cache.addMessage(this);

        JSONArray embedsJson = data.has("embeds") ? data.getJSONArray("embeds") : new JSONArray();
        for (int i = 0; i < embedsJson.length(); i++) {
            Embed embed = new ImplEmbed(embedsJson.getJSONObject(i));
            embeds.add(embed);
        }

        JSONArray reactionsJson = data.has("reactions") ? data.getJSONArray("reactions") : new JSONArray();
        for (int i = 0; i < reactionsJson.length(); i++) {
            Reaction reaction = new ImplReaction(this, reactionsJson.getJSONObject(i));
            reactions.add(reaction);
        }

        JSONArray attachmentsJson = data.has("attachments") ? data.getJSONArray("attachments") : new JSONArray();
        for (int i = 0; i < attachmentsJson.length(); i++) {
            MessageAttachment attachment = new ImplMessageAttachment(this, attachmentsJson.getJSONObject(i));
            attachments.add(attachment);
        }

        JSONArray mentionsJson = data.getJSONArray("mentions");
        for (int i = 0; i < mentionsJson.length(); i++) {
            User user = api.getOrCreateUser(mentionsJson.getJSONObject(i));
            mentions.add(user);
        }

        getServer().ifPresent(server -> {
            JSONArray roleMentionsJson = data.has("mention_roles") && !data.isNull("mention_roles") ?
                    data.getJSONArray("mention_roles") : new JSONArray();
            for (int i = 0; i < roleMentionsJson.length(); i++) {
                server.getRoleById(roleMentionsJson.getString(i)).ifPresent(roleMentions::add);
            }
        });
    }

    /**
     * Checks if the message should be kept in cache.
     *
     * @return Whether the message should be kept in cache or not.
     */
    public boolean keepCached() {
        if (keepCachedCounter <= 5) {
            // keepCached() is checked every 30 seconds.
            // This makes sure, that messages are cached for at least 2 minutes!
            keepCachedCounter++;
        }
        return keepCached || keepCachedCounter <= 5;
    }

    /**
     * Sets if the message should be kept in cache.
     *
     * @param keepCached Whether the message should be kept in cache or not.
     */
    public void setKeepCached(boolean keepCached) {
        this.keepCached = keepCached;
    }

    /**
     * Sets the content of the message.
     *
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets the last edit time of the message.
     *
     * @param lastEditTime The last edit time of the message.
     */
    public void setLastEditTime(Instant lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    /**
     * Sets the embeds of the message.
     *
     * @param embeds The embeds to set.
     */
    public void setEmbeds(List<Embed> embeds) {
        this.embeds = embeds;
    }

    /**
     * Sets the deleted flag of the message.
     *
     * @param deleted The deleted flag.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Adds an emoji to the list of reactions.
     *
     * @param emoji The emoji.
     * @param you Whether this reaction is used by you or not.
     */
    public void addReaction(Emoji emoji, boolean you) {
        Optional<Reaction> reaction = reactions.stream().filter(r -> emoji == r.getEmoji()).findAny();
        reaction.ifPresent(r -> ((ImplReaction) r).incrementCount(you));
        if (!reaction.isPresent()) {
            reactions.add(new ImplReaction(this, emoji, 1, you));
        }
    }

    /**
     * Removes an emoji from the list of reactions.
     *
     * @param emoji The emoji.
     * @param you Whether this reaction is used by you or not.
     */
    public void removeReaction(Emoji emoji, boolean you) {
        Optional<Reaction> reaction = reactions.stream().filter(r -> emoji == r.getEmoji()).findAny();
        reaction.ifPresent(r -> ((ImplReaction) r).decrementCount(you));
        reactions.removeIf(r -> r.getCount() <= 0);
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
    public String getContent() {
        return content;
    }

    @Override
    public Optional<Instant> getLastEditTimestamp() {
        return Optional.ofNullable(lastEditTime);
    }

    @Override
    public List<MessageAttachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
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
    public List<Embed> getEmbeds() {
        return Collections.unmodifiableList(embeds);
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
    public boolean isCachedForever() {
        return cacheForever;
    }

    @Override
    public void setCachedForever(boolean cachedForever) {
        this.cacheForever = cachedForever;
        if (cachedForever) {
            // Just make sure it's in the cache
            ((ImplMessageCache) channel.getMessageCache()).addMessage(this);
        }
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public List<Reaction> getReactions() {
        return new ArrayList<>(reactions);
    }

    @Override
    public List<User> getMentionedUsers() {
        return mentions;
    }

    @Override
    public List<Role> getMentionedRoles() {
        return roleMentions;
    }

    @Override
    public int compareTo(Message otherMessage) {
        return otherMessage.getCreationTimestamp().compareTo(getCreationTimestamp());
    }

    @Override
    public String toString() {
        return String.format("Message (id: %s, content: %s)", getId(), getContent());
    }

    @Override
    public int hashCode() {
        return String.valueOf(getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Message && ((Message) obj).getId() == getId();
    }
}
