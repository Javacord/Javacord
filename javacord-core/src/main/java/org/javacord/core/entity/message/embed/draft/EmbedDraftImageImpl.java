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
    private URL url;

    protected EmbedDraftImageImpl(EmbedDraft parent, URL url) {
        super(parent, EmbedDraftImage.class, SentEmbedImage.class);
        this.url = url;
    }

    public EmbedDraftImageImpl(EmbedDraft parent, String url) {
        this(parent, urlOrNull(url));
    }

    public EmbedDraftImageImpl(BaseEmbed parent, BaseEmbedImage member) {
        this(parent.toEmbedDraft(), member.getUrl().orElse(null));
        if (url == null) {
            container = ((EmbedDraftFileContainerAttachableMember) member).container;
        }
    }

    @Override
    public EmbedDraftImage setUrl(URL url) {
        this.url = url;
        return this;
    }

    @Override
    public Optional<URL> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "image";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("url", getAttachmentUrlAsString().orElse(getUrl().map(URL::toExternalForm).orElse(null)));

        return jsonFieldName;
    }
}
