package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.Embed;

import java.util.Optional;

/**
 * A message delete event.
 */
public class MessageEditEvent extends OptionalMessageEvent {

    /**
     * The new content of the message.
     */
    private final String newContent;

    /**
     * The new embed of the message. May be <code>null</code>.
     */
    private final Embed newEmbed;

    /**
     * Creates a new message edit event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public MessageEditEvent(DiscordApi api, long messageId, TextChannel channel, String newContent, Embed newEmbed) {
        super(api, messageId, channel);
        this.newContent = newContent;
        this.newEmbed = newEmbed;
    }

    /**
     * Gets the new content of the message.
     *
     * @return The new content of the message.
     */
    public String getNewContent() {
        return newContent;
    }

    /**
     * Gets the old content of the message. It will only be present, if the message is in the cache.
     *
     * @return The old content of the message.
     */
    public Optional<String> getOldContent() {
        return getMessage().map(Message::getContent);
    }

    /**
     * Gets the new embed of the message. May not be present, if the new message does not have an embed.
     *
     * @return The new embed of the message.
     */
    public Optional<Embed> getNewEmbed() {
        return Optional.ofNullable(newEmbed);
    }

    /**
     * Gets the old embed of the message.
     * The outer optional is only present, if the old message is in the cache.
     * The inner optional is only present, if the old message had an embed.
     *
     * @return The old embed of the message.
     */
    public Optional<Optional<Embed>> getOldEmbed() {
        return getMessage().map(Message::getEmbed);
    }

}
