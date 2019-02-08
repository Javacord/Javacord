package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Optional;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftImage;
import org.javacord.api.entity.message.embed.sent.SentEmbedImage;
import org.javacord.core.util.JsonNodeable;

import java.net.URL;

public class EmbedDraftImageImpl extends EmbedDraftFileContainerAttachableMember<EmbedDraftImage, SentEmbedImage>
        implements EmbedDraftImage, JsonNodeable {

    protected EmbedDraftImageImpl(EmbedDraft parent, String url) {
        super(parent, EmbedDraftImage.class, SentEmbedImage.class);
        this.fileUri = url;
    }

    public EmbedDraftImageImpl(BaseEmbed parent, BaseEmbedImage member) {
        this(parent.toEmbedDraft(), member.getUrl().orElse(null));
        if (fileUri == null) {
            container = ((EmbedDraftFileContainerAttachableMember) member).container;
        }
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
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "image";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("url", fileUri);

        return jsonFieldName;
    }
}
