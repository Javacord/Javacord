package org.javacord.event.user.impl;

import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.user.User;
import org.javacord.event.user.UserStartTypingEvent;

/**
 * The implementation of {@link UserStartTypingEvent}.
 */
public class ImplUserStartTypingEvent extends ImplTextChannelUserEvent implements UserStartTypingEvent {

    /**
     * Creates a new user start typing event.
     *
     * @param user The user of the event.
     * @param channel The text channel of the event.
     */
    public ImplUserStartTypingEvent(User user, TextChannel channel) {
        super(user, channel);
    }

}
