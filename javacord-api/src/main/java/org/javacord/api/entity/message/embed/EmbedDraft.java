package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.parts.EmbedField;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftThumbnail;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an embed draft.
 * Embed drafts are built by the EmbedBuilder class.
 * This is used to send an embed within a message.
 *
 * @see EmbedBuilder#build()
 */
public interface EmbedDraft extends EmbedBase {

    /**
     * Gets the title of the embed.
     *
     * @return The title of the embed.
     */
    Optional<String> getTitle();

    /**
     * Gets the description of the embed.
     *
     * @return The description of the embed.
     */
    Optional<String> getDescription();

    /**
     * Gets the URL of the embed.
     *
     * @return The URL of the embed.
     */
    Optional<String> getUrl();

    /**
     * Gets the timestamp of the embed.
     *
     * @return The timestamp of the embed.
     */
    Optional<Instant> getTimestamp();

    /**
     * Gets the color of the embed.
     *
     * @return The color of the embed.
     */
    Optional<Color> getColor();

    /**
     * Gets the footer object of this embed.
     *
     * @return The footer object.
     */
    Optional<EmbedDraftFooter> getEmbedDraftFooter();

    /**
     * Gets the image object of this embed.
     *
     * @return The image object.
     */
    Optional<EmbedDraftImage> getEmbedDraftImage();

    /**
     * Gets the embed author object of this embed.
     *
     * @return The author object.
     */
    Optional<EmbedDraftAuthor> getEmbedDraftAuthor();

    /**
     * Gets the embed thumbnail object of this embed.
     *
     * @return The thumbnail object.
     */
    Optional<EmbedDraftThumbnail> getEmbedDraftThumbnail();

    /**
     * Gets a list of the fields in the embed.
     *
     * @return A list of the fields in the embed.
     */
    List<EmbedField> getFields();

    /**
     * Sends this embed draft to the given messageable.
     *
     * @param messageable The messageable to send this embed to.
     * @return A future to contain the sent message.
     */
    default CompletableFuture<Message> send(Messageable messageable) {
        return messageable.sendMessage(this);
    }
}
