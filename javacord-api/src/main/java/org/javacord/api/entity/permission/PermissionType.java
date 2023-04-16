package org.javacord.api.entity.permission;

/**
 * This enum contains all types of permissions.
 */
public enum PermissionType {

    /**
     * Allows creation of instant invites.
     */
    CREATE_INSTANT_INVITE(1L << 0),
    /**
     * Allows kicking members.
     */
    KICK_MEMBERS(1L << 1),
    /**
     * Allows banning members.
     */
    BAN_MEMBERS(1L << 2),
    /**
     * Allows all permissions and bypasses channel permission overwrites.
     */
    ADMINISTRATOR(1L << 3),
    /**
     * Allows management and editing of channels.
     */
    MANAGE_CHANNELS(1L << 4),
    /**
     * Allows management and editing of the server.
     */
    MANAGE_SERVER(1L << 5),
    /**
     * Allows for the addition of reactions to messages.
     */
    ADD_REACTIONS(1L << 6),
    /**
     * Allows for viewing of audit logs.
     */
    VIEW_AUDIT_LOG(1L << 7),
    /**
     * Allows for using priority speaker in a voice channel.
     */
    PRIORITY_SPEAKER(1L << 8),
    /**
     * Allows the user to go live.
     */
    STREAM(1L << 9),
    /**
     * Allows server members to view a channel,
     * which includes reading messages in text channels and joining voice channels.
     */
    VIEW_CHANNEL(1L << 10),
    /**
     * Allows for sending messages in a channel and creating threads in a forum
     * (does not allow sending messages in threads).
     */
    SEND_MESSAGES(1L << 11),
    /**
     * Allows for sending of /tts messages.
     */
    SEND_TTS_MESSAGES(1L << 12),
    /**
     * Allows for deletion of other users messages.
     */
    MANAGE_MESSAGES(1L << 13),
    /**
     * Links sent by users with this permission will be auto-embedded.
     */
    EMBED_LINKS(1L << 14),
    /**
     * Allows for uploading images and files.
     */
    ATTACH_FILES(1L << 15),
    /**
     * Allows for reading of message history.
     */
    READ_MESSAGE_HISTORY(1L << 16),
    /**
     * Allows for using the @everyone tag to notify all users in a channel,
     * and the @here tag to notify all online users in a channel.
     */
    MENTION_EVERYONE(1L << 17),
    /**
     * Allows the usage of custom emojis from other servers.
     */
    USE_EXTERNAL_EMOJIS(1L << 18),
    /**
     * Allows for viewing server insights.
     */
    VIEW_SERVER_INSIGHTS(1L << 19),
    /**
     * Allows for joining of a voice channel.
     */
    CONNECT(1L << 20),
    /**
     * Allows for speaking in a voice channel.
     */
    SPEAK(1L << 21),
    /**
     * Allows for muting members in a voice channel.
     */
    MUTE_MEMBERS(1L << 22),
    /**
     * Allows for deafening of members in a voice channel.
     */
    DEAFEN_MEMBERS(1L << 23),
    /**
     * Allows for moving of members between voice channels.
     */
    MOVE_MEMBERS(1L << 24),
    /**
     * Allows for using voice-activity-detection in a voice channel.
     */
    USE_VAD(1L << 25),
    /**
     * Allows for modification of own nickname.
     */
    CHANGE_NICKNAME(1L << 26),
    /**
     * Allows for modification of other users nicknames.
     */
    MANAGE_NICKNAMES(1L << 27),
    /**
     * Allows management and editing of roles.
     */
    MANAGE_ROLES(1L << 28),
    /**
     * Allows management and editing of webhooks.
     */
    MANAGE_WEBHOOKS(1L << 29),
    /**
     * Allows management and editing of emojis, stickers, and soundboard sounds.
     */
    MANAGE_SERVER_EXPRESSIONS(1L << 30),
    /**
     * Allows members to use application commands, including slash commands and context menu commands..
     */
    USE_APPLICATION_COMMANDS(1L << 31),
    /**
     * Allows for requesting to speak in stage channels.
     * (This permission is under active development and may be changed or removed.).
     */
    REQUEST_TO_SPEAK(1L << 32),
    /**
     * Allows for creating, editing, and deleting scheduled events.
     */
    MANAGE_EVENTS(1L << 33),
    /**
     * Allows for deleting and archiving threads, and viewing all private threads.
     */
    MANAGE_THREADS(1L << 34),
    /**
     * Allows for creating public and announcement threads.
     */
    CREATE_PUBLIC_THREADS(1L << 35),
    /**
     * Allows for creating private threads.
     */
    CREATE_PRIVATE_THREADS(1L << 36),
    /**
     * Allows the usage of custom stickers from other servers.
     */
    USE_EXTERNAL_STICKERS(1L << 37),
    /**
     * Allows for sending messages in threads.
     */
    SEND_MESSAGES_IN_THREADS(1L << 38),
    /**
     * Allows for using Activities (applications with the EMBEDDED flag) in a voice channel.
     */
    USE_EMBEDDED_ACTIVITIES(1L << 39),
    /**
     * Allows for timing out users to prevent them from sending or reacting to messages in chat and threads,
     * and from speaking in voice and stage channels.
     */
    MODERATE_MEMBERS(1L << 40),
    /**
     * Allows for viewing role subscription insights.
     */
    VIEW_CREATOR_MONETIZATION_ANALYTICS(1L << 41),
    /**
     * Allows for using soundboard in a voice channel.
     */
    USE_SOUNDBOARD(1L << 42),
    /**
     * Allows for sending voice messages in text channels.
     */
    SEND_VOICE_MESSAGES(1L << 46);

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
     * @param l   The long to change.
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
