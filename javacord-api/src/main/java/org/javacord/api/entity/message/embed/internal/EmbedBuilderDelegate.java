package org.javacord.api.entity.message.embed.internal;

import java.awt.Color;
import java.time.Instant;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;

/**
 * This class is internally used by the {@link EmbedBuilder} to created embeds.
 * You usually don't want to interact with this object.
 */
public interface EmbedBuilderDelegate {
    void setTitle(String title);

    void setDescription(String description);

    void setUrl(String url);

    void setTimestamp(Instant timestamp);

    void setColor(Color color);

    void setAuthor(String name, String url, String iconUrl);

    void setAuthor(String name, String url, Object icon, String fileType);

    <T extends BaseEmbedAuthor & BaseEmbedMember<?, ? extends EmbedDraftAuthor, ?>> void setAuthor(T author);

    void setThumbnail(String url);

    void setThumbnail(Object image, String fileType);

    <T extends BaseEmbedThumbnail & BaseEmbedMember<?, ? extends EmbedDraftThumbnail, ?>> void setThumbnail(T thumbnail);

    void setImage(String url);

    void setImage(Object image, String fileType);

    <T extends BaseEmbedImage & BaseEmbedMember<?, ? extends EmbedDraftImage, ?>> void setImage(T image);

    void setFooter(String text, String iconUrl);

    void setFooter(String text, Object icon, String fileType);

    <T extends BaseEmbedFooter & BaseEmbedMember<?, ? extends EmbedDraftFooter, ?>> void setFooter(T footer);

    void addField(String name, String value, boolean inline);

    void addField(BaseEmbedField field);

    EmbedDraft build();
}