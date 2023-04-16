package org.javacord.api.entity.intent;

/**
 * Represents an intent.
 *
 * @see <a href="https://discord.com/developers/docs/topics/gateway#list-of-intents">Discord docs</a>
 */
public enum Intent {

    /**
     * The following event are received.
     * <ul>
     *     <li>GUILD_CREATE</li>
     *     <li>GUILD_UPDATE</li>
     *     <li>GUILD_DELETE</li>
     *     <li>GUILD_ROLE_CREATE</li>
     *     <li>GUILD_ROLE_UPDATE</li>
     *     <li>GUILD_ROLE_DELETE</li>
     *     <li>CHANNEL_CREATE</li>
     *     <li>CHANNEL_UPDATE</li>
     *     <li>CHANNEL_DELETE</li>
     *     <li>CHANNEL_PINS_UPDATE</li>
     *     <li>THREAD_CREATE</li>
     *     <li>THREAD_UPDATE</li>
     *     <li>THREAD_DELETE</li>
     *     <li>THREAD_LIST_SYNC</li>
     *     <li>THREAD_MEMBER_UPDATE (not implemented)</li>
     *     <li>THREAD_MEMBERS_UPDATE</li>
     *     <li>STAGE_INSTANCE_CREATE (not implemented)</li>
     *     <li>STAGE_INSTANCE_UPDATE (not implemented)</li>
     *     <li>STAGE_INSTANCE_DELETE (not implemented)</li>
     * </ul>
     */
    GUILDS(0, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_MEMBER_ADD</li>
     *     <li>GUILD_MEMBER_UPDATE</li>
     *     <li>GUILD_MEMBER_REMOVE</li>
     *     <li>THREAD_MEMBERS_UPDATE (adds additional information to the members when using the GUILDS intent)</li>
     * </ul>
     *
     * <p>Note: This is a privileged intent which must be enabled in the bots application page in the Developer portal
     *
     * @see <a href="https://discord.com/developers/applications">Discord developer portal</a>
     */
    GUILD_MEMBERS(1, true),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_AUDIT_LOG_ENTRY_CREATE (not implemented)</li>
     *     <li>GUILD_BAN_ADD</li>
     *     <li>GUILD_BAN_REMOVE</li>
     * </ul>
     */
    GUILD_MODERATION(2, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_EMOJIS_UPDATE</li>
     *     <li>GUILD_STICKERS_UPDATE</li>
     * </ul>
     */
    GUILD_EMOJIS_AND_STICKERS(3, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>INTEGRATION_CREATE (not implemented)</li>
     *     <li>GUILD_INTEGRATIONS_UPDATE</li>
     *     <li>INTEGRATION_DELETE (not implemented)</li>
     * </ul>
     */
    GUILD_INTEGRATIONS(4, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>WEBHOOKS_UPDATE</li>
     * </ul>
     */
    GUILD_WEBHOOKS(5, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>INVITE_CREATE</li>
     *     <li>INVITE_DELETE</li>
     * </ul>
     */
    GUILD_INVITES(6, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>VOICE_STATE_UPDATE</li>
     * </ul>
     */
    GUILD_VOICE_STATES(7, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>PRESENCE_UPDATE</li>
     * </ul>
     *
     * <p>Note: This is a privileged intent which must be enabled in the bots application page in the Developer portal
     *
     * @see <a href="https://discord.com/developers/applications">Discord developer portal</a>
     */
    GUILD_PRESENCES(8, true),

    /**
     * The following events are received.
     * <ul>
     *     <li>MESSAGE_CREATE</li>
     *     <li>MESSAGE_UPDATE</li>
     *     <li>MESSAGE_DELETE</li>
     *     <li>MESSAGE_DELETE_BULK</li>
     * </ul>
     */
    GUILD_MESSAGES(9, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>MESSAGE_REACTION_ADD</li>
     *     <li>MESSAGE_REACTION_REMOVE</li>
     *     <li>MESSAGE_REACTION_REMOVE_ALL</li>
     *     <li>MESSAGE_REACTION_REMOVE_EMOJI (not implemented)</li>
     * </ul>
     */
    GUILD_MESSAGE_REACTIONS(10, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>TYPING_START</li>
     * </ul>
     */
    GUILD_MESSAGE_TYPING(11, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>CHANNEL_CREATE</li>
     *     <li>MESSAGE_CREATE</li>
     *     <li>MESSAGE_UPDATE</li>
     *     <li>MESSAGE_DELETE</li>
     *     <li>CHANNEL_PINS_UPDATE</li>
     * </ul>
     */
    DIRECT_MESSAGES(12, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>MESSAGE_REACTION_ADD</li>
     *     <li>MESSAGE_REACTION_REMOVE</li>
     *     <li>MESSAGE_REACTION_REMOVE_ALL</li>
     *     <li>MESSAGE_REACTION_REMOVE_EMOJI (not implemented)</li>
     * </ul>
     */
    DIRECT_MESSAGE_REACTIONS(13, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>TYPING_START</li>
     * </ul>
     */
    DIRECT_MESSAGE_TYPING(14, false),

    /**
     * This is required to receive non-empty values for content fields (content, attachments, embeds and components).
     *
     * <p>This doesn't apply for DMs, messages your bot sends, or messages in which your bot is mentioned.
     *
     * <p>Note: This is a privileged intent which must be enabled in the bots application page in the Developer portal
     *
     * @see <a href="https://discord.com/developers/applications">Discord developer portal</a>
     */
    MESSAGE_CONTENT(15, true),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_SCHEDULED_EVENT_CREATE (not implemented)</li>
     *     <li>GUILD_SCHEDULED_EVENT_UPDATE (not implemented)</li>
     *     <li>GUILD_SCHEDULED_EVENT_DELETE (not implemented)</li>
     *     <li>GUILD_SCHEDULED_EVENT_USER_ADD (not implemented)</li>
     *     <li>GUILD_SCHEDULED_EVENT_USER_REMOVE (not implemented)</li>
     * </ul>
     */
    GUILD_SCHEDULED_EVENTS(16, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>AUTO_MODERATION_RULE_CREATE (not implemented)</li>
     *     <li>AUTO_MODERATION_RULE_UPDATE (not implemented)</li>
     *     <li>AUTO_MODERATION_RULE_DELETE (not implemented)</li>
     * </ul>
     */
    AUTO_MODERATION_CONFIGURATION(20, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>AUTO_MODERATION_ACTION_EXECUTION (not implemented)</li>
     * </ul>
     */
    AUTO_MODERATION_EXECUTION(21, false);

    private final int id;

    private final boolean privileged;

    /**
     * Class constructor.
     *
     * @param id         The id of the intent.
     * @param privileged Whether the intent is privileged or not.
     */
    Intent(int id, boolean privileged) {
        this.id = id;
        this.privileged = privileged;
    }

    /**
     * Gets the id of the intent.
     *
     * @return The id of the intent.
     */
    public int getId() {
        return id;
    }

    /**
     * Check if the intent is privileged.
     *
     * <p>Privileged intents are usually related so sensitive data and must manually be enabled
     * in the <a href="https://discord.com/developers/applications">developer portal</a>.
     *
     * @return Whether this intent is privileged or not.
     */
    public boolean isPrivileged() {
        return privileged;
    }

    /**
     * Gets the bitmask from the given intents.
     *
     * @param intents An array of intents.
     * @return The calculated bitmask.
     */
    public static int calculateBitmask(Intent... intents) {
        int intentCount = 0;
        for (Intent intentValue : intents) {
            intentCount += (1 << intentValue.getId());
        }
        return intentCount;
    }

}
