package org.javacord.api.entity.permission;

/**
 * This enum contains all types of permissions.
 */
public enum PermissionType {

    // general
    CREATE_INSTANT_INVITE(0x0000000001L),
    KICK_MEMBERS(0x0000000002L),
    BAN_MEMBERS(0x0000000004L),
    ADMINISTRATOR(0x0000000008L),
    MANAGE_CHANNELS(0x0000000010L),
    MANAGE_SERVER(0x0000000020L),
    ADD_REACTIONS(0x0000000040L),
    VIEW_AUDIT_LOG(0x0000000080L),
    VIEW_SERVER_INSIGHTS(0x0000080000L), //VIEW_GUILD_INSIGHTS

    // chat
    READ_MESSAGES(0x0000000400L), //VIEW_CHANNEL
    SEND_MESSAGES(0x0000000800L),
    SEND_TTS_MESSAGES(0x0000001000L),
    MANAGE_MESSAGES(0x0000002000L),
    EMBED_LINKS(0x0000004000L),
    ATTACH_FILE(0x0000008000L),
    READ_MESSAGE_HISTORY(0x0000010000L),
    MENTION_EVERYONE(0x0000020000L),
    USE_EXTERNAL_EMOJIS(0x0000040000L),
    USE_EXTERNAL_STICKERS(0x2000000000L),

    // voice
    CONNECT(0x0000100000L),
    SPEAK(0x0000200000L),
    MUTE_MEMBERS(0x0000400000L),
    DEAFEN_MEMBERS(0x0000800000L),
    MOVE_MEMBERS(0x0001000000L),
    USE_VOICE_ACTIVITY(0x0002000000L), //USE_VAD
    PRIORITY_SPEAKER(0x0000000100L),
    STREAM(0x0000000200L),
    REQUEST_TO_SPEAK(0x0100000000L),
    START_EMBEDDED_ACTIVITIES(0x8000000000L),

    //threads
    MANAGE_THREADS(0x0400000000L),
    CREATE_PUBLIC_THREADS(0x0800000000L),
    CREATE_PRIVATE_THREADS(0x1000000000L),
    SEND_MESSAGES_IN_THREADS(0x4000000000L),

    // misc
    CHANGE_NICKNAME(0x0004000000L),
    MANAGE_NICKNAMES(0x0008000000L),
    MANAGE_ROLES(0x0010000000L),
    MANAGE_WEBHOOKS(0x0020000000L),
    MANAGE_EMOJIS(0x0040000000L), //MANAGE_EMOJIS_AND_STICKERS
    USE_APPLICATION_COMMANDS(0x0080000000L);

    /**
     * The value of the permission. A long where only one bit is set (e.g. <code>0b1000</code>).
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
     * @param l The long to check.
     * @return Whether the permission is "included" or not.
     */
    public boolean isSet(long l) {
        return (l & getValue()) != 0;
    }

    /**
     * Sets or unsets the type for the given long.
     *
     * @param l The long to change.
     * @param set Whether the type should be set or not.
     * @return The changed long.
     */
    public long set(long l, boolean set) {
        if (set && !isSet(l)) {
            return l + getValue();
        }
        if (!set && isSet(l)) {
            return l - getValue();
        }
        return l;
    }

}
