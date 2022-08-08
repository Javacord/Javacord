package org.javacord.api.event.server.member;

import org.javacord.api.event.channel.TextChannelEvent;
import org.javacord.api.event.user.OptionalUserEvent;

/**
 * An event when a user starts typing.
 * If the user starts typing the "xyz is typing..." message is displayed for 5 seconds.
 * It also stops if the user sent a message.
 */
public interface ServerMemberUserStartTypingEvent extends TextChannelEvent, OptionalUserEvent {

}
