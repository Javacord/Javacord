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
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is intended to be used by advanced users that desire full control over interaction responses.
 * We strongly recommend using the offered methods on your received interactions instead of this class.
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
        delegate.sendInitialResponse(interaction)
                .thenRun(() -> future.complete(this))
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
    public InteractionMessageBuilder appendNamedLink(final String name, final String url) {
        delegate.appendNamedLink(name, url);
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
        delegate.addEmbeds(Arrays.asList(embeds));
        return this;
    }

    @Override
    public InteractionMessageBuilder addEmbeds(List<EmbedBuilder> embeds) {
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
    public InteractionMessageBuilder addAttachment(BufferedImage image, String fileName) {
        addAttachment(image, fileName, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, fileName,  description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(File file) {
        addAttachment(file, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(File file, String description) {
        delegate.addAttachment(file, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(Icon icon) {
        delegate.addAttachment(icon, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(Icon icon, String description) {
        delegate.addAttachment(icon, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(URL url) {
        delegate.addAttachment(url, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(URL url, String description) {
        delegate.addAttachment(url, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(byte[] bytes, String fileName) {
        addAttachment(bytes, fileName, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, fileName, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(InputStream stream, String fileName) {
        addAttachment(stream, fileName, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachment(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, fileName, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        addAttachment(image, "SPOILER_" + fileName, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, "SPOILER_" + fileName, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(File file) {
        addAttachmentAsSpoiler(file, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(File file, String description) {
        delegate.addAttachmentAsSpoiler(file, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(Icon icon) {
        addAttachmentAsSpoiler(icon, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(Icon icon, String description) {
        delegate.addAttachmentAsSpoiler(icon, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(URL url) {
        addAttachmentAsSpoiler(url, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(URL url, String description) {
        delegate.addAttachmentAsSpoiler(url, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        addAttachment(bytes, "SPOILER_" + fileName, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName, description);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(InputStream stream, String fileName) {
        addAttachment(stream, "SPOILER_" + fileName, null);
        return this;
    }

    @Override
    public InteractionMessageBuilder addAttachmentAsSpoiler(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, "SPOILER_" + fileName, description);
        return this;
    }
}