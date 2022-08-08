package org.javacord.core.event.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.event.message.MessageEditEvent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The implementation of {@link MessageEditEvent}.
 */
public class MessageEditEventImpl extends RequestableMessageEventImpl implements MessageEditEvent {

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
    public MessageEditEventImpl(
            DiscordApi api, long messageId, TextChannel channel, String newContent, List<Embed> newEmbeds,
            String oldContent, List<Embed> oldEmbeds) {
        super(api, messageId, channel);
        this.newContent = newContent;
        this.newEmbeds = newEmbeds;
        this.oldContent = oldContent;
        this.oldEmbeds = oldEmbeds;
    }

    @Override
    public String getNewContent() {
        return newContent == null ? "" : newContent;
    }

    @Override
    public Optional<String> getOldContent() {
        return Optional.ofNullable(oldContent);
    }

    @Override
    public List<Embed> getNewEmbeds() {
        return newEmbeds == null ? Collections.emptyList() : newEmbeds;
    }

    @Override
    public Optional<List<Embed>> getOldEmbeds() {
        return Optional.ofNullable(oldEmbeds);
    }

}
