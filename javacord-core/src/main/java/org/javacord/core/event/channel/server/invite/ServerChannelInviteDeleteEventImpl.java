package org.javacord.core.event.channel.server.invite;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.server.invite.ServerChannelInviteDeleteEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

public class ServerChannelInviteDeleteEventImpl extends ServerChannelEventImpl
        implements ServerChannelInviteDeleteEvent {

    private final String code;

    /**
     * Creates a new server channel invite delete event.
     *
     * @param code The code for this invite.
     * @param channel The invite of the event.
     */
    public ServerChannelInviteDeleteEventImpl(String code, ServerChannel channel) {
        super(channel);
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
