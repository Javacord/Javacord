package org.javacord.core.entity.message.embed;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedDraft;
import org.javacord.api.entity.message.embed.internal.EmbedBuilderDelegate;
import org.javacord.api.entity.message.embed.parts.EditableEmbedField;
import org.javacord.api.entity.message.embed.parts.EmbedField;
import org.javacord.api.entity.user.User;
import org.javacord.core.entity.message.embed.parts.EmbedFieldImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftAuthorImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftFooterImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftImageImpl;
import org.javacord.core.entity.message.embed.parts.draft.EmbedDraftThumbnailImpl;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.io.FileUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The implementation of {@link EmbedBuilderDelegate}.
 */
public class EmbedBuilderDelegateImpl implements EmbedBuilderDelegate {

    // Fields
    final List<EmbedFieldImpl> fields = new ArrayList<>();
    // General embed stuff
    String title = null;
    String description = null;
    String url = null;
    Instant timestamp = null;
    Color color = null;
    // Footer
    String footerText = null;
    String footerIconUrl = null;
    FileContainer footerIconContainer = null;
    // Image
    String imageUrl = null;
    FileContainer imageContainer = null;
    // Video
    String videoUrl = null;
    FileContainer videoContainer = null;
    // Author
    String authorName = null;
    String authorUrl = null;
    String authorIconUrl = null;
    FileContainer authorIconContainer = null;
    // Thumbnail
    String thumbnailUrl = null;
    FileContainer thumbnailContainer = null;

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
    public void setTimestampToNow() {
        this.timestamp = Instant.now();
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
    public void setFooter(String text) {
        footerText = text;
        footerIconUrl = null;
        footerIconContainer = null;
    }

    @Override
    public void setFooter(String text, String iconUrl) {
        footerText = text;
        footerIconUrl = iconUrl;
        footerIconContainer = null;
    }

    @Override
    public void setFooter(String text, Icon icon) {
        footerText = text;
        footerIconUrl = icon.getUrl().toString();
        footerIconContainer = null;
    }

    @Override
    public void setFooter(String text, File icon) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + FileUtils.getExtension(icon));
        }
    }

    @Override
    public void setFooter(String text, InputStream icon) {
        setFooter(text, icon, "png");
    }

    @Override
    public void setFooter(String text, InputStream icon, String fileType) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon, fileType);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setFooter(String text, byte[] icon) {
        setFooter(text, icon, "png");
    }

    @Override
    public void setFooter(String text, byte[] icon, String fileType) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon, fileType);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setFooter(String text, BufferedImage icon) {
        setFooter(text, icon, "png");
    }

    @Override
    public void setFooter(String text, BufferedImage icon, String fileType) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon, fileType);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setImage(String url) {
        imageUrl = url;
        imageContainer = null;
    }

    @Override
    public void setImage(Icon image) {
        imageUrl = image.getUrl().toString();
        imageContainer = null;
    }

    @Override
    public void setImage(File image) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + FileUtils.getExtension(image));
        }
    }

    @Override
    public void setImage(InputStream image) {
        setImage(image, "png");
    }

    @Override
    public void setImage(InputStream image, String fileType) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image, fileType);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setImage(byte[] image) {
        setImage(image, "png");
    }

    @Override
    public void setImage(byte[] image, String fileType) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image, fileType);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setImage(BufferedImage image) {
        setImage(image, "png");
    }

    @Override
    public void setImage(BufferedImage image, String fileType) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image, fileType);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setAuthor(MessageAuthor author) {
        authorName = author.getDisplayName();
        authorUrl = null;
        authorIconUrl = author.getAvatar().getUrl().toString();
        authorIconContainer = null;
    }

    @Override
    public void setAuthor(User author) {
        authorName = author.getName();
        authorUrl = null;
        authorIconUrl = author.getAvatar().getUrl().toString();
        authorIconContainer = null;
    }

    @Override
    public void setAuthor(String name) {
        authorName = name;
        authorUrl = null;
        authorIconUrl = null;
        authorIconContainer = null;
    }

    @Override
    public void setAuthor(String name, String url, String iconUrl) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = iconUrl;
        authorIconContainer = null;
    }

    @Override
    public void setAuthor(String name, String url, Icon icon) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = icon.getUrl().toString();
        authorIconContainer = null;
    }

    @Override
    public void setAuthor(String name, String url, File icon) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + FileUtils.getExtension(icon));
        }
    }

    @Override
    public void setAuthor(String name, String url, InputStream icon) {
        setAuthor(name, url, icon, "png");
    }

    @Override
    public void setAuthor(String name, String url, InputStream icon, String fileType) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon, fileType);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setAuthor(String name, String url, byte[] icon) {
        setAuthor(name, url, icon, "png");
    }

    @Override
    public void setAuthor(String name, String url, byte[] icon, String fileType) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon, fileType);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setAuthor(String name, String url, BufferedImage icon) {
        setAuthor(name, url, icon, "png");
    }

    @Override
    public void setAuthor(String name, String url, BufferedImage icon, String fileType) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon, fileType);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setThumbnail(String url) {
        thumbnailUrl = url;
        thumbnailContainer = null;
    }

    @Override
    public void setThumbnail(Icon thumbnail) {
        thumbnailUrl = thumbnail.getUrl().toString();
        thumbnailContainer = null;
    }

    @Override
    public void setThumbnail(File thumbnail) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail);
            thumbnailContainer.setFileTypeOrName(
                    UUID.randomUUID().toString() + "." + FileUtils.getExtension(thumbnail));
        }
    }

    @Override
    public void setThumbnail(InputStream thumbnail) {
        setThumbnail(thumbnail, "png");
    }

    @Override
    public void setThumbnail(InputStream thumbnail, String fileType) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail, fileType);
            thumbnailContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setThumbnail(byte[] thumbnail) {
        setThumbnail(thumbnail, "png");
    }

    @Override
    public void setThumbnail(byte[] thumbnail, String fileType) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail, fileType);
            thumbnailContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void setThumbnail(BufferedImage thumbnail) {
        setThumbnail(thumbnail, "png");
    }

    @Override
    public void setThumbnail(BufferedImage thumbnail, String fileType) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail, fileType);
            thumbnailContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
    }

    @Override
    public void addField(String name, String value, boolean inline) {
        fields.add(new EmbedFieldImpl(name, value, inline));
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
    public void removeFields(Predicate<EmbedField> predicate) {
        fields.removeIf(predicate);
    }

    @Override
    public boolean requiresAttachments() {
        return footerIconContainer != null
                || imageContainer != null
                || authorIconContainer != null
                || thumbnailContainer != null;
    }

    @Override
    public EmbedDraft build() {
        return new EmbedDraftImpl(
                title,
                description,
                url,
                timestamp,
                color,
                getDraftAuthor(),
                getDraftThumbnail(),
                getDraftFooter(),
                getDraftImage(),
                fields
        );
    }

    private EmbedDraftAuthorImpl getDraftAuthor() {
        if (authorName == null
                && authorUrl == null
                && authorIconUrl == null
                && authorIconContainer == null) {
            return null;
        } else {
            return new EmbedDraftAuthorImpl(
                    authorName,
                    authorUrl,
                    authorIconUrl,
                    authorIconContainer);
        }
    }

    private EmbedDraftThumbnailImpl getDraftThumbnail() {
        if (thumbnailUrl == null
                && thumbnailContainer == null) {
            return null;
        } else {
            return new EmbedDraftThumbnailImpl(
                    thumbnailUrl,
                    thumbnailContainer);
        }
    }

    private EmbedDraftFooterImpl getDraftFooter() {
        if (footerText == null
                && footerIconUrl == null
                && footerIconContainer == null) {
            return null;
        } else {
            return new EmbedDraftFooterImpl(
                    footerText,
                    footerIconUrl,
                    footerIconContainer);
        }
    }

    private EmbedDraftImageImpl getDraftImage() {
        if (imageUrl == null
                && imageContainer == null) {
            return null;
        } else {
            return new EmbedDraftImageImpl(
                    imageUrl,
                    imageContainer);
        }
    }
}
