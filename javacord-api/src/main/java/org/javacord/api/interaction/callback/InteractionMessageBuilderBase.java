package org.javacord.api.interaction.callback;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.mention.AllowedMentions;

import java.util.EnumSet;

public interface InteractionMessageBuilderBase<T> {
    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code     The code.
     * @return The current instance in order to chain call methods.
     */
    T appendCode(String language, String code);

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message     The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    T append(String message, MessageDecoration... decorations);

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    T append(Mentionable entity);

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    T append(Object object);

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    T appendNewLine();

    /**
     * Sets the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @param content The new content of the message.
     * @return The current instance in order to chain call methods.
     */
    T setContent(String content);

    /**
     * Adds the embed to the message.
     *
     * @param embed The embed to add.
     * @return The current instance in order to chain call methods.
     */
    T addEmbed(EmbedBuilder embed);

    /**
     * Adds the embeds to the message.
     *
     * @param embeds The embeds to add.
     * @return The current instance in order to chain call methods.
     */
    T addEmbeds(EmbedBuilder... embeds);

    /**
     * Adds multiple components to the message.
     *
     * @param components The components.
     * @return The current instance in order to chain call methods.
     */
    T addComponents(HighLevelComponent... components);

    /**
     * Removes all components from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    T removeAllComponents();

    /**
     * Remove a component from the message.
     *
     * @param index The index placement to remove from.
     * @return The current instance in order to chain call methods.
     */
    T removeComponent(int index);

    /**
     * Remove a component from the message.
     *
     * @param component The component.
     * @return The current instance in order to chain call methods.
     */
    T removeComponent(HighLevelComponent component);

    /**
     * Removes the embed from the message.
     *
     * @param embed The embed to remove.
     * @return The current instance in order to chain call methods.
     */
    T removeEmbed(EmbedBuilder embed);

    /**
     * Removes the embeds from the message.
     *
     * @param embeds The embeds to remove.
     * @return The current instance in order to chain call methods.
     */
    T removeEmbeds(EmbedBuilder... embeds);

    /**
     * Removes all embeds from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    T removeAllEmbeds();

    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     * @return The current instance in order to chain call methods.
     */
    T setTts(boolean tts);

    /**
     * Controls who will be mentioned if mentions exist in the message.
     *
     * @param allowedMentions The mention object.
     * @return The current instance in order to chain call methods.
     */
    T setAllowedMentions(AllowedMentions allowedMentions);

    /**
     * Sets the message flags of the message.
     *
     * @param messageFlags The message flags enum type.
     * @return The current instance in order to chain call methods.
     */
    T setFlags(MessageFlag... messageFlags);

    /**
     * Sets the message flags of the message.
     *
     * @param messageFlags An EnumSet of message flag enum type.
     * @return The current instance in order to chain call methods.
     */
    T setFlags(EnumSet<MessageFlag> messageFlags);

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    StringBuilder getStringBuilder();
}
