package org.javacord.api.interaction.callback;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

/**
 * This class is intended to be used by advanced users that desire full control over interaction responses.
 * We strongly recommend to use the offered methods on your received interactions instead of this class.
 */
public class InteractionMessageBuilder implements ExtendedInteractionMessageBuilderBase<InteractionMessageBuilder> {

    protected final InteractionMessageBuilderDelegate delegate =
            DelegateFactory.createInteractionMessageBuilderDelegate();

    /**
     * Sends the first response message.
     * This can only be done once after you want to respond to an interaction the FIRST time.
     * Responding directly to an interaction limits you to not being able to upload anything.
     * Therefore i.e. {@link EmbedBuilder#setFooter(String, File)} will not work and you have to use the String methods
     * for attachments like {@link EmbedBuilder#setFooter(String, String)} if available.
     * If you want to upload attachments use {@link #editOriginalResponse(InteractionBase)} instead.
     *
     * @param interaction The interaction.
     * @return The CompletableFuture when your message was sent.
     */
    public CompletableFuture<InteractionMessageBuilder> sendInitialResponse(InteractionBase interaction) {
        CompletableFuture<InteractionMessageBuilder> future = new CompletableFuture<>();

        CompletableFuture<Void> job = delegate.sendInitialResponse(interaction)
                .thenRun(() -> {
                    future.complete(this);
                })
                .exceptionally(e -> {
                    future.completeExceptionally(e);
                    return null;
                });
        return future;
    }

    /**
     * Edits your original sent response.
     * Your original response may be sent by {@link #sendInitialResponse(InteractionBase)}
     * or by deciding to respond through {@link InteractionBase#respondLater()} which sends a "loading state"
     * as your first response.
     * In comparison to {@link #sendInitialResponse(InteractionBase)} this method allows you to upload attachments
     * with your message.
     *
     * @param interaction The interaction.
     * @return The edited message.
     */
    public CompletableFuture<Message> editOriginalResponse(InteractionBase interaction) {
        return delegate.editOriginalResponse(interaction);
    }

    /**
     * Sends a followup message to an interaction.
     *
     * @param interaction The interaction.
     * @return The sent message.
     */
    public CompletableFuture<Message> sendFollowupMessage(InteractionBase interaction) {
        return delegate.sendFollowupMessage(interaction);
    }

    /**
     * Edits a followup message from an interaction.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be edited.
     * @return The edited message.
     */
    public CompletableFuture<Message> editFollowupMessage(InteractionBase interaction, long messageId) {
        return editFollowupMessage(interaction, Long.toUnsignedString(messageId));
    }

    /**
     * Edits a followup message from an interaction.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be edited.
     * @return The edited message.
     */
    public CompletableFuture<Message> editFollowupMessage(InteractionBase interaction, String messageId) {
        return delegate.editFollowupMessage(interaction, messageId);
    }

    /**
     * Update the message the components were attached to.
     *
     * @param interaction The original interaction.
     * @return The completable future to determine if the message was updated.
     */
    public CompletableFuture<Void> updateOriginalMessage(InteractionBase interaction) {
        return delegate.updateOriginalMessage(interaction);
    }

    /**
     * Delete the original response.
     *
     * @param interaction The interaction.
     * @return The completable future when the message has been deleted.
     */
    public CompletableFuture<Void> deleteInitialResponse(InteractionBase interaction) {
        return delegate.deleteInitialResponse(interaction);
    }

    /**
     * Delete a followup message from an interaction.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be deleted.
     * @return The deleted message.
     */
    public CompletableFuture<Void> deleteFollowupMessage(InteractionBase interaction, String messageId) {
        return delegate.deleteFollowupMessage(interaction, messageId);
    }

    @Override
    public InteractionMessageBuilder appendCode(String language, String code) {
        delegate.appendCode(language, code);
        return this;
    }

    @Override
    public InteractionMessageBuilder append(String message, MessageDecoration... decorations) {
        delegate.append(message, decorations);
        return this;
    }

    @Override
    public InteractionMessageBuilder append(Mentionable entity) {
        delegate.append(entity);
        return this;
    }

    @Override
    public InteractionMessageBuilder append(Object object) {
        delegate.append(object);
        return this;
    }

    @Override
    public InteractionMessageBuilder appendNewLine() {
        delegate.appendNewLine();
        return this;
    }

    @Override
    public InteractionMessageBuilder setContent(String content) {
        delegate.setContent(content);
        return this;
    }

    @Override
    public InteractionMessageBuilder addEmbed(EmbedBuilder embed) {
        delegate.addEmbed(embed);
        return this;
    }

    @Override
    public InteractionMessageBuilder addEmbeds(EmbedBuilder... embeds) {
        delegate.addEmbeds(embeds);
        return this;
    }

    @Override
    public InteractionMessageBuilder addComponents(HighLevelComponent... components) {
        delegate.addComponents(components);
        return this;
    }

    @Override
    public InteractionMessageBuilder removeAllComponents() {
        delegate.removeAllComponents();
        return this;
    }

    @Override
    public InteractionMessageBuilder removeComponent(int index) {
        delegate.removeComponent(index);
        return this;
    }

    @Override
    public InteractionMessageBuilder removeComponent(HighLevelComponent component) {
        delegate.removeComponent(component);
        return this;
    }

    @Override
    public InteractionMessageBuilder removeEmbed(EmbedBuilder embed) {
        delegate.removeEmbed(embed);
        return this;
    }

    @Override
    public InteractionMessageBuilder removeEmbeds(EmbedBuilder... embeds) {
        delegate.removeEmbeds(embeds);
        return this;
    }

    @Override
    public InteractionMessageBuilder removeAllEmbeds() {
        delegate.removeAllEmbeds();
        return this;
    }

    @Override
    public InteractionMessageBuilder setTts(boolean tts) {
        delegate.setTts(tts);
        return this;
    }

    @Override
    public InteractionMessageBuilder setAllowedMentions(AllowedMentions allowedMentions) {
        delegate.setAllowedMentions(allowedMentions);
        return this;
    }

    @Override
    public InteractionMessageBuilder setFlags(MessageFlag... messageFlags) {
        setFlags(EnumSet.copyOf(Arrays.asList(messageFlags)));
        return this;
    }

    @Override
    public InteractionMessageBuilder setFlags(EnumSet<MessageFlag> messageFlags) {
        delegate.setFlags(messageFlags);
        return this;
    }

    @Override
    public StringBuilder getStringBuilder() {
        return delegate.getStringBuilder();
    }

    @Override
    public InteractionMessageBuilder copy(Message message) {
        delegate.copy(message);
        return this;
    }

    @Override
    public InteractionMessageBuilder copy(InteractionBase interaction) {
        delegate.copy(interaction);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFile(BufferedImage image, String fileName) {
        delegate.addFile(image, fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFile(File file) {
        delegate.addFile(file);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFile(Icon icon) {
        delegate.addFile(icon);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFile(URL url) {
        delegate.addFile(url);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFile(byte[] bytes, String fileName) {
        delegate.addFile(bytes, fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFile(InputStream stream, String fileName) {
        delegate.addFile(stream, fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFileAsSpoiler(BufferedImage image, String fileName) {
        delegate.addFile(image, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFileAsSpoiler(File file) {
        delegate.addFileAsSpoiler(file);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFileAsSpoiler(Icon icon) {
        delegate.addFileAsSpoiler(icon);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFileAsSpoiler(URL url) {
        delegate.addFileAsSpoiler(url);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFileAsSpoiler(byte[] bytes, String fileName) {
        delegate.addFile(bytes, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addFileAsSpoiler(InputStream stream, String fileName) {
        delegate.addFile(stream, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(BufferedImage image, String fileName) {
        delegate.addAttachment(image, fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(File file) {
        delegate.addAttachment(file);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(Icon icon) {
        delegate.addAttachment(icon);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(URL url) {
        delegate.addAttachment(url);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(InputStream stream, String fileName) {
        delegate.addAttachment(stream, fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        delegate.addAttachment(image, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(File file) {
        delegate.addAttachmentAsSpoiler(file);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(Icon icon) {
        delegate.addAttachmentAsSpoiler(icon);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(URL url) {
        delegate.addAttachmentAsSpoiler(url);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(InputStream stream, String fileName) {
        delegate.addAttachment(stream, "SPOILER_" + fileName);
        return this;
    }
}
