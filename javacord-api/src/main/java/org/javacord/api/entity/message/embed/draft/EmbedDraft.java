package org.javacord.api.entity.message.embed.draft;

import java.net.URL;
import org.javacord.api.entity.message.embed.BaseEmbed;

import java.awt.*;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EmbedDraft extends BaseEmbed {
    EmbedDraft setTitle(String title);

    EmbedDraft setDescription(String description);

    EmbedDraft setUrl(URL url);

    EmbedDraft setTimestamp(Instant timestamp);

    EmbedDraft setColor(Color color);

    EmbedDraft setAuthor(EmbedDraftAuthor author);

    EmbedDraft modifyAuthor(Consumer<EmbedDraftAuthor> authorFunction);

    EmbedDraft setThumbnail(EmbedDraftThumbnail thumbnail);

    EmbedDraft modifyThumbnail(Consumer<EmbedDraftThumbnail> thumbnailFunction);

    EmbedDraft setImage(EmbedDraftImage image);

    EmbedDraft modifyImage(Consumer<EmbedDraftImage> imageFunction);

    EmbedDraft setFooter(EmbedDraftFooter footer);

    EmbedDraft modifyFooter(Consumer<EmbedDraftFooter> footerFunction);

    EmbedDraft addField(EmbedDraftField field);

    EmbedDraft modifyFields(
            Predicate<EmbedDraftField> fieldPredicate,
            Function<EmbedDraftField, EmbedDraftField> fieldFunction
    );

    default EmbedDraft modifyAllFields(Function<EmbedDraftField, EmbedDraftField> fieldFunction) {
        return modifyFields(any -> true, fieldFunction);
    }

    EmbedDraft removeFields(Predicate<EmbedDraftField> fieldPredicate);

    default EmbedDraft removeAllFields() {
        return removeFields(any -> true);
    }

    boolean requiresAttachments();
}
