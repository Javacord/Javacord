package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public interface MessageBuilderEmbed<T extends MessageBuilderEmbed<T>> {

    /**
     * Gets the delegate of this message builder.
     *
     * @return The delegate of this message builder.
     */
    MessageBuilderBaseDelegate getDelegate();

    /**
     * Sets the embed of the message (overrides all existing embeds).
     *
     * @param embed The embed to set.
     * @return The current instance in order to chain call methods.
     */
    default T setEmbed(EmbedBuilder embed) {
        removeAllEmbeds();
        getDelegate().addEmbed(embed);
        return (T) this;
    }

    /**
     * Sets multiple embeds of the message (overrides all existing embeds).
     *
     * @param embeds The embed to set.
     * @return The current instance in order to chain call methods.
     */
    default T setEmbeds(EmbedBuilder... embeds) {
        removeAllEmbeds();
        addEmbeds(Arrays.asList(embeds));
        return (T) this;
    }

    /**
     * Sets multiple embeds of the message (overrides all existing embeds).
     *
     * @param embeds The embed to set.
     * @return The current instance in order to chain call methods.
     */
    default T setEmbeds(List<EmbedBuilder> embeds) {
        removeAllEmbeds();
        embeds.forEach(this::addEmbed);
        return (T) this;
    }

    /**
     * Adds an embed to the message.
     *
     * @param embed The embed to add.
     * @return The current instance in order to chain call methods.
     */
    default T addEmbed(EmbedBuilder embed) {
        getDelegate().addEmbed(embed);
        return (T) this;
    }

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     * @return The current instance in order to chain call methods.
     */
    default T addEmbeds(EmbedBuilder... embeds) {
        for (EmbedBuilder embed : embeds) {
            addEmbed(embed);
        }
        return (T) this;
    }

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     * @return The current instance in order to chain call methods.
     */
    default T addEmbeds(List<EmbedBuilder> embeds) {
        embeds.forEach(this::addEmbed);
        return (T) this;
    }

    /**
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     * @return The current instance in order to chain call methods.
     */
    default T removeEmbed(EmbedBuilder embed) {
        getDelegate().removeEmbed(embed);
        return (T) this;
    }

    /**
     * Removes the embeds from the message.
     *
     * @param embeds The embeds to remove.
     * @return The current instance in order to chain call methods.
     */
    default T removeEmbeds(EmbedBuilder... embeds) {
        for (EmbedBuilder embed : embeds) {
            removeEmbed(embed);
        }
        return (T) this;
    }

    /**
     * Removes all embeds from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    default T removeAllEmbeds() {
        getDelegate().removeAllEmbeds();
        return (T) this;
    }
}
