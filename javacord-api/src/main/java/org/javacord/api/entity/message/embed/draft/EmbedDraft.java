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

    /**
     * Used to modify the image object of the embed.
     * If no image was set before, this method creates a new EmbedDraftImage object which then can be modified.
     *
     * @param imageFunction A function to modify the image. Can return {@code null} to remove the image.
     * @return This instance of the embed.
     */
    EmbedDraft modifyImage(Function<EmbedDraftImage, EmbedDraftImage> imageFunction);

    /**
     * Used to modify the footer object of the embed.
     * If no footer was set before, this method creates a new EmbedDraftFooter object which then can be modified.
     *
     * @param footerFunction A function to modify the footer. Can return {@code null} to remove the footer.
     * @return This instance of the embed.
     */
    EmbedDraft modifyFooter(Function<EmbedDraftFooter, EmbedDraftFooter> footerFunction);

    @Override
    Optional<EmbedDraftFooter> getFooter();

    @Override
    Optional<EmbedDraftImage> getImage();

    @Override
    Optional<EmbedDraftThumbnail> getThumbnail();

    @Override
    Optional<EmbedDraftAuthor> getAuthor();

    @Override
    List<EmbedDraftField> getFields();

    /**
     * Adds a field to the embed and modifies the field using the given function.
     *
     * @param fieldFunction The function to be used to modify the added field.
     * @return This instance of the embed.
     */
    EmbedDraft addField(Function<EmbedDraftField, EmbedDraftField> fieldFunction);

    /**
     * Modifies all fields that fit the predicate using the given function.
     *
     * @param fieldPredicate The predicate to use for checking which fields are to be removed.
     * @param fieldFunction The function to be used to modify the fields.
     * @return This instance of the embed.
     */
    EmbedDraft modifyFields(
            Predicate<EmbedDraftField> fieldPredicate,
            Function<EmbedDraftField, EmbedDraftField> fieldFunction
    );

    /**
     * Modifies all fields using the given function.
     *
     * @param fieldFunction The function to be used to modify the fields.
     * @return This instance of the embed.
     */
    default EmbedDraft modifyAllFields(Function<EmbedDraftField, EmbedDraftField> fieldFunction) {
        return modifyFields(any -> true, fieldFunction);
    }

    /**
     * Removes all fields that fit the predicate.
     *
     * @param fieldPredicate The predicate to use for checking which fields are to be removed.
     * @return This instance of the embed.
     */
    EmbedDraft removeFields(Predicate<EmbedDraftField> fieldPredicate);

    /**
     * Removes all fields from the embed.
     *
     * @return This instance of the embed.
     */
    default EmbedDraft removeAllFields() {
        return removeFields(any -> true);
    }

    /**
     * Returns whether the embed requires any attachments to be uploaded.
     *
     * @return Whether the embed requires any attachments to be uploaded.
     */
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
