package org.javacord.core.util.handler.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.event.interaction.InteractionCreateEvent;
import org.javacord.api.interaction.InteractionType;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.interaction.InteractionCreateEventImpl;
import org.javacord.core.interaction.ApplicationCommandInteractionImpl;
import org.javacord.core.interaction.ButtonInteractionImpl;
import org.javacord.core.interaction.InteractionImpl;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

/**
 * Handles the guild create packet.
 */
public class InteractionCreateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(InteractionCreateHandler.class);

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
        InteractionImpl interaction;
        int type = packet.get("type").asInt();
        switch (InteractionType.fromValue(type)) {
            case APPLICATION_COMMAND:
                interaction = new ApplicationCommandInteractionImpl(api, channel, packet);
                break;
            case MESSAGE_COMPONENT:
                int componentType = packet.get("data").get("component_type").asInt();
                switch (ComponentType.fromId(componentType)) {
                    case BUTTON:
                        interaction = new ButtonInteractionImpl(api, channel, packet);
                        break;
                    case ACTION_ROW:
                        logger.warn("Received a message component interaction of type ACTION_ROW. This should not"
                                + " be possible.");
                        return;
                    default:
                        logger.warn(String.format("Received message component interaction of unknown type %s. "
                                + "Please contact the developer!", componentType));
                        return;
                }
                break;
            default:
                logger.warn(String.format("Received interaction of unknown type %s. "
                        + "Please contact the developer!", type));
                return;
        }
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
