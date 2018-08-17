package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBase;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedDraft;
import org.javacord.api.entity.message.embed.SentEmbed;
import org.javacord.api.entity.message.embed.parts.EditableEmbedField;
import org.javacord.api.entity.message.embed.parts.EmbedField;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedAuthor;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedFooter;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedImage;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedProvider;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedThumbnail;
import org.javacord.api.entity.message.embed.parts.sent.SentEmbedVideo;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.entity.message.embed.parts.EmbedFieldImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftAuthorImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftFooterImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftImageImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftThumbnailImpl;
import org.javacord.core.entity.message.embed.parts.sent.SentEmbedAuthorImpl;
import org.javacord.core.entity.message.embed.parts.sent.SentEmbedFooterImpl;
import org.javacord.core.entity.message.embed.parts.sent.SentEmbedImageImpl;
import org.javacord.core.entity.message.embed.parts.sent.SentEmbedProviderImpl;
import org.javacord.core.entity.message.embed.parts.sent.SentEmbedThumbnailImpl;
import org.javacord.core.entity.message.embed.parts.sent.SentEmbedVideoImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The implementation of a sent embed.
 */
public class SentEmbedImpl implements SentEmbed {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(SentEmbedImpl.class);

    private final MessageImpl message;
    private final String title;
    private final String type;
    private final String description;
    private final String url;
    private final Instant timestamp;
    private final Color color;
    private final SentEmbedFooter footer;
    private final SentEmbedImage image;
    private final SentEmbedThumbnail thumbnail;
    private final SentEmbedVideo video;
    private final SentEmbedProvider provider;
    private final SentEmbedAuthor author;
    private final List<EmbedFieldImpl> fields = new ArrayList<>();

    /**
     * Creates a new embed.
     *
     * @param message The message that this embed is in.
     * @param data The json data of the embed.
     */
    public SentEmbedImpl(MessageImpl message, JsonNode data) {
        this.message = message;
        title = data.has("title") ? data.get("title").asText() : null;
        type = data.has("type") ? data.get("type").asText() : null;
        description = data.has("description") ? data.get("description").asText() : null;
        url = data.has("url") ? data.get("url").asText() : null;
        timestamp = data.has("timestamp") ? OffsetDateTime.parse(data.get("timestamp").asText()).toInstant() : null;
        color = data.has("color") ? new Color(data.get("color").asInt()) : null;
        footer = data.has("footer") ? new SentEmbedFooterImpl(data.get("footer")) : null;
        image = data.has("image") ? new SentEmbedImageImpl(data.get("image")) : null;
        thumbnail = data.has("thumbnail") ? new SentEmbedThumbnailImpl(data.get("thumbnail")) : null;
        video = data.has("video") ? new SentEmbedVideoImpl(data.get("video")) : null;
        provider = data.has("provider") ? new SentEmbedProviderImpl(data.get("provider")) : null;
        author = data.has("author") ? new SentEmbedAuthorImpl(data.get("author")) : null;
        if (data.has("fields")) {
            for (JsonNode jsonField : data.get("fields")) {
                this.fields.add(new EmbedFieldImpl(jsonField));
            }
        }
    }

    @Override
    public Optional<Message> getMessage() {
        return Optional.ofNullable(message);
    }

    @Override
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<URL> getUrl() {
        if (url == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
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
    public Optional<SentEmbedFooter> getFooter() {
        return Optional.ofNullable(footer);
    }

    @Override
    public Optional<SentEmbedImage> getImage() {
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<SentEmbedThumbnail> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    @Override
    public Optional<SentEmbedVideo> getVideo() {
        return Optional.ofNullable(video);
    }

    @Override
    public Optional<SentEmbedProvider> getProvider() {
        return Optional.ofNullable(provider);
    }

    @Override
    public Optional<SentEmbedAuthor> getAuthor() {
        return Optional.ofNullable(author);
    }

    @Override
    public List<EmbedField> getFields() {
        return fields.stream()
                .map(field -> (EmbedField) field)
                .collect(Collectors.toList());
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
        if (message != null) {
            message.getApi()
                    .getUncachedMessageUtil()
                    .edit(
                            message.getChannel().getIdAsString(),
                            message.getIdAsString(),
                            this.asEmbedDraft());
        } else {
            throw new NullPointerException("This sent embed belongs to no Message!");
        }
    }

    @Override
    public EmbedDraft asEmbedDraft() {
        return new EmbedDraftImpl(
                getTitle().orElse(null),
                getDescription().orElse(null),
                getUrl().map(URL::toExternalForm).orElse(null),
                getTimestamp().orElse(null),
                getColor().orElse(null),
                (EmbedDraftAuthorImpl) getAuthor().map(SentEmbedAuthor::toDraft).orElse(null),
                (EmbedDraftThumbnailImpl) getThumbnail().map(SentEmbedThumbnail::toDraft).orElse(null),
                (EmbedDraftFooterImpl) getFooter().map(SentEmbedFooter::toDraft).orElse(null),
                (EmbedDraftImageImpl) getImage().map(SentEmbedImage::toDraft).orElse(null),
                getFields().stream().map(field -> (EmbedFieldImpl) field).collect(Collectors.toList())
        );
    }

    @Override
    public Optional<SentEmbed> asSentEmbed() {
        return Optional.of(this);
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

    @Override
    public EmbedBuilder toBuilder() {
        EmbedBuilder builder = new EmbedBuilder();
        getTitle().ifPresent(builder::setTitle);
        getDescription().ifPresent(builder::setDescription);
        getUrl().ifPresent(url -> builder.setUrl(url.toString()));
        getTimestamp().ifPresent(builder::setTimestamp);
        getColor().ifPresent(builder::setColor);
        getFooter().ifPresent(footer -> builder.setFooter(
                footer.getText().orElse(null), footer.getIconUrl().map(URL::toString).orElse(null)));
        getImage().ifPresent(image -> builder.setImage(image.getUrl().toString()));
        getThumbnail().ifPresent(thumbnail -> builder.setThumbnail(thumbnail.getUrl().toString()));
        getAuthor().ifPresent(author -> builder.setAuthor(
                author.getName(),
                author.getUrl().map(URL::toString).orElse(null),
                author.getIconUrl().map(URL::toString).orElse(null)));
        getFields().forEach(field -> builder.addField(field.getName(), field.getValue(), field.isInline()));
        return builder;
    }
}
