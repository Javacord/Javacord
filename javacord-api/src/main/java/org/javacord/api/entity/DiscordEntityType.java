package org.javacord.api.entity;

/**
 * An enum containing all possible types {@link DiscordEntity}s.
 *
 * <p>Useful in combination with {@link org.javacord.api.DiscordApi#getCachedEntityById(long)}, to find out
 * whether the retrieved item is of a desired type.
 */
public enum DiscordEntityType {

    /**
     * A {@link org.javacord.api.entity.server.Server} type.
     */
    SERVER(0x00001, 0x00001),

    /**
     * An {@link org.javacord.api.entity.auditlog.AuditLogEntry} type.
     */
    AUDIT_LOG_ENTRY(0x00002, 0x00002),

    /**
     * An {@link org.javacord.api.entity.auditlog.AuditLogEntryTarget} type.
     */
    AUDIT_LOG_ENTRY_TARGET(0x00004, 0x00004),

    /**
     * A {@link org.javacord.api.entity.emoji.CustomEmoji} type.
     */
    CUSTOM_EMOJI(0x00008, 0x00008),

    /**
     * An {@link org.javacord.api.entity.user.User} type.
     */
    USER(0x00010, 0x00010),

    /**
     * A {@link org.javacord.api.entity.webhook.Webhook} type.
     */
    WEBHOOK(0x00020, 0x00020),

    /**
     * A {@link org.javacord.api.entity.permission.Role} type.
     */
    ROLE(0x00040, 0x00040),

    /**
     * A {@link org.javacord.api.entity.message.Message} type.
     */
    MESSAGE(0x00080, 0x00080),

    /**
     * A {@link org.javacord.api.entity.channel.Channel} type.
     */
    CHANNEL(0x00100, 0x00100),

    /**
     * A {@link org.javacord.api.entity.channel.TextChannel} type.
     */
    TEXT_CHANNEL(0x00200, CHANNEL.effectiveMask | 0x00200),

    /**
     * A {@link org.javacord.api.entity.channel.VoiceChannel} type.
     */
    VOICE_CHANNEL(0x00400, CHANNEL.effectiveMask | 0x00400),

    /**
     * A {@link org.javacord.api.entity.channel.ServerChannel} type.
     */
    SERVER_CHANNEL(0x00800, CHANNEL.effectiveMask | 0x00800),

    /**
     * A {@link org.javacord.api.entity.channel.ServerTextChannel} type.
     */
    SERVER_TEXT_CHANNEL(0x01000, TEXT_CHANNEL.effectiveMask | SERVER_CHANNEL.effectiveMask | 0x01000),

    /**
     * A {@link org.javacord.api.entity.channel.ServerVoiceChannel} type.
     */
    SERVER_VOICE_CHANNEL(0x02000, VOICE_CHANNEL.effectiveMask | SERVER_CHANNEL.effectiveMask | 0x02000),

    /**
     * A {@link org.javacord.api.entity.channel.PrivateChannel} type.
     */
    PRIVATE_CHANNEL(0x04000, TEXT_CHANNEL.effectiveMask | VOICE_CHANNEL.effectiveMask | 0x04000),

    /**
     * A {@link org.javacord.api.entity.channel.GroupChannel} type.
     */
    GROUP_CHANNEL(0x08000, TEXT_CHANNEL.effectiveMask | VOICE_CHANNEL.effectiveMask | 0x08000),

    /**
     * A {@link org.javacord.api.entity.channel.ChannelCategory} type.
     */
    CHANNEL_CATEGORY(0x10000, SERVER_CHANNEL.effectiveMask | 0x10000),

    /**
     * A {@link org.javacord.api.entity.message.MessageAttachment} type.
     */
    MESSAGE_ATTACHMENT(0x20000, 0x20000);

    /**
     * The simple bitmask of this particular type.
     */
    private final int ownMask;

    /**
     * The extended bitmask with supertypes.
     */
    private final int effectiveMask;

    /**
     * Constructor.
     * Uses a bitmask for this object in particular and an effective
     * masks that includes all the masks of overriding types.
     * This way, no {@code switch} statement is required.
     *
     * <p>If a type has no supertypes, the {@code effectiveMask} is equal to the {@code ownMask}.
     *
     * <p>Bitmasks are completely internal.
     *
     * @param ownMask The bitmask of this object.
     * @param effectiveMask A bitmask that also contains all masks of valid sub-types.
     */
    DiscordEntityType(int ownMask, int effectiveMask) {
        this.ownMask = ownMask;
        this.effectiveMask = effectiveMask;
    }

    /**
     * Checks whether another {@code DiscordEntityType} is either a supertype of this type or this exact type.
     *
     * @param other The other {@code DiscordEntityType} to check.
     * @return Whether this type is a supertype of this type or this exact type.
     */
    public boolean isType(DiscordEntityType other) {
        return (effectiveMask & other.ownMask) != 0;
    }

}
