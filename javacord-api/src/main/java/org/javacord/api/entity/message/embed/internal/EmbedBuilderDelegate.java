package org.javacord.api.entity.message.embed.internal;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;

/**
 * This class is internally used by the {@link EmbedBuilder} to created embeds.
 * You usually don't want to interact with this object.
 */
public interface EmbedBuilderDelegate {
    void setTitle(String title);

    void setDescription(String description);

    void setUrl(URL url);

    void setTimestamp(Instant timestamp);

    void setColor(Color color);

    void setAuthor(String name, URL url, URL iconUrl);

    void setAuthor(String name, URL url, Object icon, String fileType);

    void setAuthor(BaseEmbedAuthor author);

    void setThumbnail(URL url);

    void setThumbnail(Object image, String fileType);

    void setThumbnail(BaseEmbedThumbnail thumbnail);

    void setImage(URL url);

    void setImage(Object image, String fileType);

    void setImage(BaseEmbedImage image);

    void setFooter(String text, URL iconUrl);

    void setFooter(String text, Object icon, String fileType);

    void setFooter(BaseEmbedFooter footer);

    void addField(String name, String value, boolean inline);

    void addField(BaseEmbedField field);

    EmbedDraft build();
}