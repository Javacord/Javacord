package org.javacord.api.entity.channel;

public interface FollowedChannel {
    /**
     * Gets the channel id of the channel.
     *
     * @return The channel id of the channel.
     */
    long getChannelId();

    /**
     * Gets the channel id of the channel as string.
     *
     * @return The channel id of the channel as string.
     */
    default String getChannelIdAsString() {
        try {
            return Long.toUnsignedString(getChannelId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given channel id is not a valid long value!", e);
        }
    }

    /**
     * Gets the targeted webhook id of the channel.
     *
     * @return The targeted webhook id of the channel.
     */
    long getTargetedWebhookId();

    /**
     * Gets the targeted webhook id of the channel as string.
     *
     * @return The targeted webhook id of the channel as string.
     */
    default String getTargetedWebhookIdAsString() {
        try {
            return Long.toUnsignedString(getTargetedWebhookId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given channel id is not a valid long value!", e);
        }
    }
}
