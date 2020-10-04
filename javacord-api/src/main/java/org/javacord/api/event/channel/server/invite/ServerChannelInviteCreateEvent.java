package org.javacord.api.event.channel.server.invite;

import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.event.channel.server.ServerChannelEvent;

/**
 * A server channel invite create event.
 */
public interface ServerChannelInviteCreateEvent extends ServerChannelEvent {

    /**
     * Gets the invite of the event.
     *
     * @return The invite of the event.
     */
    Invite getInvite();

}
