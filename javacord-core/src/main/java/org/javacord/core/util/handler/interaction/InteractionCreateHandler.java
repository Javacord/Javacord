package org.javacord.core.util.handler.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.event.interaction.AutocompleteCreateEvent;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.event.interaction.InteractionCreateEvent;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.event.interaction.MessageContextMenuCommandEvent;
import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.event.interaction.UserContextMenuCommandEvent;
import org.javacord.api.interaction.ApplicationCommandType;
import org.javacord.api.interaction.InteractionType;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.interaction.AutocompleteCreateEventImpl;
import org.javacord.core.event.interaction.ButtonClickEventImpl;
import org.javacord.core.event.interaction.InteractionCreateEventImpl;
import org.javacord.core.event.interaction.MessageComponentCreateEventImpl;
import org.javacord.core.event.interaction.MessageContextMenuCommandEventImpl;
import org.javacord.core.event.interaction.SelectMenuChooseEventImpl;
import org.javacord.core.event.interaction.SlashCommandCreateEventImpl;
import org.javacord.core.event.interaction.UserContextMenuCommandEventImpl;
import org.javacord.core.interaction.AutocompleteInteractionImpl;
import org.javacord.core.interaction.ButtonInteractionImpl;
import org.javacord.core.interaction.InteractionImpl;
import org.javacord.core.interaction.MessageContextMenuInteractionImpl;
import org.javacord.core.interaction.SelectMenuInteractionImpl;
import org.javacord.core.interaction.SlashCommandInteractionImpl;
import org.javacord.core.interaction.UserContextMenuInteractionImpl;
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
            long channelId = packet.get("channel_id").asLong();

            // Check if this interaction comes from a guild or a DM
            if (packet.hasNonNull("guild_id")) {
                channel = api.getTextChannelById(channelId).orElse(null);
            } else {
                UserImpl user = new UserImpl(api, packet.get("user"), (MemberImpl) null, null);
                channel = PrivateChannelImpl.getOrCreatePrivateChannel(api, channelId, user.getId(), user);
            }
        }

        int typeId = packet.get("type").asInt();
        final InteractionType interactionType = InteractionType.fromValue(typeId);
        ComponentType componentType = null;

        InteractionImpl interaction;
        switch (interactionType) {
            case APPLICATION_COMMAND:
                int applicationCommandTypeId = packet.get("data").get("type").asInt();
                ApplicationCommandType applicationCommandType =
                        ApplicationCommandType.fromValue(applicationCommandTypeId);
                switch (applicationCommandType) {
                    case SLASH:
                        interaction = new SlashCommandInteractionImpl(api, channel, packet);
                        break;
                    case USER:
                        interaction = new UserContextMenuInteractionImpl(api, channel, packet);
                        break;
                    case MESSAGE:
                        interaction = new MessageContextMenuInteractionImpl(api, channel, packet);
                        break;
                    default:
                        logger.info("Got application command interaction of unknown type <{}>. "
                                + "Please contact the developer!", applicationCommandTypeId);
                        return;
                }
                break;
            case MESSAGE_COMPONENT:
                int componentTypeId = packet.get("data").get("component_type").asInt();
                componentType = ComponentType.fromId(componentTypeId);
                switch (componentType) {
                    case BUTTON:
                        interaction = new ButtonInteractionImpl(api, channel, packet);
                        break;
                    case ACTION_ROW:
                        logger.warn("Received a message component interaction of type ACTION_ROW. This should not"
                                + " be possible.");
                        return;
                    case SELECT_MENU:
                        interaction = new SelectMenuInteractionImpl(api, channel, packet);
                        break;
                    default:
                        logger.warn("Received message component interaction of unknown type <{}>. "
                                + "Please contact the developer!", componentTypeId);
                        return;
                }
                break;
            case APPLICATION_COMMAND_AUTOCOMPLETE:
                interaction = new AutocompleteInteractionImpl(api, channel, packet);
                break;
            default:
                logger.warn("Received interaction of unknown type <{}>. "
                        + "Please contact the developer!", typeId);
                return;
        }
        InteractionCreateEvent event = new InteractionCreateEventImpl(interaction);

        ServerImpl server = (ServerImpl) interaction.getServer().orElse(null);

        api.getEventDispatcher().dispatchInteractionCreateEvent(
                server == null ? api : server,
                server,
                interaction.getChannel().orElse(null),
                interaction.getUser(),
                event);

        switch (interactionType) {
            case APPLICATION_COMMAND:
                int applicationCommandTypeId = packet.get("data").get("type").asInt();
                ApplicationCommandType applicationCommandType =
                        ApplicationCommandType.fromValue(applicationCommandTypeId);
                switch (applicationCommandType) {
                    case SLASH:
                        SlashCommandCreateEvent slashCommandCreateEvent =
                                new SlashCommandCreateEventImpl(interaction);
                        api.getEventDispatcher().dispatchSlashCommandCreateEvent(
                                server == null ? api : server,
                                server,
                                interaction.getChannel().orElse(null),
                                interaction.getUser(),
                                slashCommandCreateEvent);
                        break;
                    case USER:
                        UserContextMenuCommandEvent userContextMenuCommandEvent =
                                new UserContextMenuCommandEventImpl(interaction);
                        api.getEventDispatcher().dispatchUserContextMenuCommandEvent(
                                server,
                                server,
                                interaction.getChannel().orElse(null),
                                interaction.getUser(),
                                userContextMenuCommandEvent);
                        break;
                    case MESSAGE:
                        MessageContextMenuCommandEvent messageContextMenuCommandEvent =
                                new MessageContextMenuCommandEventImpl(interaction);
                        api.getEventDispatcher().dispatchMessageContextMenuCommandEvent(
                                server,
                                interaction.asMessageContextMenuInteraction().orElseThrow(AssertionError::new)
                                        .getTarget().getId(),
                                server,
                                interaction.getChannel().orElse(null),
                                interaction.getUser(),
                                messageContextMenuCommandEvent);
                        break;
                    default:
                        logger.info("Got application command interaction of unknown type <{}>. "
                                + "Please contact the developer!", applicationCommandTypeId);
                        return;
                }

                break;
            case MESSAGE_COMPONENT:
                MessageComponentCreateEvent messageComponentCreateEvent =
                        new MessageComponentCreateEventImpl(interaction);
                long messageId = messageComponentCreateEvent.getMessageComponentInteraction().getMessage().getId();
                api.getEventDispatcher().dispatchMessageComponentCreateEvent(
                        server == null ? api : server,
                        messageId,
                        server,
                        interaction.getChannel().orElse(null),
                        interaction.getUser(),
                        messageComponentCreateEvent);
                switch (componentType) {
                    case BUTTON:
                        ButtonClickEvent buttonClickEvent = new ButtonClickEventImpl(interaction);
                        api.getEventDispatcher().dispatchButtonClickEvent(
                                server == null ? api : server,
                                messageId,
                                server,
                                interaction.getChannel().orElse(null),
                                interaction.getUser(),
                                buttonClickEvent);
                        break;
                    case SELECT_MENU:
                        SelectMenuChooseEvent selectMenuChooseEvent = new SelectMenuChooseEventImpl(interaction);
                        api.getEventDispatcher().dispatchSelectMenuChooseEvent(
                                server == null ? api : server,
                                messageId,
                                server,
                                interaction.getChannel().orElse(null),
                                interaction.getUser(),
                                selectMenuChooseEvent);
                        break;
                    default:
                        break;
                }
                break;
            case APPLICATION_COMMAND_AUTOCOMPLETE:
                AutocompleteCreateEvent autocompleteCreateEvent =
                        new AutocompleteCreateEventImpl(interaction);
                api.getEventDispatcher().dispatchAutocompleteCreateEvent(
                        server == null ? api : server,
                        server,
                        interaction.getChannel().orElse(null),
                        interaction.getUser(),
                        autocompleteCreateEvent);
                break;
            default:
                break;
        }

    }

}
