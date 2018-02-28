package de.btobastian.javacord.entity.message.embed;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.message.MessageAuthor;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.util.FileContainer;
import de.btobastian.javacord.util.io.FileUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is used to create embeds.
 */
public class EmbedBuilder {

    // General embed stuff
    private String title = null;
    private String description = null;
    private String url = null;
    private Instant timestamp = null;
    private Color color = null;

    // Footer
    private String footerText = null;
    private String footerIconUrl = null;
    private FileContainer footerIconContainer = null;

    // Image
    private String imageUrl = null;
    private FileContainer imageContainer = null;

    // Author
    private String authorName = null;
    private String authorUrl = null;
    private String authorIconUrl = null;
    private FileContainer authorIconContainer = null;

    // Thumbnail
    private String thumbnailUrl = null;
    private FileContainer thumbnailContainer = null;

    // Fields
    // (Array indices: 0: name (String), 1: value (String), 2: inline (boolean)
    private List<Object[]> fields = new ArrayList<>();

    /**
     * Creates a new embed builder.
     */
    public EmbedBuilder() {
        // Default constructor
    }

    /**
     * Sets the title of the embed.
     *
     * @param title The title of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the description of the embed.
     *
     * @param description The description of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the url of the embed.
     *
     * @param url The url of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the current time as timestamp of the embed.
     *
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setTimestamp() {
        this.timestamp = Instant.now();
        return this;
    }

    /**
     * Sets the timestamp of the embed.
     *
     * @param timestamp The timestamp to set.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Sets the color of the embed.
     *
     * @param color The color of the embed.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text) {
        footerText = text;
        footerIconUrl = null;
        footerIconContainer = null;
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param iconUrl The url of the footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, String iconUrl) {
        footerText = text;
        footerIconUrl = iconUrl;
        footerIconContainer = null;
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, Icon icon) {
        footerText = text;
        footerIconUrl = icon.getUrl().toString();
        footerIconContainer = null;
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, File icon) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + FileUtils.getExtension(icon));
        }
        return this;
    }

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, InputStream icon) {
        return setFooter(text, icon, "png");
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, InputStream icon, String fileType) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon, fileType);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, byte[] icon) {
        return setFooter(text, icon, "png");
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, byte[] icon, String fileType) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon, fileType);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the footer of the embed.
     * This method assumes the file type is "png"!
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, BufferedImage icon) {
        return setFooter(text, icon, "png");
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param icon The footer's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setFooter(String text, BufferedImage icon, String fileType) {
        footerText = text;
        footerIconUrl = null;
        if (icon == null) {
            footerIconContainer = null;
        } else {
            footerIconContainer = new FileContainer(icon, fileType);
            footerIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param url The url of the image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(String url) {
        imageUrl = url;
        imageContainer = null;
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(Icon image) {
        imageUrl = image.getUrl().toString();
        imageContainer = null;
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(File image) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + FileUtils.getExtension(image));
        }
        return this;
    }

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(InputStream image) {
        return setImage(image, "png");
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(InputStream image, String fileType) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image, fileType);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(byte[] image) {
        return setImage(image, "png");
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(byte[] image, String fileType) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image, fileType);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the image of the embed.
     * This method assumes the file type is "png"!
     *
     * @param image The image.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(BufferedImage image) {
        return setImage(image, "png");
    }

    /**
     * Sets the image of the embed.
     *
     * @param image The image.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setImage(BufferedImage image, String fileType) {
        imageUrl = null;
        if (image == null) {
            imageContainer = null;
        } else {
            imageContainer = new FileContainer(image, fileType);
            imageContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param author The message author which should be used as author.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(MessageAuthor author) {
        authorName = author.getDisplayName();
        authorUrl = null;
        authorIconUrl = author.getAvatar().getUrl().toString();
        authorIconContainer = null;
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param author The user which should be used as author.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(User author) {
        authorName = author.getName();
        authorUrl = null;
        authorIconUrl = author.getAvatar().getUrl().toString();
        authorIconContainer = null;
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name) {
        authorName = name;
        authorUrl = null;
        authorIconUrl = null;
        authorIconContainer = null;
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param iconUrl The url of the author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, String iconUrl) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = iconUrl;
        authorIconContainer = null;
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, Icon icon) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = icon.getUrl().toString();
        authorIconContainer = null;
        return this;
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, File icon) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + FileUtils.getExtension(icon));
        }
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, InputStream icon) {
        return setAuthor(name, url, icon, "png");
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, InputStream icon, String fileType) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon, fileType);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, byte[] icon) {
        return setAuthor(name, url, icon, "png");
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, byte[] icon, String fileType) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon, fileType);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the author of the embed.
     * This method assumes the file type is "png"!
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, BufferedImage icon) {
        return setAuthor(name, url, icon, "png");
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param icon The author's icon.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setAuthor(String name, String url, BufferedImage icon, String fileType) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = null;
        if (icon == null) {
            authorIconContainer = null;
        } else {
            authorIconContainer = new FileContainer(icon, fileType);
            authorIconContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param url The url of the thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(String url) {
        thumbnailUrl = url;
        thumbnailContainer = null;
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(Icon thumbnail) {
        thumbnailUrl = thumbnail.getUrl().toString();
        thumbnailContainer = null;
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(File thumbnail) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail);
            thumbnailContainer.setFileTypeOrName(
                    UUID.randomUUID().toString() + "." + FileUtils.getExtension(thumbnail));
        }
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(InputStream thumbnail) {
        return setThumbnail(thumbnail, "png");
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(InputStream thumbnail, String fileType) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail, fileType);
            thumbnailContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(byte[] thumbnail) {
        return setThumbnail(thumbnail, "png");
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(byte[] thumbnail, String fileType) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail, fileType);
            thumbnailContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     * This method assumes the file type is "png"!
     *
     * @param thumbnail The thumbnail.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(BufferedImage thumbnail) {
        return setThumbnail(thumbnail, "png");
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail.
     * @param fileType The type of the file, e.g. "png" or "gif".
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder setThumbnail(BufferedImage thumbnail, String fileType) {
        thumbnailUrl = null;
        if (thumbnail == null) {
            thumbnailContainer = null;
        } else {
            thumbnailContainer = new FileContainer(thumbnail, fileType);
            thumbnailContainer.setFileTypeOrName(UUID.randomUUID().toString() + "." + fileType);
        }
        return this;
    }

    /**
     * Adds a field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline or not.
     * @return The current instance in order to chain call methods.
     */
    public EmbedBuilder addField(String name, String value, boolean inline) {
        fields.add(new Object[]{name, value, inline});
        return this;
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
     * Checks if this embed requires any attachments.
     *
     * @return Whether the embed requires attachments or not.
     */
    public boolean requiresAttachments() {
        return footerIconContainer != null ||
                imageContainer != null ||
                authorIconContainer != null ||
                thumbnailContainer != null;
    }

    /**
     * Gets the required attachments for this embed.
     *
     * @return The required attachments for this embed.
     */
    public List<FileContainer> getRequiredAttachments() {
        List<FileContainer> requiredAttachments = new ArrayList<>();
        if (footerIconContainer != null) {
            requiredAttachments.add(footerIconContainer);
        }
        if (imageContainer != null) {
            requiredAttachments.add(imageContainer);
        }
        if (authorIconContainer != null) {
            requiredAttachments.add(authorIconContainer);
        }
        if (thumbnailContainer != null) {
            requiredAttachments.add(thumbnailContainer);
        }
        return requiredAttachments;
    }

    /**
     * Adds the json data to the given object node.
     *
     * @param object The object, the data should be added to.
     * @return The provided object with the data of the embed.
     */
    public ObjectNode toJsonNode(ObjectNode object) {
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
            object.putObject("thumbnailContainer").put("url", thumbnailUrl);
        }
        if (thumbnailContainer != null) {
            object.putObject("thumbnailContainer").put("url", "attachment://" + thumbnailContainer.getFileTypeOrName());
        }
        if (fields.size() > 0) {
            ArrayNode jsonFields = object.putArray("fields");
            for (Object[] field : fields) {
                ObjectNode jsonField = jsonFields.addObject();
                if (field[0] != null && !field[0].equals("")) {
                    jsonField.put("name", (String) field[0]);
                }
                if (field[1] != null && !field[1].equals("")) {
                    jsonField.put("value", (String) field[1]);
                }
                if (field[2] != null) {
                    jsonField.put("inline", (boolean) field[2]);
                }
            }
        }
        return object;
    }

}