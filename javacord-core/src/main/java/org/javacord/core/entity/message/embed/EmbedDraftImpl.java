package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.EmbedBase;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedDraft;
import org.javacord.api.entity.message.embed.parts.EditableEmbedField;
import org.javacord.api.entity.message.embed.parts.EmbedField;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftThumbnail;
import org.javacord.core.entity.message.embed.parts.EmbedFieldImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftAuthorImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftFooterImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftImageImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftThumbnailImpl;
import org.javacord.core.util.FileContainer;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The implementation of a drafted embed.
 */
public class EmbedDraftImpl implements EmbedDraft {
    private final String title;
    private final String description;
    private final String url;
    private final Instant timestamp;
    private final Color color;
    private final EmbedDraftFooterImpl embedDraftFooter;
    private final EmbedDraftImageImpl embedDraftImage;
    private final EmbedDraftAuthorImpl embedDraftAuthor;
    private final EmbedDraftThumbnailImpl embedDraftThumbnail;
    private final List<EmbedFieldImpl> fields;

    /**
     * Creates a new instance.
     *  @param title               The title of the embed.
     * @param description         The description of the embed.
     * @param url                 The url that the title of the embed should lead to.
     * @param timestamp           The timestamp of the embed.
     * @param color               The color of the embed.
     * @param embedDraftAuthor    The draft embed author object.
     * @param embedDraftThumbnail The draft embed thumbnail object.
     * @param embedDraftFooter    The draft embed footer object.
     * @param embedDraftImage     The draft embed image object.
     * @param fields              A list with the embeds fields.
     */
    public EmbedDraftImpl(
            String title,
            String description,
            String url,
            Instant timestamp,
            Color color,
            EmbedDraftAuthorImpl embedDraftAuthor,
            EmbedDraftThumbnailImpl embedDraftThumbnail,
            EmbedDraftFooterImpl embedDraftFooter,
            EmbedDraftImageImpl embedDraftImage,
            List<EmbedFieldImpl> fields) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.embedDraftFooter = embedDraftFooter;
        this.embedDraftImage = embedDraftImage;
        this.embedDraftAuthor = embedDraftAuthor;
        this.embedDraftThumbnail = embedDraftThumbnail;
        this.fields = fields;
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
    public Optional<EmbedDraftFooter> getEmbedDraftFooter() {
        return Optional.ofNullable(embedDraftFooter);
    }

    @Override
    public Optional<EmbedDraftImage> getEmbedDraftImage() {
        return Optional.ofNullable(embedDraftImage);
    }

    @Override
    public Optional<EmbedDraftAuthor> getEmbedDraftAuthor() {
        return Optional.ofNullable(embedDraftAuthor);
    }

    @Override
    public Optional<EmbedDraftThumbnail> getEmbedDraftThumbnail() {
        return Optional.ofNullable(embedDraftThumbnail);
    }

    @Override
    public List<EmbedField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public <T extends EmbedField> void updateFields(
            Predicate<EmbedField> predicate,
            Function<EditableEmbedField, T> updater) {
        for (int i = 0; i < fields.size(); i++) {
            EmbedFieldImpl field = fields.get(i);
            if (predicate.test(field)) {
                fields.set(i, (EmbedFieldImpl) updater.apply(field.asEditableEmbedField()));
            }
        }
    }

    @Override
    public EmbedBuilder toBuilder() {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setUrl(url)
                .setTimestamp(timestamp)
                .setColor(color);
        EmbedBuilderDelegateImpl delegate = (EmbedBuilderDelegateImpl) builder.getDelegate();

        if (embedDraftFooter != null) {
            delegate.footerText = embedDraftFooter.getText().orElse(null);
            delegate.footerIconUrl = embedDraftFooter.getIconUrl().map(URL::toExternalForm).orElse(null);
            delegate.footerIconContainer = embedDraftFooter.getFooterIconContainer();
        }
        if (embedDraftImage != null) {
            delegate.imageUrl = (embedDraftImage.getUrl() == null ? null : embedDraftImage.getUrl().toExternalForm());
            delegate.imageContainer = embedDraftImage.getImageContainer();
        }
        if (embedDraftThumbnail != null) {
            delegate.thumbnailUrl = (embedDraftThumbnail.getUrl() == null
                    ? null : embedDraftThumbnail.getUrl().toExternalForm());
            delegate.thumbnailContainer = embedDraftThumbnail.getThumbnailContainer();
        }
        if (embedDraftAuthor != null) {
            delegate.authorName = embedDraftAuthor.getName();
            delegate.authorUrl = embedDraftAuthor.getUrl().map(URL::toExternalForm).orElse(null);
            delegate.authorIconUrl = embedDraftAuthor.getIconUrl().map(URL::toExternalForm).orElse(null);
            delegate.authorIconContainer = embedDraftAuthor.getAuthorIconContainer();
        }
        fields.forEach(field -> builder.addField(field.getName(), field.getValue(), field.isInline()));
        return builder;
    }

    @Override
    public EmbedDraft asEmbedDraft() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof EmbedBase) {
            String thisJson = ((EmbedDraftImpl) this.asEmbedDraft()).toJsonNode().toString();
            String otherJson = ((EmbedDraftImpl) ((EmbedBase) other).asEmbedDraft()).toJsonNode().toString();

            return thisJson.equals(otherJson);
        } else {
            return false;
        }
    }

    /**
     * Gets the embed as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The embed as a ObjectNode.
     */
    public ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    /**
     * Adds the json data to the given object node.
     *
     * @param object The object, the data should be added to.
     * @return The provided object with the data of the embed.
     */
    public ObjectNode toJsonNode(ObjectNode object) {
        String footerText = null;
        String footerIconUrl = null;
        String imageUrl = null;
        String videoUrl = null;
        String authorName = null;
        String authorUrl = null;
        String authorIconUrl = null;
        String thumbnailUrl = null;
        FileContainer footerIconContainer = null;
        FileContainer imageContainer = null;
        FileContainer videoContainer = null;
        FileContainer authorIconContainer = null;
        FileContainer thumbnailContainer = null;
        if (embedDraftFooter != null) {
            footerText = embedDraftFooter.getText().orElse(null);
            footerIconUrl = embedDraftFooter.getIconUrl().map(URL::toExternalForm).orElse(null);
            footerIconContainer = embedDraftFooter.getFooterIconContainer();
        }
        if (embedDraftImage != null) {
            imageUrl = (embedDraftImage.getUrl() == null ? null : embedDraftImage.getUrl().toExternalForm());
            imageContainer = embedDraftImage.getImageContainer();
        }
        if (embedDraftAuthor != null) {
            authorName = embedDraftAuthor.getName();
            authorUrl = embedDraftAuthor.getUrl().map(URL::toExternalForm).orElse(null);
            authorIconUrl = embedDraftAuthor.getIconUrl().map(URL::toExternalForm).orElse(null);
            authorIconContainer = embedDraftAuthor.getAuthorIconContainer();
        }
        if (embedDraftThumbnail != null) {
            thumbnailUrl = (embedDraftThumbnail.getUrl() == null
                    ? null : embedDraftThumbnail.getUrl().toExternalForm());
            thumbnailContainer = embedDraftThumbnail.getThumbnailContainer();
        }

        object.put("type", "rich");
        if (title != null && !title.equals("")) {
            object.put("title", title);
        }
        if (description != null && !description.equals("")) {
            object.put("description", description);
        }
        if (url != null && !url.equals("")) {
            object.put("url", url);
        }
        if (color != null) {
            object.put("color", color.getRGB() & 0xFFFFFF);
        }
        if (timestamp != null) {
            object.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(timestamp));
        }
        if ((footerText != null && !footerText.equals("")) || (footerIconUrl != null && !footerIconUrl.equals(""))) {
            ObjectNode footer = object.putObject("footer");
            if (footerText != null && !footerText.equals("")) {
                footer.put("text", footerText);
            }
            if (footerIconUrl != null && !footerIconUrl.equals("")) {
                footer.put("icon_url", footerIconUrl);
            }
            if (footerIconContainer != null) {
                footer.put("icon_url", "attachment://" + footerIconContainer.getFileTypeOrName());
            }
        }
        if (imageUrl != null && !imageUrl.equals("")) {
            object.putObject("image").put("url", imageUrl);
        }
        if (imageContainer != null) {
            object.putObject("image").put("url", "attachment://" + imageContainer.getFileTypeOrName());
        }
        if (videoUrl != null && !videoUrl.equals("")) {
            object.putObject("video").put("url", videoUrl);
        }
        if (videoContainer != null) {
            object.putObject("video").put("url", "attachment://" + videoContainer.getFileTypeOrName());
        }
        if (authorName != null && !authorName.equals("")) {
            ObjectNode author = object.putObject("author");
            author.put("name", authorName);
            if (authorUrl != null && !authorUrl.equals("")) {
                author.put("url", authorUrl);
            }
            if (authorIconUrl != null && !authorIconUrl.equals("")) {
                author.put("icon_url", authorIconUrl);
            }
            if (authorIconContainer != null) {
                author.put("url", "attachment://" + authorIconContainer.getFileTypeOrName());
            }
        }
        if (thumbnailUrl != null && !thumbnailUrl.equals("")) {
            object.putObject("thumbnail").put("url", thumbnailUrl);
        }
        if (thumbnailContainer != null) {
            object.putObject("thumbnail").put("url", "attachment://" + thumbnailContainer.getFileTypeOrName());
        }
        if (fields.size() > 0) {
            ArrayNode jsonFields = object.putArray("fields");
            for (EmbedField field : fields) {
                ObjectNode jsonField = jsonFields.addObject();
                jsonField.put("name", field.getName());
                jsonField.put("value", field.getValue());
                jsonField.put("inline", field.isInline());
            }
        }
        return object;
    }

    /**
     * Gets whether the embed has any attached files.
     *
     * @return Whether the embed has any attached files.
     */
    public boolean requiresAttachments() {
        boolean response = false;
        if (embedDraftFooter != null) {
            response = embedDraftFooter.getFooterIconContainer() != null;
        }
        if (embedDraftImage != null) {
            response = response || embedDraftImage.getImageContainer() != null;
        }
        if (embedDraftAuthor != null) {
            response = response || embedDraftAuthor.getAuthorIconContainer() != null;
        }
        if (embedDraftAuthor != null) {
            response = response || embedDraftAuthor.getAuthorIconContainer() != null;
        }

        return response;
    }

    /**
     * Gets a list of all FileContainers that are in the embed.
     *
     * @return A list of FileContainers
     */
    public List<FileContainer> getRequiredAttachments() {
        List<FileContainer> fileContainers = new ArrayList<>();
        if (embedDraftFooter != null) {
            fileContainers.add(embedDraftFooter.getFooterIconContainer());
        }
        if (embedDraftImage != null) {
            fileContainers.add(embedDraftImage.getImageContainer());
        }
        if (embedDraftAuthor != null) {
            fileContainers.add(embedDraftAuthor.getAuthorIconContainer());
        }
        if (embedDraftAuthor != null) {
            fileContainers.add(embedDraftAuthor.getAuthorIconContainer());
        }
        return fileContainers.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
