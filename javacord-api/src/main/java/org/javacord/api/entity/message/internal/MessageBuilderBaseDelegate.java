package org.javacord.api.entity.message.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.IncomingWebhook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link MessageBuilder} to create messages.
 * You usually don't want to interact with this object.
 */
public interface MessageBuilderBaseDelegate {

    /**
     * Add high-level components to the message.
     *
     * @param highLevelComponents The high-level components.
     */
    void addComponents(HighLevelComponent... highLevelComponents);

    /**
     * Add low-level components to the message, wrapped in an ActionRow.
     *
     * @param lowLevelComponents The low level components.
     */
    void addActionRow(LowLevelComponent... lowLevelComponents);

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code The code.
     */
    void appendCode(String language, String code);

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message The string to append.
     * @param decorations The decorations of the string.
     */
    void append(String message, MessageDecoration... decorations);

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     */
    void append(Mentionable entity);

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @see StringBuilder#append(Object)
     */
    void append(Object object);

    /**
     * Appends a named link to the message.
     *
     * @param name The name of the link.
     * @param url  The URL of the link.
     */
    void appendNamedLink(String name, String url);

    /**
     * Appends a new line to the message.
     */
    void appendNewLine();

    /**
     * Fill the builder's values with a given message.
     *
     * @param message The message to copy from.
     */
    void copy(Message message);

    /**
     * Sets the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @param content The new content of the message.
     */
    void setContent(String content);

    /**
     * Removes an attachment from the message.
     *
     * @param attachment The attachment to remove.
     */
    void removeExistingAttachment(Attachment attachment);

    /**
     * Removes all the attachments from the message.
     */
    void removeExistingAttachments();

    /**
     * Removes multiple attachments from the message.
     *
     * @param attachments The attachments to remove.
     */
    void removeExistingAttachments(Collection<Attachment> attachments);

    /**
     * Adds the embed to the message.
     *
     * @param embed The embed to add.
     */
    void addEmbed(EmbedBuilder embed);

    /**
     * Removes all embeds from the message.
     */
    void removeAllEmbeds();

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     */
    void addEmbeds(List<EmbedBuilder> embeds);

    /**
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     */
    void removeEmbed(EmbedBuilder embed);

    /**
     * Removes the embeds from the message.
     *
     * @param embeds The embeds to remove.
     */
    void removeEmbeds(EmbedBuilder... embeds);

    /**
     * Remove a high-level component from the message.
     *
     * @param index The index placement.
     */
    void removeComponent(int index);

    /**
     * Remove a high-level component from the message.
     *
     * @param component The high-level component being removed.
     */
    void removeComponent(HighLevelComponent component);

    /**
     * Remove all high-level components from the message.
     */
    void removeAllComponents();

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     */
    void setTts(boolean tts);

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @param description The description of the image.
     */
    void addAttachment(BufferedImage image, String fileName, String description);

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @param description The description of the file.
     */
    void addAttachment(File file, String description);

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @param description The description of the icon.
     */
    void addAttachment(Icon icon, String description);

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @param description The description of the attachment.
     */
    void addAttachment(URL url, String description);

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @param description The description of the file.
     */
    void addAttachment(byte[] bytes, String fileName, String description);

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @param description The description of the file.
     */
    void addAttachment(InputStream stream, String fileName, String description);

    /**
     * Adds a spoiler attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @param description The description of the file.
     */
    void addAttachmentAsSpoiler(File file, String description);

    /**
     * Adds a spoiler attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @param description The description of the icon.
     */
    void addAttachmentAsSpoiler(Icon icon, String description);

    /**
     * Adds a spoiler attachment to the message.
     *
     * @param url The url of the attachment.
     * @param description The description of the url.
     */
    void addAttachmentAsSpoiler(URL url, String description);
    
    /**
     * Controls the mention behavior.
     *
     * @param allowedMentions The mention object to specify which mention should ping.
     */
    void setAllowedMentions(AllowedMentions allowedMentions);

    /**
     * Sets the message to reply to.
     *
     * @param messageId The id of the message to reply to.
     */
    void replyTo(long messageId);

    /**
     * Sets the nonce of the message.
     *
     * @param nonce The nonce to set.
     */
    void setNonce(String nonce);

    /**
     * Adds a sticker to the message. This will only work if the sticker is from the same server as the message will
     * be sent on, or if it is a default sticker.
     *
     * @param stickerId The ID of the sticker to add.
     */
    void addSticker(long stickerId);

    /**
     * Adds stickers to the message. You can add up to 3 different stickers per message.
     * Adding the same sticker twice or more will just add the sticker once. This will only work if the stickers are
     * from the same server as the message will be sent on, or if they are default stickers.
     *
     * @param stickerIds The stickers to add.
     */
    void addStickers(Collection<Long> stickerIds);

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    StringBuilder getStringBuilder();

    /**
     * Sends the message.
     *
     * @param user The user to which the message should be sent.
     * @return The message that has been sent.
     */
    CompletableFuture<Message> send(User user);

    /**
     * Sends the message.
     *
     * @param channel The channel in which the message should be sent.
     * @return The message that has been sent.
     */
    CompletableFuture<Message> send(TextChannel channel);

    /**
     * Sends the message.
     *
     * @param webhook The webhook from which the message should be sent.
     * @return The message that has been sent.
     */
    CompletableFuture<Message> send(IncomingWebhook webhook);

    /**
     * Sends the message.
     *
     * @param messageable The receiver of the message.
     * @return The message that has been sent.
     */
    CompletableFuture<Message> send(Messageable messageable);

    /**
     * Edits the message.
     *
     * @param message The message to edit.
     * @param allFields True if all fields should be included in the patch request, even if not changed; False if
     *                  only changed fields should be patched
     * @return The edited message.
     */
    CompletableFuture<Message> edit(Message message, boolean allFields);

    /**
     * Sends the message.
     *
     * @param api The api instance needed to send and return the message.
     * @param webhookId The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return The message that has been sent.
     */
    CompletableFuture<Message> sendWithWebhook(DiscordApi api, String webhookId, String webhookToken);
}
