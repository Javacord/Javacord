package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Color;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedField;
import org.javacord.api.entity.message.embed.sent.SentEmbedFooter;
import org.javacord.api.entity.message.embed.sent.SentEmbedImage;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.JsonNodeable;

public class EmbedDraftImpl implements EmbedDraft, JsonNodeable {
    protected final List<EmbedDraftField> fields;
    protected String title;
    protected String description;
    protected String url;
    protected Instant timestamp;
    protected Color color;
    protected EmbedDraftAuthorImpl author;
    protected EmbedDraftThumbnailImpl thumbnail;
    protected EmbedDraftImageImpl image;
    protected EmbedDraftFooterImpl footer;

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
        fields.stream()
                .map(field -> new EmbedDraftFieldImpl(this, field))
                .forEachOrdered(this.fields::add);
    }

    @SuppressWarnings("unchecked") // False Negative
    public static <D extends EmbedDraftMember> D createFrom(BaseEmbed parent, SentEmbedMember member) {
        if (member instanceof SentEmbedAuthor) {
            return (D) new EmbedDraftAuthorImpl(parent, (SentEmbedAuthor) member);
        } else if (member instanceof SentEmbedField) {
            return (D) new EmbedDraftFieldImpl(parent, (SentEmbedField) member);
        } else if (member instanceof SentEmbedFooter) {
            return (D) new EmbedDraftFooterImpl(parent, (SentEmbedFooter) member);
        } else if (member instanceof SentEmbedImage) {
            return (D) new EmbedDraftImageImpl(parent, (SentEmbedImage) member);
        } else if (member instanceof SentEmbedThumbnail) {
            return (D) new EmbedDraftThumbnailImpl(parent, (SentEmbedThumbnail) member);
        }
        throw new AssertionError();
    }

    private EmbedDraftAuthorImpl createAuthor(
            String authorName,
            String authorUrl,
            String authorIconUrl,
            FileContainer authorIconContainer
    ) {
        if (authorName == null) {
            return null;
        }

        EmbedDraftAuthorImpl author = new EmbedDraftAuthorImpl(this, authorName, authorUrl, authorIconUrl);
        author.attachContainer(authorIconContainer);
        return author;
    }

    private EmbedDraftThumbnailImpl createThumbnail(
            String thumbnailUrl,
            FileContainer thumbnailContainer
    ) {
        if (thumbnailUrl == null && thumbnailContainer == null) {
            return null;
        }

        EmbedDraftThumbnailImpl thumbnail = new EmbedDraftThumbnailImpl(this, thumbnailUrl);
        thumbnail.attachContainer(thumbnailContainer);
        return thumbnail;
    }

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

    private EmbedDraftFooterImpl createFooter(
            String footerText,
            String footerIconUrl,
            FileContainer footerIconContainer
    ) {
        if (footerText == null && (footerIconUrl == null && footerIconContainer == null)) {
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
    public EmbedDraft modifyAuthor(Consumer<EmbedDraftAuthor> authorFunction) {
        if (authorFunction == null) {
            author = null;
        } else {
            authorFunction.accept(author);
        }
        return this;
    }

    @Override
    public EmbedDraft setThumbnail(EmbedDraftThumbnail thumbnail) {
        this.thumbnail = (EmbedDraftThumbnailImpl) thumbnail;
        return this;
    }

    @Override
    public EmbedDraft modifyThumbnail(Consumer<EmbedDraftThumbnail> thumbnailFunction) {
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
    public EmbedDraft modifyImage(Consumer<EmbedDraftImage> imageFunction) {
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
    public EmbedDraft modifyFooter(Consumer<EmbedDraftFooter> footerFunction) {
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
    public Optional<EmbedDraftFooterImpl> getFooter() {
        return Optional.ofNullable(footer);
    }

    @Override
    public Optional<EmbedDraftImageImpl> getImage() {
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<EmbedDraftThumbnailImpl> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    @Override
    public Optional<EmbedDraftAuthorImpl> getAuthor() {
        return Optional.ofNullable(author);
    }

    @Override
    public List<EmbedDraftFieldImpl> getFields() {
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

        getTitle().ifPresent(title -> node.put("title", title));
        getDescription().ifPresent(description -> node.put("description", description));
        getUrl().ifPresent(url -> node.put("url", url));
        getTimestamp().map(DateTimeFormatter.ISO_INSTANT::format).ifPresent(epoch -> node.put("timestamp", epoch));
        getColor().map(clr -> clr.getRGB() & 0xFFFFFF).ifPresent(rgb -> node.put("color", rgb));
        getFooter().ifPresent(footer -> footer.applyToNode(node));
        getImage().ifPresent(image -> image.applyToNode(node));
        getThumbnail().ifPresent(thumbnail -> thumbnail.applyToNode(node));
        getAuthor().ifPresent(author -> author.applyToNode(node));
        getFields().forEach(field -> field.applyToNode(node));

        return jsonFieldName;
    }
}
