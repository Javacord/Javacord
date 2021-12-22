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
        delegate.addAttachment(image, fileName);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(File file) {
        delegate.addAttachment(file);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(Icon icon) {
        delegate.addAttachment(icon);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(URL url) {
        delegate.addAttachment(url);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, fileName);
        return myClass.cast(this);
    }

    @Override
    public T addAttachment(InputStream stream, String fileName) {
        delegate.addAttachment(stream, fileName);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        delegate.addAttachment(image, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(File file) {
        delegate.addAttachmentAsSpoiler(file);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(Icon icon) {
        delegate.addAttachmentAsSpoiler(icon);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(URL url) {
        delegate.addAttachmentAsSpoiler(url);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        delegate.addAttachment(bytes, "SPOILER_" + fileName);
        return myClass.cast(this);
    }

    @Override
    public T addAttachmentAsSpoiler(InputStream stream, String fileName) {
        delegate.addAttachment(stream, "SPOILER_" + fileName);
        return myClass.cast(this);
    }
}
