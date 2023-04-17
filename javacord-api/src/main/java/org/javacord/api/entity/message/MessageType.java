package org.javacord.api.entity.message;

/**
 * The enum contains all different types of messages.
 */
public enum MessageType {

    /**
     * A normal message being sent by a user.
     */
    NORMAL(0),

    /**
     * A normal message being sent by a webhook.
     */
    NORMAL_WEBHOOK(0),
    RECIPIENT_ADD(1),
    RECIPIENT_REMOVE(2),
    CALL(3),
    CHANNEL_NAME_CHANGE(4),
    CHANNEL_ICON_CHANGE(5),
    CHANNEL_PINNED_MESSAGE(6),
    SERVER_MEMBER_JOIN(7),
    USER_PREMIUM_SERVER_SUBSCRIPTION(8),
    USER_PREMIUM_SERVER_SUBSCRIPTION_TIER_1(9),
    USER_PREMIUM_SERVER_SUBSCRIPTION_TIER_2(10),
    USER_PREMIUM_SERVER_SUBSCRIPTION_TIER_3(11),
    CHANNEL_FOLLOW_ADD(12),
    SERVER_DISCOVERY_DISQUALIFIED(14),
    SERVER_DISCOVERY_REQUALIFIED(15),
    SERVER_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16),
    SERVER_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17),
    THREAD_CREATED(18),
    REPLY(19),
    SLASH_COMMAND(20),
    THREAD_STARTER_MESSAGE(21),
    GUILD_INVITE_REMINDER(22),
    CONTEXT_MENU_COMMAND(23),
    AUTO_MODERATION_ACTION(24),
    ROLE_SUBSCRIPTION_PURCHASE(25),
    INTERACTION_PREMIUM_UPSELL(26),
    STAGE_START(27),
    STAGE_END(28),
    STAGE_SPEAKER_JOIN(29),
    STAGE_TOPIC_CHANGE(31),
    SERVER_APPLICATION_PREMIUM_SUBSCRIPTION(32),

    /**
     * An unknown message type.
     */
    UNKNOWN(-1);

    /**
     * The int representing the type.
     */
    private final int type;

    /**
     * Creates a new message type.
     *
     * @param type The int representing the type.
     */
    MessageType(int type) {
        this.type = type;
    }

    /**
     * Gets the type by its int representation.
     *
     * @param type    The int representation.
     * @param webhook Whether the message was sent by a webhook or not.
     * @return The message type.
     */
    public static MessageType byType(int type, boolean webhook) {
        if (type == 0 && webhook) {
            return NORMAL_WEBHOOK;
        }
        for (MessageType value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        return UNKNOWN;
    }

}
