package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.TimestampStyle;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public interface MessageBuilderContent<T extends MessageBuilderContent<T>> {

    /**
     * Gets the delegate of this message builder.
     *
     * @return The delegate of this message builder.
     */
    MessageBuilderBaseDelegate getDelegate();

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    default StringBuilder getStringBuilder() {
        return getDelegate().getStringBuilder();
    }

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code     The code.
     * @return The current instance in order to chain call methods.
     */
    default T appendCode(final String language, final String code) {
        getDelegate().appendToStringBuilder("\n")
                .append(MessageDecoration.CODE_LONG.getPrefix())
                .append(language)
                .append("\n")
                .append(code)
                .append(MessageDecoration.CODE_LONG.getSuffix());
        return (T) this;
    }

    /**
     * Appends a timestamp to the message with the default timestamp style {@link TimestampStyle#SHORT_DATE_TIME}.
     *
     * @param epochSeconds The epoch time in seconds.
     * @return The current instance in order to chain call methods.
     */
    default T appendTimestamp(final long epochSeconds) {
        return appendTimestamp(epochSeconds, TimestampStyle.SHORT_DATE_TIME);
    }

    /**
     * Appends a timestamp to the message with the default timestamp style {@link TimestampStyle#SHORT_DATE_TIME}.
     *
     * @param instant The instant for the displaying timestamp.
     * @return The current instance in order to chain call methods.
     */
    default T appendTimestamp(final Instant instant) {
        return appendTimestamp(instant.getEpochSecond(), TimestampStyle.SHORT_DATE_TIME);
    }

    /**
     * Appends a timestamp to the message.
     *
     * @param epochSeconds   The epoch time in seconds.
     * @param timestampStyle The displayed timestamp style.
     * @return The current instance in order to chain call methods.
     */
    default T appendTimestamp(final long epochSeconds, final TimestampStyle timestampStyle) {
        getDelegate().appendToStringBuilder(timestampStyle.getTimestampTag(epochSeconds));
        return (T) this;
    }

    /**
     * Appends a timestamp to the message.
     *
     * @param instant        The instant for the displaying timestamp.
     * @param timestampStyle The displayed timestamp style.
     * @return The current instance in order to chain call methods.
     */
    default T appendTimestamp(final Instant instant, final TimestampStyle timestampStyle) {
        return appendTimestamp(instant.getEpochSecond(), timestampStyle);
    }

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message     The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    default T append(final String message, final MessageDecoration... decorations) {
        StringBuilder sb = getDelegate().appendToStringBuilder("");
        for (MessageDecoration decoration : decorations) {
            sb.append(decoration.getPrefix());
        }
        getStringBuilder().append(message);
        for (int i = decorations.length - 1; i >= 0; i--) {
            sb.append(decorations[i].getSuffix());
        }
        return (T) this;
    }

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    default T append(final Mentionable entity) {
        getDelegate().appendToStringBuilder(entity.getMentionTag());
        return (T) this;
    }

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    default T append(final Object object) {
        getDelegate().appendToStringBuilder(String.valueOf(object));
        return (T) this;
    }

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    default T appendNewLine() {
        getDelegate().appendToStringBuilder("\n");
        return (T) this;
    }

    /**
     * Sets the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @param content The new content of the message.
     * @return The current instance in order to chain call methods.
     */
    default T setContent(final String content) {
        getStringBuilder().setLength(0);
        getDelegate().appendToStringBuilder(content);
        return (T) this;
    }

    /**
     * Removes the content of the message.
     * This method overwrites all previous content changes
     * (using {@link #append(String, MessageDecoration...)} for example).
     *
     * @return The current instance in order to chain call methods.
     */
    default T removeContent() {
        return setContent(null);
    }

    /**
     * Appends a masked link "<a href="https://javacord.org/">Javacord</a>" to the message.
     *
     * @param text The text of the link.
     * @param url  The url.
     * @return The current instance in order to chain call methods.
     */
    default T appendMaskedLink(String text, String url) {
        return append("[").append(text).append("](").append(url).append(")");
    }

    /**
     * Appends a string as a header to the message.
     *
     * @param header The header to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendLargeHeader(String header) {
        return appendNewLine().append("#").append(header).appendNewLine();
    }

    /**
     * Appends a string as a header to the message.
     *
     * @param header The header to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendMediumHeader(String header) {
        return appendNewLine().append("##").append(header).appendNewLine();
    }

    /**
     * Appends a string as a header to the message.
     *
     * @param header The header to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendSmallHeader(String header) {
        return appendNewLine().append("###").append(header).appendNewLine();
    }

    /**
     * Append a list element to the message.
     *
     * @param listElement The list element to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendListElement(String listElement) {
        if (!getStringBuilder().toString().endsWith("\n")) {
            appendNewLine();
        }
        return append("- ").append(listElement).appendNewLine();
    }

    /**
     * Append a nested list element to the message.
     *
     * @param nestedListElement The nested list element to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendNestedListElement(String nestedListElement) {
        if (!getStringBuilder().toString().endsWith("\n")) {
            appendNewLine();
        }
        return append(" - ").append(nestedListElement).appendNewLine();
    }

    /**
     * Appends a list to the message.
     *
     * @param list The list to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendList(List<String> list) {
        list.forEach(this::appendListElement);
        return (T) this;
    }

    /**
     * Appends a list to the message.
     *
     * @param list The list to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendList(String... list) {
        return appendList(Arrays.asList(list));
    }

    /**
     * Appends a nested list to the message.
     * The structure will look like this:
     * <ul>
     *     <li>outerListName</li>
     *     <li><ul>
     *         <li>list[0]</li>
     *         <li>list[1]</li>
     *         <li>list[...]</li>
     *     </ul></li>
     * </ul>
     *
     * @param outerListName The outer list name to append.
     * @param list          The list to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendNestedList(String outerListName, List<String> list) {
        appendListElement(outerListName);
        list.forEach(this::appendNestedListElement);
        return (T) this;
    }

    /**
     * Appends a nested list to the message.
     * The structure will look like this:
     * <ul>
     *     <li>outerListName</li>
     *     <li><ul>
     *         <li>list[0]</li>
     *         <li>list[1]</li>
     *         <li>list[...]</li>
     *     </ul></li>
     * </ul>
     *
     * @param outerListName The outer list name to append.
     * @param list          The list to append.
     * @return The current instance in order to chain call methods.
     */
    default T appendNestedList(String outerListName, String... list) {
        return appendNestedList(outerListName, Arrays.asList(list));
    }

}
