package org.javacord.event.message;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.message.embed.Embed;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A message delete event.
 */
public class MessageEditEvent extends RequestableMessageEvent {

    /**
     * The new content of the message.
     */
    private final String newContent;

    /**
     * The old content of the message. May be <code>null</code>!
     */
    private final String oldContent;

    /**
     * The new embeds of the message.
     */
    private final List<Embed> newEmbeds;

    /**
     * The old embeds of the message. May be <code>null</code>!
     */
    private final List<Embed> oldEmbeds;

    /**
     * Creates a new message edit event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param newContent The new content of the message.
     * @param newEmbeds The new embeds of the message.
     * @param oldContent The old content of the message.
     * @param oldEmbeds The old embeds of the message.
     */
    public MessageEditEvent(
            DiscordApi api, long messageId, TextChannel channel, String newContent, List<Embed> newEmbeds,
            String oldContent, List<Embed> oldEmbeds)
    {
        super(api, messageId, channel);
        this.newContent = newContent;
        this.newEmbeds = newEmbeds;
        this.oldContent = oldContent;
        this.oldEmbeds = oldEmbeds;
    }

    /**
     * Gets the new content of the message.
     *
     * @return The new content of the message.
     */
    public String getNewContent() {
        return newContent == null ? "" : newContent;
    }

    /**
     * Gets the old content of the message. It will only be present, if the message is in the cache.
     *
     * @return The old content of the message.
     */
    public Optional<String> getOldContent() {
        return Optional.ofNullable(oldContent);
    }

    /**
     * Gets the new embeds of the message.
     *
     * @return The new embeds of the message.
     */
    public List<Embed> getNewEmbeds() {
        return newEmbeds == null ? Collections.emptyList() : newEmbeds;
    }

    /**
     * Gets the old embeds of the message. It will only be present, if the message is in the cache.
     *
     * @return The old embeds of the message.
     */
    public Optional<List<Embed>> getOldEmbeds() {
        return Optional.ofNullable(oldEmbeds);
    }

}
