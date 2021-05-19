package org.javacord.api.entity.message;

import org.javacord.api.command.Interaction;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.event.command.InteractionCreateEvent;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class InteractionMessageBuilder {

    protected final InteractionMessageBuilderDelegate delegate =
            DelegateFactory.createInteractionMessageBuilderDelegate();

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code The code.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder appendCode(String language, String code) {
        delegate.appendCode(language, code);
        return this;
    }

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder append(String message, MessageDecoration... decorations) {
        delegate.append(message, decorations);
        return this;
    }

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder append(Mentionable entity) {
        delegate.append(entity);
        return this;
    }

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    public InteractionMessageBuilder append(Object object) {
        delegate.append(object);
        return this;
    }

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder appendNewLine() {
        delegate.appendNewLine();
        return this;
    }

    /**
     * Sets the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @param content The new content of the message.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder setContent(String content) {
        delegate.setContent(content);
        return this;
    }

    /**
     * Adds the embed to the message.
     *
     * @param embed The embed to add.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addEmbed(EmbedBuilder embed) {
        delegate.addEmbed(embed);
        return this;
    }

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addEmbeds(EmbedBuilder... embeds) {
        delegate.addEmbeds(embeds);
        return this;
    }

    /**
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder removeEmbed(EmbedBuilder embed) {
        delegate.removeEmbed(embed);
        return this;
    }

    /**
     * Removes the embeds from the message.
     *
     * @param embeds The embeds to remove.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder removeEmbeds(EmbedBuilder... embeds) {
        delegate.removeEmbeds(embeds);
        return this;
    }

    /**
     * Removes all embeds from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder removeAllEmbeds() {
        delegate.removeAllEmbeds();
        return this;
    }

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder setTts(boolean tts) {
        delegate.setTts(tts);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(BufferedImage, String)
     */
    public InteractionMessageBuilder addFile(BufferedImage image, String fileName) {
        delegate.addFile(image, fileName);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(File)
     */
    public InteractionMessageBuilder addFile(File file) {
        delegate.addFile(file);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(Icon)
     */
    public InteractionMessageBuilder addFile(Icon icon) {
        delegate.addFile(icon);
        return this;
    }

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    public InteractionMessageBuilder addFile(URL url) {
        delegate.addFile(url);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(byte[], String)
     */
    public InteractionMessageBuilder addFile(byte[] bytes, String fileName) {
        delegate.addFile(bytes, fileName);
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    public InteractionMessageBuilder addFile(InputStream stream, String fileName) {
        delegate.addFile(stream, fileName);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(BufferedImage, String)
     */
    public InteractionMessageBuilder addFileAsSpoiler(BufferedImage image, String fileName) {
        delegate.addFile(image, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(File)
     */
    public InteractionMessageBuilder addFileAsSpoiler(File file) {
        delegate.addFileAsSpoiler(file);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(Icon)
     */
    public InteractionMessageBuilder addFileAsSpoiler(Icon icon) {
        delegate.addFileAsSpoiler(icon);
        return this;
    }

    /**
     * Adds a file to the message and marks it as a spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(URL)
     */
    public InteractionMessageBuilder addFileAsSpoiler(URL url) {
        delegate.addFileAsSpoiler(url);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachmentAsSpoiler(byte[], String)
     */
    public InteractionMessageBuilder addFileAsSpoiler(byte[] bytes, String fileName) {
        delegate.addFile(bytes, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds a file to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    public InteractionMessageBuilder addFileAsSpoiler(InputStream stream, String fileName) {
        delegate.addFile(stream, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachment(BufferedImage image, String fileName) {
        delegate.addAttachment(image, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachment(File file) {
        delegate.addAttachment(file);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachment(Icon icon) {
        delegate.addAttachment(icon);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachment(URL url) {
        delegate.addAttachment(url);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachment(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachment(InputStream stream, String fileName) {
        delegate.addAttachment(stream, fileName);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param image The image to add as an attachment.
     * @param fileName The file name of the image.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        delegate.addAttachment(image, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param file The file to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachmentAsSpoiler(File file) {
        delegate.addAttachmentAsSpoiler(file);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param icon The icon to add as an attachment.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachmentAsSpoiler(Icon icon) {
        delegate.addAttachmentAsSpoiler(icon);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachmentAsSpoiler(URL url) {
        delegate.addAttachmentAsSpoiler(url);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param bytes The bytes of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Adds an attachment to the message and marks it as spoiler.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder addAttachmentAsSpoiler(InputStream stream, String fileName) {
        delegate.addAttachment(stream, "SPOILER_" + fileName);
        return this;
    }

    /**
     * Controls who will be mentioned if mentions exist in the message.
     *
     * @param allowedMentions The mention object.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder setAllowedMentions(AllowedMentions allowedMentions) {
        delegate.setAllowedMentions(allowedMentions);
        return this;
    }

    /**
     * Sets the message flags of the message.
     *
     * @param messageFlags The message flags enum type.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder setFlags(MessageFlag... messageFlags) {
        setFlags(EnumSet.copyOf(Arrays.asList(messageFlags)));
        return this;
    }

    /**
     * Sets the message flags of the message.
     *
     * @param messageFlags An EnumSet of message flag enum type.
     * @return The current instance in order to chain call methods.
     */
    public InteractionMessageBuilder setFlags(EnumSet<MessageFlag> messageFlags) {
        delegate.setFlags(messageFlags);
        return this;
    }

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    public StringBuilder getStringBuilder() {
        return delegate.getStringBuilder();
    }

    /**
     * Sends the first response message.
     * This can only be done once after you want to respond to an interaction the FIRST time.
     * Responding directly to an interaction limits you to not being able to upload anything.
     * Therefore i.e. {@link EmbedBuilder#setFooter(String, File)} will not work and you have to use the String methods
     * for attachments like {@link EmbedBuilder#setFooter(String, String)} if available.
     * If you want to upload attachments use {@link #editOriginalResponse(Interaction)} instead.
     *
     * @param interaction The interaction to send the response.
     * @return The CompletableFuture when your message was sent.
     */
    public CompletableFuture<Void> sendInitialResponse(Interaction interaction) {
        return delegate.sendInitialResponse(interaction);
    }

    /**
     * Edits your original sent response.
     * Your original response may be sent by {@link #sendInitialResponse(Interaction)}
     * or by deciding to respond through {@link InteractionCreateEvent#respondLater()} which sends a "loading state"
     * as your first response.
     * In comparison to {@link #sendInitialResponse(Interaction)} this method allows you to upload attachments
     * with your message.
     *
     * @param interaction The interaction to send the response.
     * @return The sent message.
     */
    public CompletableFuture<Message> editOriginalResponse(Interaction interaction) {
        return delegate.editOriginalResponse(interaction);
    }

    /**
     * Sends a followup message to an interaction.
     *
     * @param interaction The interaction to send the followup message to.
     * @return The sent message.
     */
    public CompletableFuture<Message> sendFollowupMessage(Interaction interaction) {
        return delegate.sendFollowupMessage(interaction);
    }

    /**
     * Edits a followup message from an interaction.
     *
     * @param interaction The interaction where the edited message belongs to.
     * @param messageId The message id of the followup message which should be edited.
     * @return The edited message.
     */
    public CompletableFuture<Message> editFollowupMessage(Interaction interaction, long messageId) {
        return editFollowupMessage(interaction, Long.toUnsignedString(messageId));
    }

    /**
     * Edits a followup message from an interaction.
     *
     * @param interaction The interaction where the edited message belongs to.
     * @param messageId The message id of the followup message which should be edited.
     * @return The edited message.
     */
    public CompletableFuture<Message> editFollowupMessage(Interaction interaction, String messageId) {
        return delegate.editFollowupMessage(interaction, messageId);
    }

}
