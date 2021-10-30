package org.javacord.api.entity.permission;

/**
 * This enum contains all types of permissions.
 */
public enum PermissionType {

    // general
    CREATE_INSTANT_INVITE(0x00000001),
    KICK_MEMBERS(0x00000002),
    BAN_MEMBERS(0x00000004),
    ADMINISTRATOR(0x00000008),
    MANAGE_CHANNELS(0x00000010),
    MANAGE_SERVER(0x00000020),
    ADD_REACTIONS(0x00000040),
    VIEW_AUDIT_LOG(0x00000080),

    // chat
    READ_MESSAGES(0x00000400),
    SEND_MESSAGES(0x00000800),
    SEND_TTS_MESSAGES(0x00001000),
    MANAGE_MESSAGES(0x00002000),
    EMBED_LINKS(0x00004000),
    ATTACH_FILE(0x00008000),
    READ_MESSAGE_HISTORY(0x00010000),
    MENTION_EVERYONE(0x00020000),
    USE_EXTERNAL_EMOJIS(0x00040000),
    VIEW_SERVER_INSIGHTS(0x00080000),

    // voice
    CONNECT(0x00100000),
    SPEAK(0x00200000),
    MUTE_MEMBERS(0x00400000),
    DEAFEN_MEMBERS(0x00800000),
    MOVE_MEMBERS(0x01000000),
    USE_VOICE_ACTIVITY(0x02000000),
    PRIORITY_SPEAKER(0x00000100),
    STREAM(0x00000200),

    // misc
    CHANGE_NICKNAME(0x04000000),
    MANAGE_NICKNAMES(0x08000000),
    MANAGE_ROLES(0x10000000),
    MANAGE_WEBHOOKS(0x20000000),
    MANAGE_EMOJIS(0x40000000),
    USE_SLASH_COMMANDS(0x80000000),
    
    REQUEST_TO_SPEAK(0x0100000000),
    MANAGE_THREADS  (0x0400000000),
    CREATE_PUBLIC_THREADS(0x0800000000),
    CREATE_PRIVATE_THREADS(0x1000000000),
    USE_EXTERNAL_STICKERS(0x2000000000),
    SEND_MESSAGES_IN_THREADS(0x4000000000),
    START_EMBEDDED_ACTIVITIES(0x8000000000);

    /**
     * The value of the permission. An long where only one bit is set (e.g. <code>0b1000</code>).
     */
    private final long value;

    /**
     * Creates a new permission type.
     *
     * @param value The value of the permission type.
     */
    PermissionType(long value) {
        this.value = value;
    }

    /**
     * Gets the value of the permission type.
     *
     * @return The value.
     */
    public long getValue() {
        return value;
    }

    /**
     * Checks if the permission is "included" in the given long.
     *
     * @param i The long to check.
     * @return Whether the permission is "included" or not.
     */
    public boolean isSet(long i) {
        return (i & getValue()) != 0;
    }

    /**
     * Sets or unsets the type for the given long.
     *
     * @param i The long to change.
     * @param set Whether the type should be set or not.
     * @return The changed long.
     */
    public long set(long i, boolean set) {
        if (set && !isSet(i)) {
            return i + getValue();
        }
        if (!set && isSet(i)) {
            return i - getValue();
        }
        return i;
    }

}
