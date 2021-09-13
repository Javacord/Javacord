package org.javacord.api.entity.message;

/**
 * Represents a message flag type.
 */
public enum MessageFlag {

    /**
     * This message has been published to subscribed channels (via Channel Following).
     */
    CROSSPOSTED(1),

    /**
     * This message originated from a message in another channel (via Channel Following).
     */
    IS_CROSSPOST(2),

    /**
     * Do not include any embeds when serializing this message.
     */
    SUPPRESS_EMBEDS(4),

    /**
     * The source message for this crosspost has been deleted (via Channel Following).
     */
    SOURCE_MESSAGE_DELETED(8),

    /**
     * This message came from the urgent message system.
     */
    URGENT(16),

    /**
     * This message has an associated thread, with the same id as the message.
     */
    HAS_THREAD(32),

    /**
     * An ephemeral message (only visible for the user).
     */
    EPHEMERAL(64),

    /**
     * This message is an Interaction Response and the bot is "thinking".
     */
    LOADING(128),
    /**
     * An unknown flag.
     */
    UNKNOWN(-1);

    /**
     * The id of the flag.
     */
    private final int id;

    /**
     * Creates a new message flag type.
     *
     * @param id The id of the type.
     */
    MessageFlag(final int id) {
        this.id = id;
    }

    /**
     * Gets the id of the message flag type.
     *
     * @return The id of the message flag type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the message flag type by its id.
     *
     * @param id The id of the message flag type.
     * @return The message flag type with the given id or {@link MessageFlag#UNKNOWN} if unknown id.
     */
    public static MessageFlag getFlagTypeById(final int id) {
        for (final MessageFlag value : values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
