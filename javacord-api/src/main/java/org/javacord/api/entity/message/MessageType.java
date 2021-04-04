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
    REPLY(19),
    APPLICATION_COMMAND(20),

    /**
     * An unknown message type.
     */
    UNKNOWN(-1);

    /**
     * The int representing the type.
     */
    final int type;

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
     * @param type The int representation.
     * @param webhook Whether the message was sent by a webhook or not.
     * @return The message type.
     */
    public static MessageType byType(int type, boolean webhook) {
        switch (type) {
            case 0:
                return webhook ? NORMAL_WEBHOOK : NORMAL;
            case 1:
                return RECIPIENT_ADD;
            case 2:
                return RECIPIENT_REMOVE;
            case 3:
                return CALL;
            case 4:
                return CHANNEL_NAME_CHANGE;
            case 5:
                return CHANNEL_ICON_CHANGE;
            case 6:
                return CHANNEL_PINNED_MESSAGE;
            case 7:
                return SERVER_MEMBER_JOIN;
            case 8:
                return USER_PREMIUM_SERVER_SUBSCRIPTION;
            case 9:
                return USER_PREMIUM_SERVER_SUBSCRIPTION_TIER_1;
            case 10:
                return USER_PREMIUM_SERVER_SUBSCRIPTION_TIER_2;
            case 11:
                return USER_PREMIUM_SERVER_SUBSCRIPTION_TIER_3;
            case 12:
                return CHANNEL_FOLLOW_ADD;
            case 14:
                return SERVER_DISCOVERY_DISQUALIFIED;
            case 15:
                return SERVER_DISCOVERY_REQUALIFIED;
            case 16:
                return SERVER_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING;
            case 17:
                return SERVER_DISCOVERY_GRACE_PERIOD_FINAL_WARNING;
            case 19:
                return REPLY;
            case 20:
                return APPLICATION_COMMAND;
            default:
                return UNKNOWN;
        }
    }

}
