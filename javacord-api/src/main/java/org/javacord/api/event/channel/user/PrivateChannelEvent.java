package org.javacord.api.event.channel.user;

import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.ChannelEvent;

import java.util.Optional;

/**
 * A private channel event.
 */
public interface PrivateChannelEvent extends ChannelEvent {

    @Override
    PrivateChannel getChannel();

    /**
     * Gets the recipient of the private channel of the event.
     * A private channel always consists of yourself and one other user.
     * The user may be missing as discord doesn't always send the required information.
     *
     * @return The recipient of the private channel of the event.
     * @see PrivateChannel#getRecipient()
     */
    default Optional<User> getRecipient() {
        return getChannel().getRecipient();
    }

}
