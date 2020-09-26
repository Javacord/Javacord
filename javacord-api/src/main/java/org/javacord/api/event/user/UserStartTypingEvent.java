package org.javacord.api.event.user;

import org.javacord.api.event.channel.TextChannelEvent;

/**
 * A event when a user starts typing.
 * If the user starts typing the "xyz is typing..." message is displayed for 5 seconds.
 * It also stops if the user sent a message.
 */
public interface UserStartTypingEvent extends TextChannelEvent, OptionalUserEvent {

}
