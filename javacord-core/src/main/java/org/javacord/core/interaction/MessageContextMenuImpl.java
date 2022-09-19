package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.interaction.ApplicationCommandType;
import org.javacord.api.interaction.MessageContextMenu;
import org.javacord.core.DiscordApiImpl;

public class MessageContextMenuImpl extends ApplicationCommandImpl implements MessageContextMenu {

    /**
     * Class constructor.
     *
     * @param api The api instance.
     * @param data The JSON data.
     */
    public MessageContextMenuImpl(DiscordApiImpl api, JsonNode data) {
        super(api, data);
    }

    @Override
    public ApplicationCommandType getType() {
        return ApplicationCommandType.MESSAGE;
    }

}
