package org.javacord.api.interaction.callback;

import org.javacord.api.entity.message.internal.MessageBuilderComponent;
import org.javacord.api.entity.message.internal.MessageBuilderContent;
import org.javacord.api.entity.message.internal.MessageBuilderEmbed;
import org.javacord.api.entity.message.internal.MessageBuilderMiscellaneous;

public interface InteractionMessageBuilderBase<T extends InteractionMessageBuilderBase<T>>
        extends MessageBuilderContent<T>,
        MessageBuilderEmbed<T>,
        MessageBuilderComponent<T>,
        MessageBuilderMiscellaneous<T> {

    /**
     * Appends a named link "[name](link)" to the message.
     * Only available in response to an interaction.
     *
     * @param name The displayed name of the link.
     * @param url  The URL of the link.
     * @return The current instance in order to chain call methods.
     */
    default T appendNamedLink(String name, String url) {
        getDelegate().appendNamedLink(name, url);
        return (T) this;
    }

}
