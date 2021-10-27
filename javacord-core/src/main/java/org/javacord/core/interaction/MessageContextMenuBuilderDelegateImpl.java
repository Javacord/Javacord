package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.ApplicationCommandType;
import org.javacord.api.interaction.MessageContextMenu;
import org.javacord.api.interaction.internal.MessageContextMenuBuilderDelegate;
import org.javacord.core.DiscordApiImpl;

public class MessageContextMenuBuilderDelegateImpl extends ApplicationCommandBuilderDelegateImpl<MessageContextMenu>
        implements MessageContextMenuBuilderDelegate {

    /**
     * Gets the JSON body for this application command.
     *
     * @return The JSON of this application command.
     */
    @Override
    public ObjectNode getJsonBodyForApplicationCommand() {
        ObjectNode jsonBody = super.getJsonBodyForApplicationCommand();

        jsonBody.put("type", ApplicationCommandType.MESSAGE.getValue());

        return jsonBody;
    }

    @Override
    public MessageContextMenu createInstance(DiscordApiImpl api, JsonNode jsonNode) {
        return new MessageContextMenuImpl(api, jsonNode);
    }
}
