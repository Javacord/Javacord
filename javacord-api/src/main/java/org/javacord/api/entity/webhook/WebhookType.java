package org.javacord.api.entity.webhook;

public enum WebhookType {

    /**
     * Incoming webhooks can post messages to channels with a generated token.
     * It extents {@link org.javacord.api.entity.message.Messageable}.
     */
    INCOMING(1),
    
    /**
     * Channel follower webhooks are internal webhooks
     * used with Channel Following to post new messages into channels.
     */
    CHANNEL_FOLLOWER(2),

    UNKNOWN(-1);
    
    private final int value;

    WebhookType(int value) {
        this.value = value;
    }

    /**
     * Gets integer value that represents this type.
     *
     * @return The integer value that represents this type.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets an {@code WebhookType} by its value.
     *
     * @param value The value of the webhook type.
     * @return The webhooktype for the given value,
     *         or {@link WebhookType#UNKNOWN} if there's none with the given value.
     */
    public static WebhookType fromValue(int value) {
        for (WebhookType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
