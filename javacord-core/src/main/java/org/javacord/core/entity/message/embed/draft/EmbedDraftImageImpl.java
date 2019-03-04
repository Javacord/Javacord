package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.core.util.JsonNodeable;

import java.util.Optional;

public class EmbedDraftImageImpl extends EmbedDraftFileContainerAttachableMember
        implements EmbedDraftImage, JsonNodeable {

    private final EmbedDraft parent;

    protected EmbedDraftImageImpl(EmbedDraft parent, String url) {
        this.parent = parent;

        this.fileUri = url;
    }

    public EmbedDraftImageImpl(BaseEmbed parent, BaseEmbedImage member) {
        this(parent.toEmbedDraft(), member.getUrl().orElse(null));
        if (fileUri == null) {
            container = ((EmbedDraftFileContainerAttachableMember) member).container;
        }
    }

    @Override
    public EmbedDraft getEmbed() {
        return parent;
    }

    @Override
    public EmbedDraftImage setUrl(String url) {
        this.fileUri = url;
        return this;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(fileUri);
    }

    @Override
    public EmbedDraftImage toEmbedDraftImage() {
        return this;
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "image";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("url", fileUri);

        return jsonFieldName;
    }
}
