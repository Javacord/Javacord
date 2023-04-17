package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.interaction.ApplicationCommandInteraction;
import org.javacord.api.interaction.InteractionType;
import org.javacord.core.DiscordApiImpl;
import java.util.Optional;

public class ApplicationCommandInteractionImpl extends InteractionImpl implements ApplicationCommandInteraction {

    protected final long commandId;
    protected final String commandName;

    protected final Long registeredCommandServerId;

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public ApplicationCommandInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);

        JsonNode data = jsonData.get("data");
        commandId = data.get("id").asLong();
        commandName = data.get("name").asText();
        registeredCommandServerId = data.hasNonNull("guild_id") ? data.get("guild_id").asLong() : null;
    }

    @Override
    public InteractionType getType() {
        return InteractionType.APPLICATION_COMMAND;
    }

    @Override
    public long getCommandId() {
        return commandId;
    }

    @Override
    public String getCommandIdAsString() {
        return String.valueOf(commandId);
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public Optional<Long> getRegisteredCommandServerId() {
        return Optional.ofNullable(registeredCommandServerId);
    }
}
