package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URL;
import java.util.Optional;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;
import org.javacord.core.util.JsonNodeable;

public class EmbedDraftThumbnailImpl extends EmbedDraftFileContainerAttachableMember<EmbedDraftThumbnail, SentEmbedThumbnail>
        implements EmbedDraftThumbnail, JsonNodeable {
    protected EmbedDraftThumbnailImpl(EmbedDraft parent, String url) {
        super(parent, EmbedDraftThumbnail.class, SentEmbedThumbnail.class);
        this.fileUri = url;
    }

    public EmbedDraftThumbnailImpl(BaseEmbed parent, BaseEmbedThumbnail embedThumbnail) {
        this(parent.toEmbedDraft(), embedThumbnail.getUrl().orElse(null));
        if (fileUri == null) {
            container = ((EmbedDraftFileContainerAttachableMember) embedThumbnail).container;
        }
    }

    @Override
    public EmbedDraftThumbnail setUrl(String url) {
        this.fileUri = url;
        return this;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(fileUri);
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "thumbnail";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("url", fileUri);

        return jsonFieldName;
    }
}
