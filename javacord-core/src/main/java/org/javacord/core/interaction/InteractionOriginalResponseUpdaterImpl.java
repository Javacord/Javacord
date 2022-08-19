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
        delegate.addEmbeds(Arrays.asList(embeds));
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
    public InteractionOriginalResponseUpdater addAttachment(BufferedImage image, String fileName) {
        addAttachment(image, fileName, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, fileName,  description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(File file) {
        addAttachment(file, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(File file, String description) {
        delegate.addAttachment(file, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(Icon icon) {
        addAttachment(icon, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(Icon icon, String description) {
        delegate.addAttachment(icon, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(URL url) {
        addAttachment(url, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(URL url, String description) {
        delegate.addAttachment(url, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(byte[] bytes, String fileName) {
        addAttachment(bytes, fileName, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, fileName, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(InputStream stream, String fileName) {
        addAttachment(stream, fileName, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachment(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, fileName, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        addAttachment(image, "SPOILER_" + fileName, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(BufferedImage image, String fileName,
                                                                     String description) {
        delegate.addAttachment(image, "SPOILER_" + fileName, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(File file) {
        addAttachmentAsSpoiler(file, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(File file, String description) {
        delegate.addAttachmentAsSpoiler(file, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(Icon icon) {
        addAttachmentAsSpoiler(icon, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(Icon icon, String description) {
        delegate.addAttachmentAsSpoiler(icon, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(URL url) {
        addAttachmentAsSpoiler(url, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(URL url, String description) {
        delegate.addAttachmentAsSpoiler(url, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        addAttachment(bytes, "SPOILER_" + fileName, null);
        return this;
    }


    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(byte[] bytes, String fileName,
                                                                     String description) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName, description);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(InputStream stream, String fileName) {
        addAttachment(stream, "SPOILER_" + fileName, null);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater addAttachmentAsSpoiler(InputStream stream, String fileName,
                                                                     String description) {
        delegate.addAttachment(stream, "SPOILER_" + fileName, description);
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
