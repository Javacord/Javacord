package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Optional;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;
import org.javacord.core.util.JsonNodeable;

public class EmbedDraftAuthorImpl extends EmbedDraftFileContainerAttachableMember<EmbedDraftAuthor, SentEmbedAuthor>
        implements EmbedDraftAuthor, JsonNodeable {
    private String name;
    private String url;

    protected EmbedDraftAuthorImpl(EmbedDraft parent, String name, String url, String iconUrl) {
        super(parent, EmbedDraftAuthor.class, SentEmbedAuthor.class);

        this.name = name;
        this.url = url;
        this.fileUri = iconUrl;
    }

    public EmbedDraftAuthorImpl(BaseEmbed parent, BaseEmbedAuthor baseEmbedAuthor) {
        this(parent.toEmbedDraft(),
                baseEmbedAuthor.getName(),
                baseEmbedAuthor.getUrl().orElse(null),
                baseEmbedAuthor.getUrl().orElse(null));
    }

    @Override
    public EmbedDraftAuthor setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public EmbedDraftAuthor setIconUrl(String url) {
        this.fileUri = url;
        return this;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public Optional<String> getIconUrl() {
        return Optional.ofNullable(fileUri);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "author";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("name", name);
        node.put("url", url);
        node.put("icon_url", fileUri);

        return jsonFieldName;
    }
}
