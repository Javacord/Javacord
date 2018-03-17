package org.javacord.api.entity.auditlog;

/**
 * An enum with all action types.
 */
public enum AuditLogActionType {

    GUILD_UPDATE(1),
    CHANNEL_CREATE(10),
    CHANNEL_UPDATE(11),
    CHANNEL_DELETE(12),
    CHANNEL_OVERWRITE_CREATE(13),
    CHANNEL_OVERWRITE_UPDATE(14),
    CHANNEL_OVERWRITE_DELETE(15),
    MEMBER_KICK(20),
    MEMBER_PRUNE(21),
    MEMBER_BAN_ADD(22),
    MEMBER_BAN_REMOVE(23),
    MEMBER_UPDATE(24),
    MEMBER_ROLE_UPDATE(25),
    ROLE_CREATE(30),
    ROLE_UPDATE(31),
    ROLE_DELETE(32),
    INVITE_CREATE(40),
    INVITE_UPDATE(41),
    INVITE_DELETE(42),
    WEBHOOK_CREATE(50),
    WEBHOOK_UPDATE(51),
    WEBHOOK_DELETE(52),
    EMOJI_CREATE(60),
    EMOJI_UPDATE(61),
    EMOJI_DELETE(62),
    MESSAGE_DELETE(72),
    /**
     * An unknown action type.
     */
    UNKNOWN(-1);

    private final int value;

    /**
     * Creates a new audit log action type.
     *
     * @param value The value of the entry.
     */
    AuditLogActionType(int value) {
        this.value = value;
    }

    /**
     * Gets the value of the action type.
     *
     * @return The value of the action type.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets an {@link AuditLogActionType} by its value.
     *
     * @param value The value of the action type.
     * @return The action type for the given value,
     *         or {@link AuditLogActionType#UNKNOWN} if there's no with the given value.
     */
    public static AuditLogActionType fromValue(int value) {
        for (AuditLogActionType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
