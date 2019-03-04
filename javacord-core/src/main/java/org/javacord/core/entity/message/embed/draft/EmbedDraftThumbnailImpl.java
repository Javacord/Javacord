package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.core.util.JsonNodeable;

import java.util.Optional;

public class EmbedDraftThumbnailImpl extends EmbedDraftFileContainerAttachableMember
        implements EmbedDraftThumbnail, JsonNodeable {
    private final EmbedDraft parent;

    protected EmbedDraftThumbnailImpl(EmbedDraft parent, String url) {
        this.parent = parent;

        this.fileUri = url;
    }

    public EmbedDraftThumbnailImpl(BaseEmbed parent, BaseEmbedThumbnail embedThumbnail) {
        this(parent.toEmbedDraft(), embedThumbnail.getUrl().orElse(null));
        if (fileUri == null) {
            container = ((EmbedDraftFileContainerAttachableMember) embedThumbnail).container;
        }
    }

    @Override
    public EmbedDraft getEmbed() {
        return parent;
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
    public EmbedDraftThumbnail toEmbedDraftThumbnail() {
        return this;
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "thumbnail";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("url", fileUri);

        return jsonFieldName;
    }
}
