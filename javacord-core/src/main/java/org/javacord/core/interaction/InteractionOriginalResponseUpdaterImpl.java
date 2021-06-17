package org.javacord.core.interaction;

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
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class InteractionOriginalResponseUpdaterImpl
        extends ExtendedInteractionMessageBuilderBaseImpl<InteractionOriginalResponseUpdater>
        implements InteractionOriginalResponseUpdater {

    private final InteractionImpl interaction;

    /**
     * Class constructor.
     *
     * @param interaction The interaction to use.
     * @param delegate    An already used delegate if the caller just sent the initial response
     */
    public InteractionOriginalResponseUpdaterImpl(InteractionBase interaction,
                                                  InteractionMessageBuilderDelegate delegate) {
        super(InteractionOriginalResponseUpdater.class, delegate);
        this.interaction = (InteractionImpl) interaction;
    }

    /**
     * Class constructor.
     *
     * @param interaction The interaction to use.
     */
    public InteractionOriginalResponseUpdaterImpl(InteractionBase interaction) {
        super(InteractionOriginalResponseUpdater.class);
        this.interaction = (InteractionImpl) interaction;
    }

    @Override
    public CompletableFuture<Message> update() {
        return this.delegate.editOriginalResponse(interaction);
    }

    @Override
    public CompletableFuture<Void> delete() {
        return this.delegate.deleteInitialResponse(interaction);
    }

    @Override
    public InteractionOriginalResponseUpdater appendCode(String language, String code) {
        delegate.appendCode(language, code);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater append(String message, MessageDecoration... decorations) {
        delegate.append(message, decorations);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater append(Mentionable entity) {
        delegate.append(entity);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater append(Object object) {
        delegate.append(object);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater appendNewLine() {
        delegate.appendNewLine();
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater setContent(String content) {
        delegate.setContent(content);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addEmbed(EmbedBuilder embed) {
        delegate.addEmbed(embed);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addEmbeds(EmbedBuilder... embeds) {
        delegate.addEmbeds(embeds);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addComponents(HighLevelComponent... components) {
        delegate.addComponents(components);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater copy(Message message) {
        delegate.copy(message);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater copy(InteractionBase interaction) {
        delegate.copy(interaction);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater removeAllComponents() {
        delegate.removeAllComponents();
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater removeComponent(int index) {
        delegate.removeComponent(index);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater removeComponent(HighLevelComponent component) {
        delegate.removeComponent(component);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater removeEmbed(EmbedBuilder embed) {
        delegate.removeEmbed(embed);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater removeEmbeds(EmbedBuilder... embeds) {
        delegate.removeEmbeds(embeds);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater removeAllEmbeds() {
        delegate.removeAllEmbeds();
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater setTts(boolean tts) {
        delegate.setTts(tts);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFile(BufferedImage image, String fileName) {
        delegate.addFile(image, fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFile(File file) {
        delegate.addFile(file);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFile(Icon icon) {
        delegate.addFile(icon);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFile(URL url) {
        delegate.addFile(url);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFile(byte[] bytes, String fileName) {
        delegate.addFile(bytes, fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFile(InputStream stream, String fileName) {
        delegate.addFile(stream, fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFileAsSpoiler(BufferedImage image, String fileName) {
        delegate.addFile(image, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFileAsSpoiler(File file) {
        delegate.addFileAsSpoiler(file);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFileAsSpoiler(Icon icon) {
        delegate.addFileAsSpoiler(icon);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFileAsSpoiler(URL url) {
        delegate.addFileAsSpoiler(url);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFileAsSpoiler(byte[] bytes, String fileName) {
        delegate.addFile(bytes, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addFileAsSpoiler(InputStream stream, String fileName) {
        delegate.addFile(stream, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(BufferedImage image, String fileName) {
        delegate.addAttachment(image, fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(File file) {
        delegate.addAttachment(file);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(Icon icon) {
        delegate.addAttachment(icon);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(URL url) {
        delegate.addAttachment(url);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(InputStream stream, String fileName) {
        delegate.addAttachment(stream, fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        delegate.addAttachment(image, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(File file) {
        delegate.addAttachmentAsSpoiler(file);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(Icon icon) {
        delegate.addAttachmentAsSpoiler(icon);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(URL url) {
        delegate.addAttachmentAsSpoiler(url);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(InputStream stream, String fileName) {
        delegate.addAttachment(stream, "SPOILER_" + fileName);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater setAllowedMentions(AllowedMentions allowedMentions) {
        delegate.setAllowedMentions(allowedMentions);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater setFlags(MessageFlag... messageFlags) {
        setFlags(EnumSet.copyOf(Arrays.asList(messageFlags)));
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater setFlags(EnumSet<MessageFlag> messageFlags) {
        delegate.setFlags(messageFlags);
        return this;
    }
}
