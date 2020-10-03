package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class MessageDto {

    private final String id;
    private final String channelId;
    @Nullable
    private final String guildId;
    private final UserDto author;
    @Nullable
    private final MemberDto member;
    private final String content;
    private final Instant timestamp;
    @Nullable
    private final Instant editedTimestamp;
    private final boolean tts;
    private final boolean mentionEveryone;
    private final UserDto[] mentions;
    private final String[] mentionRoles;
    @Nullable
    private final ChannelMentionDto mentionChannels;
    private final AttachmentDto[] attachments;
    private final EmbedDto[] embeds;
    @Nullable
    private final ReactionDto[] reactions;
    @Nullable
    private final String nonce;
    private final boolean pinned;
    @Nullable
    private final String webhookId;
    private final int type;
    @Nullable
    private final MessageActivityDto activity;
    @Nullable
    private final MessageApplicationDto application;
    @Nullable
    private final MessageReferenceDto messageReference;
    @Nullable
    private final Integer flags;

    @JsonCreator
    public MessageDto(String id, String channelId, @Nullable String guildId, UserDto author, @Nullable MemberDto member, String content, Instant timestamp, @Nullable Instant editedTimestamp, boolean tts, boolean mentionEveryone, UserDto[] mentions, String[] mentionRoles, @Nullable ChannelMentionDto mentionChannels, AttachmentDto[] attachments, EmbedDto[] embeds, @Nullable ReactionDto[] reactions, @Nullable String nonce, boolean pinned, @Nullable String webhookId, int type, @Nullable MessageActivityDto activity, @Nullable MessageApplicationDto application, @Nullable MessageReferenceDto messageReference, @Nullable Integer flags) {
        this.id = id;
        this.channelId = channelId;
        this.guildId = guildId;
        this.author = author;
        this.member = member;
        this.content = content;
        this.timestamp = timestamp;
        this.editedTimestamp = editedTimestamp;
        this.tts = tts;
        this.mentionEveryone = mentionEveryone;
        this.mentions = mentions;
        this.mentionRoles = mentionRoles;
        this.mentionChannels = mentionChannels;
        this.attachments = attachments;
        this.embeds = embeds;
        this.reactions = reactions;
        this.nonce = nonce;
        this.pinned = pinned;
        this.webhookId = webhookId;
        this.type = type;
        this.activity = activity;
        this.application = application;
        this.messageReference = messageReference;
        this.flags = flags;
    }

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public Optional<String> getGuildId() {
        return Optional.ofNullable(guildId);
    }

    public UserDto getAuthor() {
        return author;
    }

    public Optional<MemberDto> getMember() {
        return Optional.ofNullable(member);
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Optional<Instant> getEditedTimestamp() {
        return Optional.ofNullable(editedTimestamp);
    }

    public boolean isTts() {
        return tts;
    }

    public boolean isMentionEveryone() {
        return mentionEveryone;
    }

    public UserDto[] getMentions() {
        return mentions;
    }

    public String[] getMentionRoles() {
        return mentionRoles;
    }

    public Optional<ChannelMentionDto> getMentionChannels() {
        return Optional.ofNullable(mentionChannels);
    }

    public AttachmentDto[] getAttachments() {
        return attachments;
    }

    public EmbedDto[] getEmbeds() {
        return embeds;
    }

    public Optional<ReactionDto[]> getReactions() {
        return Optional.ofNullable(reactions);
    }

    public Optional<String> getNonce() {
        return Optional.ofNullable(nonce);
    }

    public boolean isPinned() {
        return pinned;
    }

    public Optional<String> getWebhookId() {
        return Optional.ofNullable(webhookId);
    }

    public int getType() {
        return type;
    }

    public Optional<MessageActivityDto> getActivity() {
        return Optional.ofNullable(activity);
    }

    public Optional<MessageApplicationDto> getApplication() {
        return Optional.ofNullable(application);
    }

    public Optional<MessageReferenceDto> getMessageReference() {
        return Optional.ofNullable(messageReference);
    }

    public Optional<Integer> getFlags() {
        return Optional.ofNullable(flags);
    }
}
