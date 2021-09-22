package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.interaction.MessageContextMenuInteraction;
import org.javacord.core.DiscordApiImpl;

public class MessageContextMenuInteractionImpl extends ApplicationCommandInteractionImpl
        implements MessageContextMenuInteraction {

    private final Message target;

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public MessageContextMenuInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);
        JsonNode data = jsonData.get("data");
        String targetId = data.get("target_id").asText();
        target = api.getOrCreateMessage(channel, data.get("resolved").get("messages").get(targetId));
    }

    @Override
    public Message getTarget() {
        return target;
    }
}
