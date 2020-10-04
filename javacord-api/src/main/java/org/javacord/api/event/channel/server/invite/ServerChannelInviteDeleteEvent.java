package org.javacord.api.event.channel.server.invite;

import org.javacord.api.event.channel.server.ServerChannelEvent;

/**
 * A server channel invite delete event.
 */
public interface ServerChannelInviteDeleteEvent extends ServerChannelEvent {

    /**
     * Gets the code associated with the deleted invite.
     *
     * @return The code for the deleted invite.
     */
    String getCode();

}
