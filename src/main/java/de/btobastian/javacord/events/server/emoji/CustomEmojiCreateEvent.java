package de.btobastian.javacord.events.server.emoji;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.events.server.ServerEvent;

/**
 * A custom emoji create event.
 */
public class CustomEmojiCreateEvent extends ServerEvent {

    /**
     * The created emoji.
     */
    private final CustomEmoji emoji;

    /**
     * Creates a new custom emoji create event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param emoji The created emoji.
     */
    public CustomEmojiCreateEvent(DiscordApi api, Server server, CustomEmoji emoji) {
        super(api, server);
        this.emoji = emoji;
    }

    /**
     * Gets the created emoji.
     *
     * @return The created emoji.
     */
    public CustomEmoji getEmoji() {
        return emoji;
    }
}
