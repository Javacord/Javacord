package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.core.DiscordApiImpl;

public class ButtonInteractionImpl extends MessageComponentInteractionImpl implements ButtonInteraction {
    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public ButtonInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BUTTON;
    }
}
