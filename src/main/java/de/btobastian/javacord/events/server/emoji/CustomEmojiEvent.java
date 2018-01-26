package de.btobastian.javacord.events.server.emoji;

import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.events.server.ServerEvent;

/**
 * A custom emoji event.
 */
public abstract class CustomEmojiEvent extends ServerEvent {

    /**
     * The custom emoji of the event.
     */
    private final CustomEmoji emoji;

    /**
     * Creates a new custom emoji event.
     *
     * @param emoji The custom emoji of the event.
     */
    public CustomEmojiEvent(CustomEmoji emoji) {
        // these events are currently only dispatched from GuildEmojisUpdateHandler, so server should always be present
        super(emoji.getApi(), emoji.getServer().orElseThrow(AssertionError::new));
        this.emoji = emoji;
    }

    /**
     * Gets the custom emoji of the event.
     *
     * @return The custom emoji of the event.
     */
    public CustomEmoji getEmoji() {
        return emoji;
    }

}
