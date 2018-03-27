package org.javacord.core.event.user;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserStartTypingEvent;

/**
 * The implementation of {@link UserStartTypingEvent}.
 */
public class UserStartTypingEventImpl extends ChannelUserEventImpl implements UserStartTypingEvent {

    /**
     * Creates a new user start typing event.
     *
     * @param user The user of the event.
     * @param channel The text channel of the event.
     */
    public UserStartTypingEventImpl(User user, TextChannel channel) {
        super(user, channel);
    }

}
