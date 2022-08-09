package org.javacord.api.entity.intent;

/**
 * Represents an intent.
 *
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#list-of-intents">Discord docs</a>
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
     * </ul>
     */
    GUILDS(0, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_MEMBER_ADD</li>
     *     <li>GUILD_MEMBER_UPDATE</li>
     *     <li>GUILD_MEMBER_REMOVE</li>
     * </ul>
     *
     * <p>Note: This is a privileged intent which must be enabled in the bots application page in the Developer portal
     *
     * @see <a href="https://discordapp.com/developers/applications">Discord developer portal</a>
     */
    GUILD_MEMBERS(1, true),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_BAN_ADD</li>
     *     <li>GUILD_BAN_REMOVE</li>
     * </ul>
     */
    GUILD_BANS(2, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_EMOJIS_UPDATE</li>
     * </ul>
     */
    GUILD_EMOJIS(3, false),

    /**
     * The following events are received.
     * <ul>
     *     <li>GUILD_INTEGRATIONS_UPDATE</li>
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
     * @see <a href="https://discordapp.com/developers/applications">Discord developer portal</a>
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
     *     <li>MESSAGE_REACTION_REMOVE_EMOJI</li>
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
     *     <li>MESSAGE_REACTION_REMOVE_EMOJI</li>
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
     * @see <a href="https://discordapp.com/developers/applications">Discord developer portal</a>
     */
    MESSAGE_CONTENT(15, true);

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
     * in the <a href="https://discordapp.com/developers/applications">developer portal</a>.
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
