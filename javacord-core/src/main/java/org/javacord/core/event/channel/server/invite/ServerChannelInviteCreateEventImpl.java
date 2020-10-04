package org.javacord.core.event.channel.server.invite;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.event.channel.server.invite.ServerChannelInviteCreateEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

public class ServerChannelInviteCreateEventImpl extends ServerChannelEventImpl
        implements ServerChannelInviteCreateEvent {

    private final Invite invite;

    /**
     * Creates a new server channel invite create event.
     *
     * @param invite The invite of the event.
     * @param channel The channel the invite is for.
     */
    public ServerChannelInviteCreateEventImpl(Invite invite, ServerChannel channel) {
        super(channel);
        this.invite = invite;
    }

    @Override
    public Invite getInvite() {
        return invite;
    }
}
