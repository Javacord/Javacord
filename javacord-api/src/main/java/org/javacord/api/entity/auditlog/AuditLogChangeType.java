package org.javacord.api.entity.auditlog;

import org.javacord.api.entity.Nameable;

/**
 * This class represents an audit log change type (sometimes also called key).
 */
public enum AuditLogChangeType implements Nameable {

    NAME("name"),
    ICON("icon_hash"),
    SPLASH("splash_hash"),
    OWNER_ID("owner_id"),
    REGION("region"),
    AFK_CHANNEL_ID("afk_channel_id"),
    AFK_TIMEOUT("afk_timeout"),
    MFA_LEVEL("mfa_level"),
    VERIFICATION_LEVEL("verification_level"),
    EXPLICIT_CONTENT_FILTER("explicit_content_filter"),
    DEFAULT_MESSAGE_NOTIFICATIONS("default_message_notifications"),
    VANITY_URL_CODE("vanity_url_code"),
    ROLE_ADD("$add"),
    ROLE_REMOVE("$remove"),
    PRUNE_DELETE_DAYS("prune_delete_days"),
    WIDGET_ENABLED("widget_enabled"),
    WIDGET_CHANNEL_ID("widget_channel_id"),
    POSITION("position"),
    TOPIC("topic"),
    BITRATE("bitrate"),
    PERMISSION_OVERWRITES("permission_overwrites"),
    NSFW("nsfw"),
    APPLICATION_ID("application_id"),
    PERMISSIONS("permissions"),
    COLOR("color"),
    DISPLAY_SEPARATELY("hoist"),
    MENTIONABLE("mentionable"),
    ALLOWED_PERMISSIONS("allow"),
    DENIED_PERMISSIONS("deny"),
    CODE("code"),
    CHANNEL_ID("channel_id"),
    INVITER_ID("inviter_id"),
    MAX_USES("max_uses"),
    USES("uses"),
    MAX_AGE("max_age"),
    TEMPORARY("temporary"),
    DEAF("deaf"),
    MUTE("mute"),
    NICK("nick"),
    AVATAR("avatar_hash"),
    ID("id"),
    TYPE("type"),
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
