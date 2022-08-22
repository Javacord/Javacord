package org.javacord.core.interaction;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.callback.ExtendedInteractionMessageBuilderBase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public abstract class ExtendedInteractionMessageBuilderBaseImpl<T>
        extends InteractionMessageBuilderBaseImpl<T>
        implements ExtendedInteractionMessageBuilderBase<T> {


    /**
     * Class constructor.
     *
     * @param myClass The interface to cast to for call chaining.
     */
    public ExtendedInteractionMessageBuilderBaseImpl(Class<T> myClass) {
        super(myClass);
    }

    /**
     * Class constructor.
     *
     * @param myClass The interface to cast to for call chaining.
     * @param delegate The delegate to use if required.
     */
    public ExtendedInteractionMessageBuilderBaseImpl(Class<T> myClass, InteractionMessageBuilderDelegate delegate) {
        super(myClass, delegate);
    }

    @Override
    public T copy(Message message) {
        delegate.copy(message);
        return myClass.cast(this);
    }

    @Override
    public T copy(InteractionBase interaction) {
        delegate.copy(interaction);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(BufferedImage image, String fileName) {
        addAttachment(image, fileName, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, fileName,  description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(File file) {
        addAttachment(file, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(File file, String description) {
        delegate.addAttachment(file, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(Icon icon) {
        addAttachment(icon, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(Icon icon, String description) {
        delegate.addAttachment(icon, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(URL url) {
        addAttachment(url, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(URL url, String description) {
        delegate.addAttachment(url, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(byte[] bytes, String fileName) {
        addAttachment(bytes, fileName, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, fileName, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(InputStream stream, String fileName) {
        addAttachment(stream, fileName, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, fileName, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        addAttachment(image, "SPOILER_" + fileName, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(BufferedImage image, String fileName, String description) {
        delegate.addAttachment(image, "SPOILER_" + fileName, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(File file) {
        addAttachmentAsSpoiler(file, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(File file, String description) {
        delegate.addAttachmentAsSpoiler(file, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(Icon icon) {
        addAttachmentAsSpoiler(icon, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(Icon icon, String description) {
        delegate.addAttachmentAsSpoiler(icon, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(URL url) {
        addAttachmentAsSpoiler(url, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(URL url, String description) {
        delegate.addAttachmentAsSpoiler(url, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        addAttachment(bytes, "SPOILER_" + fileName, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(byte[] bytes, String fileName, String description) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName, description);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(InputStream stream, String fileName) {
        addAttachment(stream, "SPOILER_" + fileName, null);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(InputStream stream, String fileName, String description) {
        delegate.addAttachment(stream, "SPOILER_" + fileName, description);
        return myClass.cast(this);
    }
}
