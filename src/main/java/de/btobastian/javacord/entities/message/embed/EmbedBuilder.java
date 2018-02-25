package de.btobastian.javacord.entities.message.embed;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.MessageAuthor;
import de.btobastian.javacord.utils.ImageContainer;
import de.btobastian.javacord.utils.io.FileUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

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
    private ImageContainer footerIconContainer = null;
    private String footerIconFileName = null; // Only used if an attachment is used

    // Image
    private String imageUrl = null;
    private ImageContainer imageContainer = null;
    private String imageFileName = null; // Only used if an attachment is used

    // Author
    private String authorName = null;
    private String authorUrl = null;
    private String authorIconUrl = null;
    private ImageContainer authorIconContainer = null;
    private String authorIconFileName = null; // Only used if an attachment is used

    // Thumbnail
    private String thumbnailUrl = null;
    private ImageContainer thumbnailContainer = null;
    private String thumbnailFileName = null; // Only used if an attachment is used

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
        footerIconFileName = null;
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
        footerIconFileName = null;
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
        footerIconFileName = null;
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
            footerIconFileName = null;
        } else {
            footerIconContainer = new ImageContainer(icon);
            footerIconFileName = UUID.randomUUID().toString() + "." + FileUtils.getExtension(icon);
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
            footerIconFileName = null;
        } else {
            footerIconContainer = new ImageContainer(icon, fileType);
            footerIconFileName = UUID.randomUUID().toString() + "." + fileType;
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
            footerIconFileName = null;
        } else {
            footerIconContainer = new ImageContainer(icon, fileType);
            footerIconFileName = UUID.randomUUID().toString() + "." + fileType;
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
            footerIconFileName = null;
        } else {
            footerIconContainer = new ImageContainer(icon, fileType);
            footerIconFileName = UUID.randomUUID().toString() + "." + fileType;
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
        imageFileName = null;
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
        imageFileName = null;
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
            imageFileName = null;
        } else {
            imageContainer = new ImageContainer(image);
            imageFileName = UUID.randomUUID().toString() + "." + FileUtils.getExtension(image);
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
            imageFileName = null;
        } else {
            imageContainer = new ImageContainer(image, fileType);
            imageFileName = UUID.randomUUID().toString() + "." + fileType;
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
            imageFileName = null;
        } else {
            imageContainer = new ImageContainer(image, fileType);
            imageFileName = UUID.randomUUID().toString() + "." + fileType;
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
            imageFileName = null;
        } else {
            imageContainer = new ImageContainer(image, fileType);
            imageFileName = UUID.randomUUID().toString() + "." + fileType;
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
        authorIconFileName = null;
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
        authorIconFileName = null;
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
        authorIconFileName = null;
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
        authorIconFileName = null;
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
        authorIconFileName = null;
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
            authorIconFileName = null;
        } else {
            authorIconContainer = new ImageContainer(icon);
            authorIconFileName = UUID.randomUUID().toString() + "." + FileUtils.getExtension(icon);
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
            authorIconFileName = null;
        } else {
            authorIconContainer = new ImageContainer(icon, fileType);
            authorIconFileName = UUID.randomUUID().toString() + "." + fileType;
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
            authorIconFileName = null;
        } else {
            authorIconContainer = new ImageContainer(icon, fileType);
            authorIconFileName = UUID.randomUUID().toString() + "." + fileType;
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
            authorIconFileName = null;
        } else {
            authorIconContainer = new ImageContainer(icon, fileType);
            authorIconFileName = UUID.randomUUID().toString() + "." + fileType;
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
        thumbnailFileName = null;
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
        thumbnailFileName = null;
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
            thumbnailFileName = null;
        } else {
            thumbnailContainer = new ImageContainer(thumbnail);
            thumbnailFileName = UUID.randomUUID().toString() + "." + FileUtils.getExtension(thumbnail);
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
            thumbnailFileName = null;
        } else {
            thumbnailContainer = new ImageContainer(thumbnail, fileType);
            thumbnailFileName = UUID.randomUUID().toString() + "." + fileType;
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
            thumbnailFileName = null;
        } else {
            thumbnailContainer = new ImageContainer(thumbnail, fileType);
            thumbnailFileName = UUID.randomUUID().toString() + "." + fileType;
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
            thumbnailFileName = null;
        } else {
            thumbnailContainer = new ImageContainer(thumbnail, fileType);
            thumbnailFileName = UUID.randomUUID().toString() + "." + fileType;
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
        return footerIconFileName != null ||
                imageFileName != null ||
                authorIconFileName != null ||
                thumbnailFileName != null;
    }

    /**
     * Passes the required attachments for this embed to the given consumer.
     *
     * @param consumer The consumer which takes the required attachments.
     */
    public void consumeRequiredAttachments(BiConsumer<String, ImageContainer> consumer) {
        if (footerIconContainer != null) {
            consumer.accept(footerIconFileName, footerIconContainer);
        }
        if (imageContainer != null) {
            consumer.accept(imageFileName, imageContainer);
        }
        if (authorIconContainer != null) {
            consumer.accept(authorIconFileName, authorIconContainer);
        }
        if (thumbnailContainer != null) {
            consumer.accept(thumbnailFileName, thumbnailContainer);
        }
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
            if (footerIconFileName != null) {
                footer.put("icon_url", "attachment://" + footerIconFileName);
            }
        }
        if (imageUrl != null && !imageUrl.equals("")) {
            object.putObject("image").put("url", imageUrl);
        }
        if (imageFileName != null) {
            object.putObject("image").put("url", "attachment://" + imageFileName);
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
            if (authorIconFileName != null) {
                author.put("url", "attachment://" + authorIconFileName);
            }
        }
        if (thumbnailUrl != null && !thumbnailUrl.equals("")) {
            object.putObject("thumbnailContainer").put("url", thumbnailUrl);
        }
        if (thumbnailFileName != null) {
            object.putObject("thumbnailContainer").put("url", "attachment://" + thumbnailFileName);
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