package org.javacord.core.util.handler.channel.invite;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.server.invite.ServerChannelInviteCreateEvent;
import org.javacord.core.entity.server.invite.InviteImpl;
import org.javacord.core.event.channel.server.invite.ServerChannelInviteCreateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

public class InviteCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public InviteCreateHandler(DiscordApi api) {
        super(api, true, "INVITE_CREATE");
    }

    @Override
    protected void handle(JsonNode packet) {
        InviteImpl invite = new InviteImpl(api, packet);
        invite.getServer().ifPresent(server -> {
            //An error here means a missing channel for the invite, which should have already thrown an error
            ServerChannel channel = invite.getChannel().orElseThrow(AssertionError::new);
            ServerChannelInviteCreateEvent event = new ServerChannelInviteCreateEventImpl(invite, channel);
            api.getEventDispatcher().dispatchServerChannelInviteCreateEvent(
                    (DispatchQueueSelector) server, server, event);
        });
    }
}
