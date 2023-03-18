package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.mention.AllowedMentions;
import java.util.Arrays;
import java.util.EnumSet;

public interface MessageBuilderMiscellaneous<T extends MessageBuilderMiscellaneous<T>> {

    /**
     * Gets the delegate of this message builder.
     *
     * @return The delegate of this message builder.
     */
    MessageBuilderBaseDelegate getDelegate();

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     * @return The current instance in order to chain call methods.
     */
    default T setTts(boolean tts) {
        getDelegate().setTts(tts);
        return (T) this;
    }

    /**
     * Sets the interaction message flags of the message.
     *
     * @param messageFlags The message flags of the message.
     * @return The current instance in order to chain call methods.
     */
    default T setFlags(EnumSet<MessageFlag> messageFlags) {
        getDelegate().setFlags(messageFlags);
        return (T) this;
    }

    /**
     * Sets the message flags of the message.
     *
     * @param messageFlags The message flags enum type.
     * @return The current instance in order to chain call methods.
     */
    default T setFlags(MessageFlag... messageFlags) {
        return setFlags(EnumSet.copyOf(Arrays.asList(messageFlags)));
    }

    /**
     * Sets the interaction message flags of the message.
     *
     * @param messageFlag The message flag of the message.
     * @return The current instance in order to chain call methods.
     */
    default T addFlag(MessageFlag messageFlag) {
        getDelegate().addFlag(messageFlag);
        return (T) this;
    }

    /**
     * Adds the {@link MessageFlag#SUPPRESS_EMBEDS} flag to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    default T suppressEmbedding() {
        getDelegate().addFlag(MessageFlag.SUPPRESS_EMBEDS);
        return (T) this;
    }

    /**
     * Adds the {@link MessageFlag#SUPPRESS_NOTIFICATIONS} flag to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    default T suppressNotifications() {
        getDelegate().addFlag(MessageFlag.SUPPRESS_NOTIFICATIONS);
        return (T) this;
    }

    /**
     * Controls who will be mentioned if mentions exist in the message.
     *
     * @param allowedMentions The mention object.
     * @return The current instance in order to chain call methods.
     */
    default T setAllowedMentions(AllowedMentions allowedMentions) {
        getDelegate().setAllowedMentions(allowedMentions);
        return (T) this;
    }
}
