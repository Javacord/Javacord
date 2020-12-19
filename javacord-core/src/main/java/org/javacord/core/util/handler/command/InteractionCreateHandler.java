package org.javacord.core.util.handler.command;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.command.InteractionCreateEvent;
import org.javacord.core.command.InteractionImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.command.InteractionCreateEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

/**
 * Handles the guild create packet.
 */
public class InteractionCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public InteractionCreateHandler(DiscordApi api) {
        super(api, true, "INTERACTION_CREATE");
    }

    @Override
    public void handle(JsonNode packet) {
        TextChannel channel = null;
        if (packet.hasNonNull("channel_id")) {
            channel = api.getTextChannelById(packet.get("channel_id").asLong()).orElse(null);
        }

        InteractionImpl interaction = new InteractionImpl(api, channel, packet);
        InteractionCreateEvent event = new InteractionCreateEventImpl(interaction);

        ServerImpl server = (ServerImpl) interaction.getServer().orElse(null);

        api.getEventDispatcher().dispatchInteractionCreateEvent(
                server == null ? api : server,
                server,
                interaction.getChannel().orElse(null),
                interaction.getUser(),
                event
        );
    }

}
