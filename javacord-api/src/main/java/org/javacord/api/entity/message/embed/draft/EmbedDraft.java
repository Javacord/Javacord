package org.javacord.api.entity.message.embed.draft;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.EmbedBuilder;

/**
 * Representation of an unsent embed that can be sent to discord.
 */
public interface EmbedDraft extends BaseEmbed {
    /**
     * Sets the title of the embed.
     *
     * @param title The title to set. Can be {@code null}.
     * @return This instance of the embed.
     */
    EmbedDraft setTitle(String title);

    /**
     * Sets the description of the embed.
     *
     * @param description The description to set. Can be {@code null}.
     * @return This instance of the embed.
     */
    EmbedDraft setDescription(String description);

    /**
     * Sets the URL of the embed.
     *
     * @param url The URL as a String to set. Can be {@code null}.
     * @return This instance of the embed.
     */
    EmbedDraft setUrl(String url);

    /**
     * Sets the timestamp of the embed.
     *
     * @param timestamp The timestamp as an instant to set. Can be {@code null}.
     * @return This instance of the embed.
     */
    EmbedDraft setTimestamp(Instant timestamp);

    /**
     * Sets the color of the embed.
     *
     * @param color The color to set. Can be {@code null}.
     * @return This instance of the embed.
     */
    EmbedDraft setColor(Color color);

    /**
     * Used to modify the author object of the embed.
     * If no author was set before, this method creates a new EmbedDraftAuthor object which then can be modified.
     *
     * @param authorFunction A function to modify the author. Can return {@code null} to remove the author.
     * @return This instance of the embed.
     */
    EmbedDraft modifyAuthor(Function<EmbedDraftAuthor, EmbedDraftAuthor> authorFunction);

    /**
     * Used to modify the thumbnail object of the embed.
     * If no thumbnail was set before, this method creates a new EmbedDraftThumbnail object which then can be modified.
     *
     * @param thumbnailFunction A function to modify the thumbnail. Can return {@code null} to remove the thumbnail.
     * @return This instance of the embed.
     */
    EmbedDraft modifyThumbnail(Function<EmbedDraftThumbnail, EmbedDraftThumbnail> thumbnailFunction);

    EmbedDraft modifyImage(Function<EmbedDraftImage, EmbedDraftImage> imageFunction);

    EmbedDraft modifyFooter(Function<EmbedDraftFooter, EmbedDraftFooter> footerFunction);

    EmbedDraft addField(EmbedDraftField field);

    EmbedDraft modifyFields(
            Predicate<EmbedDraftField> fieldPredicate,
            Function<EmbedDraftField, EmbedDraftField> fieldFunction
    );

    @Override
    Optional<EmbedDraftFooter> getFooter();

    EmbedDraft setFooter(EmbedDraftFooter footer);

    @Override
    Optional<EmbedDraftImage> getImage();

    EmbedDraft setImage(EmbedDraftImage image);

    @Override
    Optional<EmbedDraftThumbnail> getThumbnail();

    EmbedDraft setThumbnail(EmbedDraftThumbnail thumbnail);

    @Override
    Optional<EmbedDraftAuthor> getAuthor();

    EmbedDraft setAuthor(EmbedDraftAuthor author);

    @Override
    List<EmbedDraftField> getFields();

    default EmbedDraft modifyAllFields(Function<EmbedDraftField, EmbedDraftField> fieldFunction) {
        return modifyFields(any -> true, fieldFunction);
    }

    EmbedDraft removeFields(Predicate<EmbedDraftField> fieldPredicate);

    default EmbedDraft removeAllFields() {
        return removeFields(any -> true);
    }

    boolean requiresAttachments();

    @Override
    default EmbedBuilder toBuilder() {
        EmbedBuilder builder = new EmbedBuilder();
        getTitle().ifPresent(builder::setTitle);
        getDescription().ifPresent(builder::setDescription);
        getUrl().ifPresent(builder::setUrl);
        getTimestamp().ifPresent(builder::setTimestamp);
        getColor().ifPresent(builder::setColor);
        getAuthor().ifPresent(builder::setAuthor);
        getThumbnail().ifPresent(builder::setThumbnail);
        getImage().ifPresent(builder::setImage);
        getFooter().ifPresent(builder::setFooter);
        getFields().forEach(builder::addField);
        return builder;
    }
}
