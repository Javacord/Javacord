package org.javacord.api.entity.server;

/**
 * System channel flags for various server notifications.
 */
public enum SystemChannelFlag {

    /**
     * Suppress member join notifications.
     */
    SUPPRESS_JOIN_NOTIFICATIONS(1 << 0),
    /**
     * Suppress server boost notifications.
     */
    SUPPRESS_PREMIUM_SUBSCRIPTIONS(1 << 1),
    /**
     * Suppress server setup tips.
     */
    SUPPRESS_GUILD_REMINDER_NOTIFICATIONS(1 << 2),
    /**
     * Hide member join sticker reply buttons.
     */
    SUPPRESS_JOIN_NOTIFICATION_REPLIES(1 << 3),
    /**
     * Suppress role subscription purchase and renewal notifications.
     */
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATIONS(1 << 4),
    /**
     * Hide role subscription sticker reply buttons.
     */
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATION_REPLIES(1 << 5);

    private final int flag;

    /**
     * Class constructor.
     *
     * @param flag The bitmask of the flag.
     */
    SystemChannelFlag(int flag) {
        this.flag = flag;
    }

    /**
     * Gets the integer value of the flag.
     *
     * @return The integer value of the flag.
     */
    public int asInt() {
        return flag;
    }
}
