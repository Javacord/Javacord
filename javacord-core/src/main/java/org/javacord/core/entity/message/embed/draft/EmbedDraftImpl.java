package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Color;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.JsonNodeable;

/**
 * Implementation for {@link EmbedDraft}.
 */
public class EmbedDraftImpl implements EmbedDraft, JsonNodeable {
    protected String title;
    protected String description;
    protected String url;
    protected Instant timestamp;
    protected Color color;
    protected EmbedDraftAuthorImpl author;
    protected EmbedDraftThumbnailImpl thumbnail;
    protected EmbedDraftImageImpl image;
    protected EmbedDraftFooterImpl footer;
    protected final List<EmbedDraftField> fields;

    /**
     * Constructor.
     *
     * Members {@code author}, {@code thumbnail}, {@code image}, {@code footer} and the fields can either be constructed
     * by this constructor or passed as preconstructed objects. If an already constructed and fitting version is
     * available, that one gets used and the partial parameters are ignored.
     * If said Members already are valid {@code EmbedDraftMember}s, they get cast to their respective implementation
     * type. If that is not the case, the respective embed draft member is created using
     * {@link BaseEmbedMember#toDraftMember()}.
     *
     * All arguments may be {@code null}.
     *
     * @param title The title of the embed.
     * @param description The description of the embed.
     * @param url The URL of the embed as a String.
     * @param timestamp The timestamp of the embed as an Instant.
     * @param color The color of the embed.
     * @param author Preconstructed author for the embed.
     * @param authorName The name of the author. Ignored if {@code author} is not {@code null}.
     * @param authorUrl The URL of the author as a String. Ignored if {@code author} is not {@code null}.
     * @param authorIconUrl The URL to the icon of the author. Ignored if {@code author} is not {@code null}.
     * @param authorIconContainer The FileContainer for the author icon. Ignored if {@code author} is not {@code null}.
     * @param thumbnail Preconstructed thumbnail for the embed.
     * @param thumbnailUrl The URL to the image of the thumbnail. Ignored if {@code thumbnail} is not {@code null}.
     * @param thumbnailContainer The FileContainer for the thumbnail. Ignored if {@code thumbnail} is not {@code null}.
     * @param image Preconstructed image for the embed.
     * @param imageUrl The URL to the image. Ignored if {@code image} is not {@code null}.
     * @param imageContainer The FileContainer for the image. Ignored if {@code image} is not {@code null}.
     * @param footer Preconstructed footer for the embed.
     * @param footerText The text for the footer. Ignored if {@code footer} is not {@code null}.
     * @param footerIconUrl The URL to the icon of the footer. Ignored if {@code footer} is not {@code null}.
     * @param footerIconContainer The FileContainer for the icon. Ignored if {@code footer} is not {@code null}.
     * @param fields A list of Fields for the embed.
     */
    public EmbedDraftImpl(
            String title,
            String description,
            String url,
            Instant timestamp,
            Color color,

            BaseEmbedMember<?, ? extends EmbedDraftAuthor, ?> author,
            String authorName,
            String authorUrl,
            String authorIconUrl,
            FileContainer authorIconContainer,

            BaseEmbedMember<?, ? extends EmbedDraftThumbnail, ?> thumbnail,
            String thumbnailUrl,
            FileContainer thumbnailContainer,

            BaseEmbedMember<?, ? extends EmbedDraftImage, ?> image,
            String imageUrl,
            FileContainer imageContainer,

            BaseEmbedMember<?, ? extends EmbedDraftFooter, ?> footer,
            String footerText,
            String footerIconUrl,
            FileContainer footerIconContainer,

            List<? extends BaseEmbedField> fields
    ) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;

        if (author != null) {
            if (author instanceof EmbedDraftAuthorImpl) {
                this.author = (EmbedDraftAuthorImpl) author;
            } else {
                this.author = (EmbedDraftAuthorImpl) author.toDraftMember();
            }
        } else {
            this.author = createAuthor(authorName, authorUrl, authorIconUrl, authorIconContainer);
        }

        if (thumbnail != null) {
            if (thumbnail instanceof EmbedDraftThumbnailImpl) {
                this.thumbnail = (EmbedDraftThumbnailImpl) thumbnail;
            } else {
                this.thumbnail = (EmbedDraftThumbnailImpl) thumbnail.toDraftMember();
            }
        } else {
            this.thumbnail = createThumbnail(thumbnailUrl, thumbnailContainer);
        }

        if (image != null) {
            if (image instanceof EmbedDraftImageImpl) {
                this.image = (EmbedDraftImageImpl) image;
            } else {
                this.image = (EmbedDraftImageImpl) image.toDraftMember();
            }
        } else {
            this.image = createImage(imageUrl, imageContainer);
        }

        if (footer != null) {
            if (footer instanceof EmbedDraftFooterImpl) {
                this.footer = (EmbedDraftFooterImpl) footer;
            } else {
                this.footer = (EmbedDraftFooterImpl) footer.toDraftMember();
            }
        } else {
            this.footer = createFooter(footerText, footerIconUrl, footerIconContainer);
        }

        this.fields = new ArrayList<>();
        if (fields != null) {
            fields.stream()
                    .map(field -> {
                        if (field instanceof EmbedDraftFieldImpl) {
                            return (EmbedDraftFieldImpl) field;
                        }
                        return new EmbedDraftFieldImpl(this, field);
                    })
                    .forEachOrdered(this.fields::add);
        }
    }

    /**
     * Creates a new EmbedDraftAuthor object for this embed.
     * Returns {@code null} if {@code authorName} is {@code null}.
     *
     * @param authorName The name of the author. Cannot be {@code null}.
     * @param authorUrl The URL of the author as a String.
     * @param authorIconUrl The URL to the author icon as a String.
     * @param authorIconContainer The FileContainer for the author icon.
     * @return A new EmbedDraftAuthorImpl instance.
     */
    private EmbedDraftAuthorImpl createAuthor(
            String authorName,
            String authorUrl,
            String authorIconUrl,
            FileContainer authorIconContainer
    ) {
        EmbedDraftAuthorImpl author = new EmbedDraftAuthorImpl(this, authorName, authorUrl, authorIconUrl);
        author.attachContainer(authorIconContainer);
        return author;
    }

    /**
     * Creates a new EmbedDraftThumbnail object for this embed.
     * Returns {@code null} if both {@code thumbnailUrl} and {@code thumbnailContainer} are {@code null}.
     *
     * @param thumbnailUrl The URL to the image as a String.
     * Cannot be {@code null} if {@code thumbnailContainer} is {@code null}.
     * @param thumbnailContainer The FileContainer for the thumbnail image.
     * Cannot be {@code null} if {@code thumbnailUrl} is {@code null}.
     * @return A new EmbedDraftThumbnailImpl instance.
     */
    private EmbedDraftThumbnailImpl createThumbnail(
            String thumbnailUrl,
            FileContainer thumbnailContainer
    ) throws NullPointerException {
        if (thumbnailUrl == null && thumbnailContainer == null) {
            return null;
        }

        EmbedDraftThumbnailImpl thumbnail = new EmbedDraftThumbnailImpl(this, thumbnailUrl);
        thumbnail.attachContainer(thumbnailContainer);
        return thumbnail;
    }

    /**
     * Creates a new EmbedDraftImage object for this embed.
     * Returns {@code null} if both {@code imageUrl} and {@code imageContainer} are {@code null}.
     *
     * @param imageUrl The URL to the image as a String.
     * Cannot be {@code null} if {@code imageContainer} is {@code null}.
     * @param imageContainer The FileContainer for the image.
     * Cannot be {@code null} if {@code imageUrl} is {@code null}.
     * @return A new EmbedDraftImageImpl instance.
     */
    private EmbedDraftImageImpl createImage(
            String imageUrl,
            FileContainer imageContainer
    ) {
        if (imageUrl == null && imageContainer == null) {
            return null;
        }

        EmbedDraftImageImpl image = new EmbedDraftImageImpl(this, imageUrl);
        image.attachContainer(imageContainer);
        return image;
    }

    /**
     * Creates a new EmbedDraftFooter object for this embed.
     * Returns {@code null} if {@code footerText} is {@code null} and if both
     * {@code footerIconUrl} and {@code footerIconContainer} are {@code null}.
     *
     * @param footerText The text of the footer. Cannot be {@code null}.
     * @param footerIconUrl The URL to the footer icon as a String.
     * Cannot be {@code null} if {@code footerIconContainer} is {@code null}.
     * @param footerIconContainer The FileContainer for the footer icon.
     * Cannot be {@code null} if {@code footerIconUrl} is {@code null}.
     * @return A new EmbedDraftFooterImpl instance.
     */
    private EmbedDraftFooterImpl createFooter(
            String footerText,
            String footerIconUrl,
            FileContainer footerIconContainer
    ) {
        if (footerText == null) {
            return null;
        }
        if (footerIconUrl == null && footerIconContainer == null) {
            return null;
        }

        EmbedDraftFooterImpl footer = new EmbedDraftFooterImpl(this, footerText, footerIconUrl);
        footer.attachContainer(footerIconContainer);
        return footer;
    }

    @Override
    public EmbedDraft setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public EmbedDraft setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public EmbedDraft setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public EmbedDraft setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public EmbedDraft setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public EmbedDraft setAuthor(EmbedDraftAuthor author) {
        this.author = (EmbedDraftAuthorImpl) author;
        return this;
    }

    @Override
    public EmbedDraft modifyAuthor(Function<EmbedDraftAuthor, EmbedDraftAuthor> authorFunction) {
        if (authorFunction == null) {
            author = null;
        } else {
            EmbedDraftAuthor modify;
            if (author == null) {
                modify = new EmbedDraftAuthorImpl(this, null, null, null);
            } else {
                modify = author;
            }
            EmbedDraftAuthorImpl modified = (EmbedDraftAuthorImpl) authorFunction.apply(modify);
            if (modified == null) {
                author = null;
            } else {
                author = modified;
            } // todo These
        }
        return this;
    }

    @Override
    public EmbedDraft setThumbnail(EmbedDraftThumbnail thumbnail) {
        this.thumbnail = (EmbedDraftThumbnailImpl) thumbnail;
        return this;
    }

    @Override
    public EmbedDraft modifyThumbnail(Function<EmbedDraftThumbnail, EmbedDraftThumbnail> thumbnailFunction) {
        if (thumbnailFunction == null) {
            author = null;
        } else {
            thumbnailFunction.accept(thumbnail);
        }
        return this;
    }

    @Override
    public EmbedDraft setImage(EmbedDraftImage image) {
        this.image = (EmbedDraftImageImpl) image;
        return this;
    }

    @Override
    public EmbedDraft modifyImage(Function<EmbedDraftImage, EmbedDraftImage> imageFunction) {
        if (imageFunction == null) {
            author = null;
        } else {
            imageFunction.accept(image);
        }
        return this;
    }

    @Override
    public EmbedDraft setFooter(EmbedDraftFooter footer) {
        this.footer = (EmbedDraftFooterImpl) footer;
        return this;
    }

    @Override
    public EmbedDraft modifyFooter(Function<EmbedDraftFooter, EmbedDraftFooter> footerFunction) {
        if (footerFunction == null) {
            author = null;
        } else {
            footerFunction.accept(footer);
        }
        return this;
    }

    @Override
    public EmbedDraft addField(EmbedDraftField field) {
        this.fields.add(field);
        return this;
    }

    @Override
    public EmbedDraft modifyFields(Predicate<EmbedDraftField> fieldPredicate, Function<EmbedDraftField, EmbedDraftField> fieldFunction) {
        boolean hasNulls = false;

        for (int i = 0; i < fields.size(); i++) {
            if (fieldFunction == null) {
                fields.set(i, null);
                hasNulls = true;
            } else if (fieldPredicate == null || fieldPredicate.test(fields.get(i))) {
                fields.set(i, fieldFunction.apply(fields.get(i)));
            }
        }

        if (hasNulls) {
            fields.removeIf(Objects::isNull);
        }

        return this;
    }

    @Override
    public EmbedDraft removeFields(Predicate<EmbedDraftField> fieldPredicate) {
        return null;
    }

    @Override
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public Optional<Instant> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    @Override
    public Optional<Color> getColor() {
        return Optional.ofNullable(color);
    }

    @Override
    public Optional<EmbedDraftFooter> getFooter() {
        return Optional.ofNullable(footer);
    }

    @Override
    public Optional<EmbedDraftImage> getImage() {
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<EmbedDraftThumbnail> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    @Override
    public Optional<EmbedDraftAuthor> getAuthor() {
        return Optional.ofNullable(author);
    }

    @Override
    public List<EmbedDraftField> getFields() {
        return fields.stream()
                .map(EmbedDraftFieldImpl.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public boolean requiresAttachments() {
        return author.hasAttachment()
                || thumbnail.hasAttachment()
                || image.hasAttachment()
                || footer.hasAttachment();
    }

    /**
     * Returns a list of all FileContainer objects required for this embed to be sent.
     *
     * @return A list of all attachment containers.
     */
    public List<FileContainer> getAttachments() {
        return Stream.of(author, thumbnail, image, footer)
                .filter(Objects::nonNull)
                .map(EmbedDraftFileContainerAttachableMember::getContainer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "embed";
        ObjectNode node = frame.putObject(jsonFieldName);

        getTitle()
                .ifPresent(title -> node.put("title", title));
        getDescription()
                .ifPresent(description -> node.put("description", description));
        getUrl()
                .ifPresent(url -> node.put("url", url));
        getTimestamp()
                .map(DateTimeFormatter.ISO_INSTANT::format)
                .ifPresent(epoch -> node.put("timestamp", epoch));
        getColor()
                .map(clr -> clr.getRGB() & 0xFFFFFF)
                .ifPresent(rgb -> node.put("color", rgb));
        getAuthor()
                .map(EmbedDraftAuthorImpl.class::cast)
                .ifPresent(author -> author.applyToNode(node));
        getThumbnail()
                .map(EmbedDraftThumbnailImpl.class::cast)
                .ifPresent(thumbnail -> thumbnail.applyToNode(node));
        getImage()
                .map(EmbedDraftImageImpl.class::cast)
                .ifPresent(image -> image.applyToNode(node));
        getFooter()
                .map(EmbedDraftFooterImpl.class::cast)
                .ifPresent(footer -> footer.applyToNode(node));
        getFields()
                .stream()
                .map(EmbedDraftFieldImpl.class::cast)
                .forEach(field -> field.applyToNode(node));

        return jsonFieldName;
    }
}
