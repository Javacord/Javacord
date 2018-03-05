package org.javacord.event.user;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.GroupChannel;
import org.javacord.entity.channel.PrivateChannel;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.user.User;

import java.util.Optional;

/**
 * A text channel event which effects a user.
 */
public abstract class TextChannelUserEvent extends UserEvent {

    /**
     * The text channel of the event.
     */
    private final TextChannel channel;

    /**
     * Creates a new text channel user event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param channel The text channel of the event.
     */
    public TextChannelUserEvent(DiscordApi api, User user, TextChannel channel) {
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

    /**
     * Gets the server text channel of the event.
     * Not present, if the channel is not a server text channel.
     *
     * @return The server text channel of the event.
     */
    public Optional<ServerTextChannel> getServerTextChannel() {
        return channel.asServerTextChannel();
    }

    /**
     * Gets the private channel of the event.
     * Not present, if the channel is not a pricvate channel.
     *
     * @return The pricate channel of the event.
     */
    public Optional<PrivateChannel> getPrivateChannel() {
        return channel.asPrivateChannel();
    }

    /**
     * Gets the group channel of the event.
     * Not present, if the channel is not a group channel.
     *
     * @return The group channel of the event.
     */
    public Optional<GroupChannel> getGroupChannel() {
        return channel.asGroupChannel();
    }
}
