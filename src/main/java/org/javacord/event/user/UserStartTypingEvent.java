package org.javacord.event.user;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.user.User;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.user.User;

/**
 * A event when a user starts typing.
 * If the user starts typing the "xyz is typing..." message is displayed for 5 seconds.
 * It also stops if the user sent a message.
 */
public class UserStartTypingEvent extends TextChannelUserEvent {

    /**
     * Creates a new user start typing event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param channel The text channel of the event.
     */
    public UserStartTypingEvent(DiscordApi api, User user, TextChannel channel) {
        super(api, user, channel);
    }

}
