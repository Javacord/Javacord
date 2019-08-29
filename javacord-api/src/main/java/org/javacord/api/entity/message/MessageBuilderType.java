package org.javacord.api.entity.message;

public enum MessageBuilderType {
    /**
     * The message being built will be sent by the botuser.
     */
    DEFAULT,

    /**
     * The message being built will be sent by a webhook.
     */
    WEBHOOK
}
