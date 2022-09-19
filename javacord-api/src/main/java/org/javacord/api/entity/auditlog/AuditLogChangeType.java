package org.javacord.api.entity.auditlog;

import org.javacord.api.entity.Nameable;

/**
 * This class represents an audit log change type (sometimes also called key).
 */
public enum AuditLogChangeType implements Nameable {

    // any entity
    ID("id"),
    NAME("name"),
    TYPE("type"),

    // serverguild
    AFK_CHANNEL_ID("afk_channel_id"),
    AFK_TIMEOUT("afk_timeout"),
    BANNER("banner_hash"),
    DEFAULT_MESSAGE_NOTIFICATIONS("default_message_notifications"),
    DESCRIPTION("description"), // also sticker
    DISCOVERY_SPLASH("discovery_splash_hash"),
    EXPLICIT_CONTENT_FILTER("explicit_content_filter"),
    ICON("icon_hash"), // also role
    MFA_LEVEL("mfa_level"),
    OWNER_ID("owner_id"),
    PREFERRED_LOCALE("preferred_locale"),
    PRUNE_DELETE_DAYS("prune_delete_days"),
    PUBLIC_UPDATES_CHANNEL_ID("public_updates_channel_id"),
    REGION("region"),
    RULES_CHANNEL_ID("rules_channel_id"),
    SPLASH("splash_hash"),
    SYSTEM_CHANNEL_ID("system_channel_id"),
    VANITY_URL_CODE("vanity_url_code"),
    VERIFICATION_LEVEL("verification_level"),
    WIDGET_CHANNEL_ID("widget_channel_id"),
    WIDGET_ENABLED("widget_enabled"),
    ROLE_ADD("$add"),
    ROLE_REMOVE("$remove"),

    // channel
    APPLICATION_ID("application_id"),
    BITRATE("bitrate"),
    DEFAULT_AUTO_ARCHIVE_DURATION("default_auto_archive_duration"),
    NSFW("nsfw"),
    PERMISSION_OVERWRITES("permission_overwrites"),
    POSITION("position"),
    RATE_LIMIT_PER_USER("rate_limit_per_user"),
    TOPIC("topic"),  // also stage instance
    USER_LIMIT("user_limit"),

    // thread
    ARCHIVED("archived"),
    AUTO_ARCHIVE_DURATION("auto_archive_duration"),
    LOCKED("locked"),

    // role
    ALLOWED_PERMISSIONS("allow"),
    COLOR("color"),
    DENIED_PERMISSIONS("deny"),
    DISPLAY_SEPARATELY("hoist"),
    MENTIONABLE("mentionable"),
    PERMISSIONS("permissions"),
    UNICODE_EMOJI("unicode_emoji"),

    // sticker
    ASSET("asset"),
    AVAILABLE("available"),
    FORMAT_TYPE("format_type"),
    GUILD_ID("guild_id"),
    TAGS("tags"),

    // invite
    CHANNEL_ID("channel_id"),
    CODE("code"),
    INVITER_ID("inviter_id"),
    MAX_AGE("max_age"),
    MAX_USES("max_uses"),
    TEMPORARY("temporary"),
    USES("uses"),

    // user
    AVATAR("avatar_hash"),
    DEAF("deaf"),
    MUTE("mute"),
    NICK("nick"),

    // integration
    ENABLE_EMOTICONS("enable_emoticons"),
    EXPIRE_BEHAVIOR("expire_behavior"),
    EXPIRE_GRACE_PERIOD("expire_grace_period"),

    // stage instance
    PRIVACY_LEVEL("privacy_level"),

    /**
     * An unknown change type.
     */
    UNKNOWN("unknown");

    /**
     * The name of the type.
     */
    private final String name;

    /**
     * Creates a new audit log change type.
     *
     * @param name The name of the type.
     */
    AuditLogChangeType(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the type.
     *
     * @return The name of the type.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the type by its name.
     *
     * @param name The name of the type.
     * @return The type with the given name.
     */
    public static AuditLogChangeType fromName(String name) {
        for (AuditLogChangeType type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
