package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class PartialMessageDto {

    private final String id;
    private final String channelId;
    @Nullable
    private final String guildId;
    @Nullable
    private final UserDto author;
    @Nullable
    private final MemberDto member;
    @Nullable
    private final String content;
    @Nullable
    private final Instant timestamp;
    @Nullable
    private final Instant editedTimestamp;
    @Nullable
    private final Boolean tts;
    @Nullable
    private final Boolean mentionEveryone;
    @Nullable
    private final UserDto[] mentions;
    @Nullable
    private final String[] mentionRoles;
    @Nullable
    private final ChannelMentionDto mentionChannels;
    @Nullable
    private final AttachmentDto[] attachments;
    @Nullable
    private final EmbedDto[] embeds;
    @Nullable
    private final ReactionDto[] reactions;
    @Nullable
    private final String nonce;
    @Nullable
    private final Boolean pinned;
    @Nullable
    private final String webhookId;
    @Nullable
    private final Integer type;
    @Nullable
    private final MessageActivityDto activity;
    @Nullable
    private final MessageApplicationDto application;
    @Nullable
    private final MessageReferenceDto messageReference;
    @Nullable
    private final Integer flags;

    @JsonCreator
    public PartialMessageDto(String id, String channelId, @Nullable String guildId, @Nullable UserDto author, @Nullable MemberDto member, @Nullable String content, @Nullable Instant timestamp, @Nullable Instant editedTimestamp, @Nullable Boolean tts, @Nullable Boolean mentionEveryone, @Nullable UserDto[] mentions, @Nullable String[] mentionRoles, @Nullable ChannelMentionDto mentionChannels, @Nullable AttachmentDto[] attachments, @Nullable EmbedDto[] embeds, @Nullable ReactionDto[] reactions, @Nullable String nonce, @Nullable Boolean pinned, @Nullable String webhookId, @Nullable Integer type, @Nullable MessageActivityDto activity, @Nullable MessageApplicationDto application, @Nullable MessageReferenceDto messageReference, @Nullable Integer flags) {
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

    public Optional<UserDto> getAuthor() {
        return Optional.ofNullable(author);
    }

    public Optional<MemberDto> getMember() {
        return Optional.ofNullable(member);
    }

    public Optional<String> getContent() {
        return Optional.ofNullable(content);
    }

    public Optional<Instant> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public Optional<Instant> getEditedTimestamp() {
        return Optional.ofNullable(editedTimestamp);
    }

    public Optional<Boolean> getTts() {
        return Optional.ofNullable(tts);
    }

    public Optional<Boolean> getMentionEveryone() {
        return Optional.ofNullable(mentionEveryone);
    }

    public Optional<UserDto[]> getMentions() {
        return Optional.ofNullable(mentions);
    }

    public Optional<String[]> getMentionRoles() {
        return Optional.ofNullable(mentionRoles);
    }

    public Optional<ChannelMentionDto> getMentionChannels() {
        return Optional.ofNullable(mentionChannels);
    }

    public Optional<AttachmentDto[]> getAttachments() {
        return Optional.ofNullable(attachments);
    }

    public Optional<EmbedDto[]> getEmbeds() {
        return Optional.ofNullable(embeds);
    }

    public Optional<ReactionDto[]> getReactions() {
        return Optional.ofNullable(reactions);
    }

    public Optional<String> getNonce() {
        return Optional.ofNullable(nonce);
    }

    public Optional<Boolean> getPinned() {
        return Optional.ofNullable(pinned);
    }

    public Optional<String> getWebhookId() {
        return Optional.ofNullable(webhookId);
    }

    public Optional<Integer> getType() {
        return Optional.ofNullable(type);
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
