package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.Embed;

import java.util.List;
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
     * The new embeds of the message.
     */
    private final List<Embed> newEmbeds;

    /**
     * Creates a new message edit event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param newContent The new content of the message.
     * @param newEmbeds The new embeds of the message.
     */
    public MessageEditEvent(DiscordApi api, long messageId, TextChannel channel, String newContent, List<Embed> newEmbeds) {
        super(api, messageId, channel);
        this.newContent = newContent;
        this.newEmbeds = newEmbeds;
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
     * Gets the new embeds of the message.
     *
     * @return The new embeds of the message.
     */
    public List<Embed> getNewEmbeds() {
        return newEmbeds;
    }

    /**
     * Gets the old embeds of the message. It will only be present, if the message is in the cache.
     *
     * @return The old embeds of the message.
     */
    public Optional<List<Embed>> getOldEmbeds() {
        return getMessage().map(Message::getEmbeds);
    }

}
