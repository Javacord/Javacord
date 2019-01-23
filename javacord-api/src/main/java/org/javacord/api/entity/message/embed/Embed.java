package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.embed.sent.SentEmbed;

/**
 * This interface represents an embed.
 *
 * @deprecated Replaced by {@link BaseEmbed}, {@link SentEmbed} and {@link org.javacord.api.entity.message.embed.draft.EmbedDraft}.
 */
@Deprecated
public interface Embed extends SentEmbed {

    /**
     * Creates a builder, based on the embed.
     * You can use this method, if you want to resend an embed, you received as a message.
     *
     * @return A builder with the values of this embed.
     */
    @Override
    default EmbedBuilder toBuilder() {
        EmbedBuilder builder = new EmbedBuilder();
        getTitle().ifPresent(builder::setTitle);
        getDescription().ifPresent(builder::setDescription);
        getUrl().ifPresent(builder::setUrl);
        getTimestamp().ifPresent(builder::setTimestamp);
        getColor().ifPresent(builder::setColor);
        getFooter().ifPresent(footer -> builder.setFooter(
                footer.getText(), footer.getIconUrl().orElse(null)));
        getImage().ifPresent(image -> builder.setImage(image.getUrl()));
        getThumbnail().ifPresent(thumbnail -> builder.setThumbnail(thumbnail.getUrl()));
        getAuthor().ifPresent(author -> builder.setAuthor(
                author.getName(),
                author.getUrl().orElse(null),
                author.getIconUrl().orElse(null)));
        getFields().forEach(field -> builder.addField(field.getName(), field.getValue(), field.isInline()));
        return builder;
    }

}