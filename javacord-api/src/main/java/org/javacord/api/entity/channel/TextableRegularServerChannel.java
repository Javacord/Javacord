package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.internal.permissions.ServerChannelTextPermissions;
import org.javacord.api.entity.webhook.WebhookBuilder;

public interface TextableRegularServerChannel extends TextChannel, Mentionable, Categorizable,
        ServerChannelTextPermissions {

    @Override
    default String getMentionTag() {
        return "<#" + getIdAsString() + ">";
    }

    /**
     * Checks is the channel is "not safe for work".
     *
     * @return Whether the channel is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Creates a webhook builder for this channel.
     *
     * @return A webhook builder.
     */
    default WebhookBuilder createWebhookBuilder() {
        return new WebhookBuilder(this);
    }

}
