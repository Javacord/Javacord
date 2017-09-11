package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;

/**
 * A text channel event which effects a user.
 */
public abstract class TextChannelEvent extends UserEvent {

    /**
     * The text channel of the event.
     */
    private final TextChannel channel;

    /**
     * Creates a new text channel event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param channel The text channel of the event.
     */
    public TextChannelEvent(DiscordApi api, User user, TextChannel channel) {
        super(api, user);
        this.channel = channel;
    }

    /**
     * Gets the text channel of the event.
     *
     * @return The text channel of the event.
     */
    public TextChannel getChannel() {
        return channel;
    }
}
