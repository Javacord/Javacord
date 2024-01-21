package org.javacord.api.entity.message.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.exception.MissingIntentException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link MessageBuilder} to create messages.
 * You usually don't want to interact with this object.
 */
public interface MessageBuilderBaseDelegate {

    /**
     * Sets the interaction message flags of the message.
     *
     * @param messageFlags The message flags of the message.
     */
    void setFlags(EnumSet<MessageFlag> messageFlags);

    /**
     * Adds the interaction message flag to the message.
     *
     * @param messageFlag The message flag of the message.
     */
    void addFlag(MessageFlag messageFlag);

    /**
     * Add high-level components to the message.
     *
     * @param highLevelComponents The high-level components.
     */
    void addComponents(List<HighLevelComponent> highLevelComponents);

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
     * Appends a string to the StringBuilder.
     *
     * @param content The string to append.
     * @return The StringBuilder to call more modifying methods on it.
     */
    StringBuilder appendToStringBuilder(String content);

    /**
     * Appends a named link to the message.
     *
     * @param name The name of the link.
     * @param url  The URL of the link.
     */
    void appendNamedLink(String name, String url);

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
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     */
    void removeEmbed(EmbedBuilder embed);

    /**
     * Fill the builder's values with a given message.
     *
     * @param message The message to copy from.
     * @throws MissingIntentException See Javadoc of {@link Message#getContent()} for further explanation.
     */
    void copy(Message message);

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     */
    void setTts(boolean tts);

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
     * Adds an attachment to the message.
     *
     * @param image       The image to add as an attachment.
     * @param fileName    The file name of the image.
     * @param description The description of the image.
     */
    void addAttachment(BufferedImage image, String fileName, String description);

    /**
     * Adds an attachment to the message.
     *
     * @param file        The file to add as an attachment.
     * @param description The description of the file.
     * @param spoiler     Whether the attachment should be marked as a spoiler.
     */
    void addAttachment(File file, String description, boolean spoiler);

    /**
     * Adds an attachment to the message.
     *
     * @param icon        The icon to add as an attachment.
     * @param description The description of the icon.
     * @param spoiler     Whether the attachment should be marked as a spoiler.
     */
    void addAttachment(Icon icon, String description, boolean spoiler);

    /**
     * Adds an attachment to the message.
     *
     * @param url         The url of the attachment.
     * @param description The description of the attachment.
     * @param spoiler     Whether the attachment should be marked as a spoiler.
     */
    void addAttachment(URL url, String description, boolean spoiler);

    /**
     * Adds an attachment to the message.
     *
     * @param bytes       The bytes of the file.
     * @param fileName    The name of the file.
     * @param description The description of the file.
     */
    void addAttachment(byte[] bytes, String fileName, String description);

    /**
     * Adds an attachment to the message.
     *
     * @param stream      The stream of the file.
     * @param fileName    The name of the file.
     * @param description The description of the file.
     */
    void addAttachment(InputStream stream, String fileName, String description);

    /**
     * Controls the mention behavior.
     *
     * @param allowedMentions The mention object to specify which mention should ping.
     */
    void setAllowedMentions(AllowedMentions allowedMentions);

    /**
     * Sets the message to reply to.
     *
     * @param messageId             The id of the message to reply to.
     * @param assertReferenceExists Used to tell discord if you want to check if the message exists.
     */
    void replyTo(long messageId, boolean assertReferenceExists);

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
     * @param message   The message to edit.
     * @param allFields True if all fields should be included in the patch request, even if not changed; False if
     *                  only changed fields should be patched
     * @return The edited message.
     */
    CompletableFuture<Message> edit(Message message, boolean allFields);

    /**
     * Sends the message.
     *
     * @param api          The api instance needed to send and return the message.
     * @param webhookId    The id of the webhook from which the message should be sent.
     * @param webhookToken The token of the webhook from which the message should be sent.
     * @return The message that has been sent.
     */
    CompletableFuture<Message> sendWithWebhook(DiscordApi api, String webhookId, String webhookToken);
}
