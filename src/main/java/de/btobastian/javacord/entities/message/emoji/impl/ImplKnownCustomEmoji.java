package de.btobastian.javacord.entities.message.emoji.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.KnownCustomEmoji;

/**
 * The implementation of {@link CustomEmoji}.
 */
public class ImplKnownCustomEmoji extends ImplCustomEmoji implements KnownCustomEmoji {

    /**
     * The server of the emoji.
     */
    private final Server server;

    /**
     * Creates a new custom emoji.
     *
     * @param api The discord api instance.
     * @param server The server of the emoji.
     * @param data The json data of the emoji.
     */
    public ImplKnownCustomEmoji(ImplDiscordApi api, Server server, JsonNode data) {
        super(api, data);
        this.server = server;
    }

    /**
     * Creates a new custom emoji.
     *
     * @param api The discord api instance.
     * @param id The id of the emoji.
     * @param server The server of the emoji.
     * @param name The name of the emoji.
     * @param animated Whether the emoji is animated or not.
     */
    public ImplKnownCustomEmoji(ImplDiscordApi api, long id, Server server, String name, boolean animated) {
        super(api, id, name, animated);
        this.server = server;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public String toString() {
        return String.format("KnownCustomEmoji (id: %s, name: %s, animated: %b, server: %s)",
                getId(), getName(), isAnimated(), getServer());
    }
}
