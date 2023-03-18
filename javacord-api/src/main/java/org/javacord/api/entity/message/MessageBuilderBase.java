package org.javacord.api.entity.message;

import org.javacord.api.entity.message.internal.MessageBuilderAttachment;
import org.javacord.api.entity.message.internal.MessageBuilderBaseDelegate;
import org.javacord.api.entity.message.internal.MessageBuilderComponent;
import org.javacord.api.entity.message.internal.MessageBuilderContent;
import org.javacord.api.entity.message.internal.MessageBuilderEmbed;
import org.javacord.api.entity.message.internal.MessageBuilderMiscellaneous;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.util.internal.DelegateFactory;

abstract class MessageBuilderBase<T extends MessageBuilderBase<T>> implements MessageBuilderContent<T>,
        MessageBuilderComponent<T>,
        MessageBuilderAttachment<T>,
        MessageBuilderEmbed<T>,
        MessageBuilderMiscellaneous<T> {
    private final Class<T> myClass;

    protected MessageBuilderBase(Class<T> myClass) {
        this.myClass = myClass;
    }

    /**
     * The message delegate used by this instance.
     */
    protected final MessageBuilderBaseDelegate delegate = DelegateFactory.createMessageBuilderBaseDelegate();

    @Override
    public MessageBuilderBaseDelegate getDelegate() {
        return delegate;
    }

    /**
     * Controls who will be mentioned if mentions exist in the message.
     *
     * @param allowedMentions The mention object.
     * @return The current instance in order to chain call methods.
     */
    public T setAllowedMentions(AllowedMentions allowedMentions) {
        delegate.setAllowedMentions(allowedMentions);
        return myClass.cast(this);
    }

    /**
     * Sets the nonce of the message.
     *
     * @param nonce The nonce to set.
     * @return The current instance in order to chain call methods.
     */
    public T setNonce(String nonce) {
        delegate.setNonce(nonce);
        return myClass.cast(this);
    }

}
