package org.javacord.api.entity.webhook;

public enum WebhookType {
    /**
     * Channel follower webhooks are internal webhooks
     * used with Channel Following to post new messages into channels.
     */
    CHANNEL_FOLLOWER,

    /**
     * Incoming webhooks can post messages to channels with a generated token.
     * It extents {@link org.javacord.api.entity.message.Messageable}.
     */
    INCOMING;
}
