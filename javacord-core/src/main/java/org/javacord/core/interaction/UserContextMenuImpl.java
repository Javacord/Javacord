package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.interaction.ApplicationCommandType;
import org.javacord.api.interaction.UserContextMenu;
import org.javacord.core.DiscordApiImpl;

public class UserContextMenuImpl extends ApplicationCommandImpl implements UserContextMenu {

    /**
     * Class constructor.
     *
     * @param api The api instance.
     * @param data The JSON data.
     */
    public UserContextMenuImpl(DiscordApiImpl api, JsonNode data) {
        super(api, data);
    }

    @Override
    public ApplicationCommandType getType() {
        return ApplicationCommandType.USER;
    }
}
