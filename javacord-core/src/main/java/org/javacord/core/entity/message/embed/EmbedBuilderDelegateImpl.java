package org.javacord.core.entity.message.embed;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;
import org.javacord.api.entity.message.embed.internal.EmbedBuilderDelegate;
import org.javacord.core.entity.message.embed.draft.EmbedDraftImpl;
import org.javacord.core.util.FileContainer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The implementation of {@link EmbedBuilderDelegate}.
 */
public class EmbedBuilderDelegateImpl implements EmbedBuilderDelegate {

    // Fields
    protected final List<BaseEmbedField> fields;
    // General embed stuff
    protected String title = null;
    protected String description = null;
    protected String url = null;
    protected Instant timestamp = null;
    protected Color color = null;
    // Author
    protected BaseEmbedAuthor author = null;
    protected String authorName = null;
    protected String authorUrl = null;
    protected String authorIconUrl = null;
    protected FileContainer authorIconContainer = null;
    // Thumbnail
    protected BaseEmbedThumbnail thumbnail = null;
    protected String thumbnailUrl = null;
    protected FileContainer thumbnailContainer = null;
    // Image
    protected BaseEmbedImage image = null;
    protected String imageUrl = null;
    protected FileContainer imageContainer = null;
    // Footer
    protected BaseEmbedFooter footer = null;
    protected String footerText = null;
    protected String footerIconUrl = null;
    protected FileContainer footerIconContainer = null;

    /**
     * Constructor of the delegate implementation.
     */
    public EmbedBuilderDelegateImpl() {
        fields = new ArrayList<>();
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setAuthor(String name, String url, String iconUrl) {
        this.authorName = name;
        this.authorUrl = url;
        this.authorIconUrl = iconUrl;
    }

    @Override
    public void setAuthor(String name, String url, Object icon, String fileType) {
        setAuthor(name, url, null);
        this.authorIconContainer = createFileContainer(icon, fileType);
    }

    @Override
    public void setAuthor(BaseEmbedAuthor author) {
        this.author = author;

        this.authorName = null;
        this.authorUrl = null;
        this.authorIconUrl = null;
        this.authorIconContainer = null;
    }

    @Override
    public void setThumbnail(String url) {
        this.thumbnailUrl = url;
    }

    @Override
    public void setThumbnail(Object image, String fileType) {
        this.thumbnailContainer = createFileContainer(image, fileType);
    }

    @Override
    public void setThumbnail(BaseEmbedThumbnail thumbnail) {
        this.thumbnail = thumbnail;

        this.thumbnailUrl = null;
        this.thumbnailContainer = null;
    }

    @Override
    public void setImage(String url) {
        this.imageUrl = url;
    }

    @Override
    public void setImage(Object image, String fileType) {
        this.imageContainer = createFileContainer(image, fileType);
    }

    @Override
    public void setImage(BaseEmbedImage image) {
        this.image = image;

        this.imageUrl = null;
        this.imageContainer = null;
    }

    @Override
    public void setFooter(String text, String iconUrl) {
        this.footerText = text;
        this.footerIconUrl = iconUrl;
    }

    @Override
    public void setFooter(String text, Object icon, String fileType) {
        setFooter(text, null);
        this.footerIconContainer = createFileContainer(icon, fileType);
    }

    @Override
    public void setFooter(BaseEmbedFooter footer) {
        this.footer = footer;

        this.footerText = null;
        this.footerIconUrl = null;
        this.footerIconContainer = null;
    }

    @Override
    public void addField(String name, String value, boolean inline) {
        fields.add(new PreliminaryField(name, value, inline));
    }

    @Override
    public void addField(BaseEmbedField field) {
        fields.add(field);
    }

    @Override
    public EmbedDraft build() {
        return new EmbedDraftImpl(
                title,
                description,
                url,
                timestamp,
                color,

                author, authorName, authorUrl, authorIconUrl, authorIconContainer,
                thumbnail, thumbnailUrl, thumbnailContainer,
                image, imageUrl, imageContainer,
                footer, footerText, footerIconUrl, footerIconContainer,
                fields
        );
    }

    /**
     * Generates a new {@code FileContainer} based on the provided image object and file type.
     * The {@code image} parameter must be one of {@link Icon},
     * {@link File}, {@link InputStream}, {@link BufferedImage} or {@link byte[]}.
     * If the {@code image} parameter is {@code null}, the method returns {@code null}.
     * If the {@code image} parameter is none of the said types, the method returns {@code null}.
     *
     * @param image The image object.
     * @param fileType The image type of the object.
     * @return A new {@code FileContainer} or {@code null}.
     */
    private FileContainer createFileContainer(Object image, String fileType) {
        if (image == null) {
            return null;
        }

        FileContainer container;

        if (image instanceof Icon) {
            container = new FileContainer((Icon) image);
        } else if (image instanceof File) {
            container = new FileContainer((File) image);
        } else if (image instanceof InputStream) {
            container = new FileContainer((InputStream) image, fileType);
        } else if (image instanceof BufferedImage) {
            container = new FileContainer((BufferedImage) image, fileType);
        } else if (image.getClass() == byte[].class) {
            container = new FileContainer((byte[]) image, fileType);
        } else {
            return null;
        }

        container.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);

        return container;
    }

    /**
     * Preliminary implementation for {@code BaseEmbedField}.
     * Only used for carrying embed field data with the delegate.
     */
    private class PreliminaryField implements BaseEmbedField {
        private final String name;
        private final String value;
        private final boolean inline;

        /**
         * Private constructor.
         *
         * @param name The fields name.
         * @param value The fields value.
         * @param inline The fields inline value.
         */
        private PreliminaryField(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        @Override
        public BaseEmbed getEmbed() {
            return null; // internal class
        }

        @Override
        public EmbedDraftField toEmbedDraftField() {
            return null; // internal class
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public boolean isInline() {
            return inline;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
