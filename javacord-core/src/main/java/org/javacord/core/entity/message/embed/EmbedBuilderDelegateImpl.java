package org.javacord.core.entity.message.embed;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.internal.EmbedBuilderDelegate;
import org.javacord.core.entity.message.embed.draft.EmbedDraftFileContainerAttachableMember;
import org.javacord.core.entity.message.embed.draft.EmbedDraftImpl;
import org.javacord.core.util.FileContainer;

/**
 * The implementation of {@link EmbedBuilderDelegate}.
 */
public class EmbedBuilderDelegateImpl implements EmbedBuilderDelegate {

    // General embed stuff
    protected String title = null;
    protected String description = null;
    protected URL url = null;
    protected Instant timestamp = null;
    protected Color color = null;

    // Author
    protected String authorName = null;
    protected URL authorUrl = null;
    protected URL authorIconUrl = null;
    protected FileContainer authorIconContainer = null;

    // Thumbnail
    protected URL thumbnailUrl = null;
    protected FileContainer thumbnailContainer = null;

    // Image
    protected URL imageUrl = null;
    protected FileContainer imageContainer = null;

    // Footer
    protected String footerText = null;
    protected URL footerIconUrl = null;
    protected FileContainer footerIconContainer = null;

    // Fields
    protected final List<BaseEmbedField> fields;

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
    public void setUrl(URL url) {
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
    public void setAuthor(String name, URL url, URL iconUrl) {
        this.authorName = name;
        this.authorUrl = url;
        this.authorIconUrl = iconUrl;
    }

    @Override
    public void setAuthor(String name, URL url, Object icon, String fileType) {
        setAuthor(name, url, null);
        this.authorIconContainer = createFileContainer(icon, fileType);
    }

    @Override
    public void setAuthor(BaseEmbedAuthor author) {
        setAuthor(
                author.getName(),
                author.getUrl().orElse(null),
                author.getIconUrl().orElse(null)
        );
        // if the icon url is empty, author is an EmbedDraftAuthor with an attachment
        if (!author.getIconUrl().isPresent()) {
            this.authorIconContainer = ((EmbedDraftFileContainerAttachableMember) author).getContainer();
        }
    }

    @Override
    public void setThumbnail(URL url) {
        this.thumbnailUrl = url;
    }

    @Override
    public void setThumbnail(Object image, String fileType) {
        this.thumbnailContainer = createFileContainer(image, fileType);
    }

    @Override
    public void setThumbnail(BaseEmbedThumbnail thumbnail) {
        setThumbnail(thumbnail.getUrl().orElse(null));
        // if the url is empty, thumbnail is an EmbedDraftThumbnail with an attachment
        if (!thumbnail.getUrl().isPresent()) {
            this.thumbnailContainer = ((EmbedDraftFileContainerAttachableMember) thumbnail).getContainer();
        }
    }

    @Override
    public void setImage(URL url) {
        this.imageUrl = url;
    }

    @Override
    public void setImage(Object image, String fileType) {
        this.imageContainer = createFileContainer(image, fileType);
    }

    @Override
    public void setImage(BaseEmbedImage image) {
        setImage(image.getUrl().orElse(null));
        // if the url is empty, image is an EmbedDraftImage with an attachment
        if (!image.getUrl().isPresent()) {
            this.imageContainer = ((EmbedDraftFileContainerAttachableMember) image).getContainer();
        }
    }

    @Override
    public void setFooter(String text, URL iconUrl) {
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
        setFooter(
                footer.getText(),
                footer.getIconUrl().orElse(null)
        );
        // if the icon url is empty, footer is an EmbedDraftFooter with an attachment
        if (!footer.getIconUrl().isPresent()) {
            this.footerIconContainer = ((EmbedDraftFileContainerAttachableMember) footer).getContainer();
        }
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

                authorName, authorUrl, authorIconUrl, authorIconContainer,
                thumbnailUrl, thumbnailContainer,
                imageUrl, imageContainer,
                footerText, footerIconUrl, footerIconContainer,
                fields
        );
    }

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
            throw new AssertionError();
        }

        container.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);

        return container;
    }

    private class PreliminaryField implements BaseEmbedField {
        private final String name;
        private final String value;
        private final boolean inline;

        private PreliminaryField(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
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
